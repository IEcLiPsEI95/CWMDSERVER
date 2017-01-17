package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.repository.DocumentRepository;

/**
 * Created by hlupean on 17-Nov-16.
 */

@Component
public class DocumentController
{
    @Autowired
    private DocumentRepository repoDocs;
    
    private static Logger log = LogManager.getLogger(DocumentController.class);
    
    private DocumentController()
    {
        log.info("DocumentController");
    }
    
    
    public String GetSignatures(User user)
    {
        return repoDocs.getSignatures(user);
    }
}
