package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.repository.DocumentRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by hlupean on 17-Nov-16.
 */

@Component
public class DocumentController {
    @Autowired
    private DocumentRepository repoDocs;

    private static Logger log = LogManager.getLogger(DocumentController.class);
    private final Path rootLocation = Paths.get("./src/main/resources/DocumentVersions");

    private DocumentController() {
        log.info("DocumentController");
    }

    public void Sign(int documentId, User user) throws UserController.RequestException {
        if (repoDocs.NextToSign(documentId) == user.getGroupId()) {
            repoDocs.Sign(documentId);
        } else {
            throw new UserController.RequestException("You are not authorised.", HttpStatus.BAD_REQUEST);
        }
    }

    public void Reject(int documentId, User user) throws UserController.RequestException {
        if (repoDocs.NextToSign(documentId) == user.getGroupId()) {
            repoDocs.Reject(documentId);
        } else {
            throw new UserController.RequestException("You are not authorised.", HttpStatus.BAD_REQUEST);
        }
    }

    public String GetNameForUpload(int docType, User user) {
        Document document = repoDocs.getDocument(docType, user);

        String newName = "";
        newName = newName + user.getFirstName() + "_" + user.getLastName() + "_";
        newName = newName + document.getIdDocumentType() + "_";

        if (document.getStatus() == 1) {
            newName = newName + "DRAFT_0." + document.getVersionDraftMinor();
        } else if (document.getStatus() == 2) {
            newName = newName + "FINAL_1.0";
        } else {
            newName = newName + "FINAL_REVIZUIT_1." + document.getVersionFinRevMinor();
        }
        document.setBaseName(newName);

        return newName;
    }

    public String GetNameForDownload(int documentType, int documentStatus, User user) throws UserController.RequestException {
        try {
            if (repoDocs.UserHasDocument(user, documentType)) {
                return repoDocs.getNameForDownload(documentType, documentStatus, user.getUsername());
            }
            return repoDocs.GetDocumentTemplate(documentType);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UserController.RequestException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public void StoreFile(MultipartFile file, String name) throws UserController.RequestException {
        try {
            if (file.isEmpty()) {
                log.error("Uploaded file is empty");
                throw new UserController.RequestException("Uploaded file is empty", HttpStatus.BAD_REQUEST);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(name));
        } catch (IOException e) {
            log.error("Error at uploading file: " + e.getMessage());
            throw new UserController.RequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Path LoadFile(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource LoadFileAsResource(String filename) throws UserController.RequestException {
        try {
            Path file = LoadFile(this.rootLocation + "/" + filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                log.info("File successfully found");
                return resource;
            } else {
                log.error("Error at loading a document");
                throw new UserController.RequestException("Failed to load file", HttpStatus.BAD_REQUEST);
            }
        } catch (MalformedURLException e) {
            log.error("Error at loading loadAsResourse" + e.getMessage());
            throw new UserController.RequestException("Failed to load file", HttpStatus.BAD_REQUEST);
        }
    }

    public void InitUploadDirectories() throws UserController.RequestException {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
            throw new UserController.RequestException("pula", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Document> GetAllTemplates() {
        return repoDocs.GetTemplates();
    }
    
    public List<Document> GetAllDocsToSign(User user)
    {
        
        return null;
    }
}
