package ro.ubbcluj.cs.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.repository.UserRepository;
import ro.ubbcluj.cs.session.SessionManager;

import javax.jws.soap.SOAPBinding;

/**
 * Created by hlupean on 16-Nov-16.
 */

@RequestMapping("auth")
@RestController
public class AuthRestController
{
    private static Logger log = LogManager.getLogger(AuthRestController.class);
    
    @Autowired
    private SessionManager sm;//= SessionManager.getInstance();
    
    @Autowired
    private UserController ctrlUser; //= UserController.getInstance();
    
    @Autowired
    private UserRepository repoUser;
    
    AuthRestController()
    {
        log.info("AuthRestController()");
    }
    
    
    
    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public @ResponseBody String Test(@PathVariable int id)
    {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }
    
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<?> Login(
            @RequestParam(value="username", required=true) String username,
            @RequestParam(value="password", required=true) String password
    )
    {
        log.info(String.format("Trying to login: username=[%1s], pass=[%2s]", username, password)); // parola plain text in loguri... PERFECT!!!!
        String token = sm.Login(username, password);
        if (null == token)
        {
            log.error("Failed to login user: " + username);
            return new ResponseEntity<>("BOULE", HttpStatus.OK);
        }
        log.info(String.format("User [%1s] logged-in with success. Token received: [%2s]", username, token));
    
    
    
        return new ResponseEntity<>(token, HttpStatus.NOT_FOUND);
    }
}
