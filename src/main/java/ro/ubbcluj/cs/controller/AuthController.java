package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.repository.DocumentRepository;

/**
 * Created by hlupean on 16-Nov-16.
 */
@RestController
public class AuthController
{
    public static final String API_DOCS = "/api/auth";
    private static Logger LOG = LogManager.getLogger("file");
    
    @Autowired
    private DocumentRepository noteRepository;
    
    private SessionManager sm = SessionManager.getInstance();
    
//    @RequestMapping(method = RequestMethod.POST)
//    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input)
//    {
//        this.validateUser(userId);
//    }
    

//    Access-Control-Allow-Origin: http://api.bob.com
//    Access-Control-Allow-Credentials: true
//    Access-Control-Expose-Headers: FooBar
//    Content-Type: text/html; charset=utf-8;
    
    
    @CrossOrigin(origins = "http://170.30.117.3:8080")
    @RequestMapping(value = API_DOCS + "/login", method = RequestMethod.POST)
    public String getById(@RequestBody String input)
    {
        System.out.println("aa");
        System.out.println(input);
        return input;
    }
}
