package ro.ubbcluj.cs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.ubbcluj.cs.repository.DocumentRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * Created by hzugr on 11/15/2016.
 */
@RestController
public class DocumentController {

    public static final String API_DOCS = "/api/docs";
    private static Logger LOG = LogManager.getLogger("file");

    @Autowired
    private DocumentRepository noteRepository;
    
    
    @RequestMapping(value = API_DOCS, method = RequestMethod.GET)
    public String getAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOG.info("getAll... by " + user.getUsername());
        return "Success!";
    }
}
