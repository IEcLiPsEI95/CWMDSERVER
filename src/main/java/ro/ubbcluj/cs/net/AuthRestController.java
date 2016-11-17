package ro.ubbcluj.cs.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.repository.DocumentRepository;
import ro.ubbcluj.cs.session.SessionManager;

/**
 * Created by hlupean on 16-Nov-16.
 */
@RestController
public class AuthRestController
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
    
    
    @RequestMapping(value = API_DOCS + "/login", method = RequestMethod.POST)
    public String Login(@RequestBody String input)
    {
        // username luat din pachet
        String token = sm.Login("username", "pass");
        if (null == token)
        {
            LOG.error("Failed to login user: " + "username");
            return null; // eroare
        }
        
        return "bau";
    }
}
