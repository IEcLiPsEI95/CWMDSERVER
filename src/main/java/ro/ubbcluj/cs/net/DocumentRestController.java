package ro.ubbcluj.cs.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import ro.ubbcluj.cs.controller.DocumentController;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.domain.CWMDRequestResponse;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.UserPerm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ro.ubbcluj.cs.session.SessionManager;
import ro.ubbcluj.cs.domain.User;

import java.io.FileNotFoundException;
import java.util.List;


/**
 * Created by hzugr on 11/15/2016.
 */

@RequestMapping("docs")
@RestController
public class DocumentRestController {
    private static final String TOKEN_HEADER = "Authorization";
    private static Logger log = LogManager.getLogger(DocumentRestController.class);

    @Autowired
    private SessionManager sm;

    @Autowired
    private DocumentController ctrlDocs;


    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public String Test(@PathVariable int id) {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public ResponseEntity<?> Sign(
            @RequestHeader(value = TOKEN_HEADER) String token,
            @RequestBody int documentId
    ) {
        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_MANAGER);
            ctrlDocs.Sign(documentId, user);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }

    @RequestMapping(value = "reject", method = RequestMethod.POST)
    public ResponseEntity<?> Reject(
            @RequestHeader(value = TOKEN_HEADER) String token,
            @RequestBody int documentId
    ) {
        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_MANAGER);
            ctrlDocs.Reject(documentId, user);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity<?> DownloadFile(
            @RequestHeader(value = TOKEN_HEADER) String token,
            @RequestParam("type") int docType,
            @PathVariable String param // ceva variable de care o sa avem nevoie
    ) {
        log.info("Trying to download a file");

        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_BASIC_USER);
            String fileName;

            if (user.getPermissions() == UserPerm.PERM_MANAGER) {
                fileName = ctrlDocs.GetNameForDownload(docType, 3, user);
            } else {
                fileName = ctrlDocs.GetNameForDownload(docType, 1, user);
            }

            Resource file = ctrlDocs.LoadFileAsResource(fileName);

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<?> UploadFile(
            @RequestHeader(value = TOKEN_HEADER) String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") int docType
    ) {
        log.info("Trying to upload a file");

        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_BASIC_USER);
            String fileName = ctrlDocs.GetNameForUpload(docType, user);

            ctrlDocs.StoreFile(file, fileName);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }

    @RequestMapping(value = "gettemplates", method = RequestMethod.GET)
    public ResponseEntity<?> GetAllTemplates(
            @RequestHeader(value = TOKEN_HEADER) String token
    ) {
        log.info("Trying to return all the templates");

        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_BASIC_USER);

            List<Document> docs = ctrlDocs.GetAllTemplates();
            return CWMDRequestResponse.createResponse(docs, HttpStatus.OK);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    @RequestMapping(value = "getdocstosign", method = RequestMethod.GET)
    public ResponseEntity<?> GetAllDocsToSign(
            @RequestHeader(value = TOKEN_HEADER) String token
    ) {
        log.info("Trying to return all the templates");
        
        try {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_MANAGER);
            
            List<Document> docs = ctrlDocs.GetAllDocsToSign(user);
            return CWMDRequestResponse.createResponse(docs, HttpStatus.OK);
        } catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }

}

