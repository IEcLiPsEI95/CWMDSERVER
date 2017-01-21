package ro.ubbcluj.cs.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.controller.DocumentController;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.domain.CWMDRequestResponse;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.repository.DocumentRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.logging.LogFactory;
import ro.ubbcluj.cs.session.SessionManager;
import ro.ubbcluj.cs.domain.User;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;

/**
 * Created by hzugr on 11/15/2016.
 */

@RequestMapping("docs")
@RestController
public class DocumentRestController
{
    private static final String TOKEN_HEADER = "Authorization";
    private static Logger log = LogManager.getLogger(DocumentRestController.class);
    
    @Autowired
    private SessionManager sm;
    
    @Autowired
    private DocumentController ctrlDocs;
    
    
    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public @ResponseBody String Test(@PathVariable int id)
    {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }
    
    

}

