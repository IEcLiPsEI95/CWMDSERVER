package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.repository.DocumentRepository;

/**
 * Created by hlupean on 17-Nov-16.
 */

@Component
public class DocumentController {
    @Autowired
    private DocumentRepository repoDocs;

    private static Logger log = LogManager.getLogger(DocumentController.class);

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
}
