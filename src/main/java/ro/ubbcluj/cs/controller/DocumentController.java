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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    public String GetNameForUpload(Document document, User user) {
        String newName = "";
        newName = newName + user.getFirstName() + "_" + user.getLastName() + "_";
        newName = newName + document.getBaseName() + "_";
        if (document.getStatus() == 1) {
            newName = newName + "DRAFT_0." + document.getVersionDraftMinor();
        } else if (document.getStatus() == 2) {
            newName = newName + "FINAL_1.0";
        }
        else {
            newName = newName + "FINAL_REVIZUIT_1." + document.getVersionFinRevMinor();
        }
        document.setBaseName(newName);

        return newName;
    }

    public String GetNameForDownload(String documentType, int documentStatus, User user) {
        return repoDocs.getNameForDownload(documentType, documentStatus, user.getUsername());
    }
    public void store(MultipartFile file, String name) {
        try {
            if (file.isEmpty()) {
                log.error("Uploaded file is empty");
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(name));
        } catch (IOException e) {
            log.error("Error at uploading file"+e.getMessage());
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) throws FileNotFoundException{
        try {
            Path file = load(this.rootLocation+"/"+filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                log.info("File successfully found");
                return resource;
            }
            else {
                log.error("Error at loading a document");
                throw new FileNotFoundException();

            }
        } catch (MalformedURLException e) {
            log.error("Error at loading loadAsResourse"+e.getMessage());
            throw new FileNotFoundException();
        }

    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
        }
    }

}
