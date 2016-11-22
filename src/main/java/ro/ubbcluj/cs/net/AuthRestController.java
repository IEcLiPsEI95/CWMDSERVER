package ro.ubbcluj.cs.net;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
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
    private static ObjectMapper mapper = new ObjectMapper();
    
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
            return new ResponseEntity<>("LOGIN ERROR", HttpStatus.NOT_FOUND);
        }
        log.info(String.format("User [%1s] logged-in with success. Token received: [%2s]", username, token));
        
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "getuser", method = RequestMethod.POST)
    public ResponseEntity<?> GetUser(
            @RequestParam(value="token", required=true) String token,
            @RequestParam(value="username", required=true) String usernameToGet
    )
    {
        log.info(String.format("Trying to get: username=[%1s]", usernameToGet));
        
        User user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_GET_USER);
        if (null == user)
        {
            log.error(String.format("User with token [%1s] does not have enough permissions to get user [%2s]", token, usernameToGet));
            return new ResponseEntity<>("Failed to get user", HttpStatus.NOT_FOUND);
        }
        
        try
        {
            User userResp = ctrlUser.GetUserByUsername(usernameToGet);
            if (null == userResp)
            {
                log.error(String.format("Username [%1s] not found", usernameToGet));
                return new ResponseEntity<>("Failed to get user", HttpStatus.NOT_FOUND);
            }
            
            String response = mapper.writeValueAsString(userResp);
            log.info(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } 
        catch (JsonProcessingException e)
        {
            log.error(String.format("Failed to convert user [%1s] to json", usernameToGet));
            return new ResponseEntity<>("Failed to convert the user to json", HttpStatus.NOT_FOUND);
        }
    }
    

    @RequestMapping(value = "adduser", method = RequestMethod.POST)
    public ResponseEntity<?> AddUser(
            @RequestParam(value="token", required=true) String token,
            @RequestParam(value="username", required=true) String usernameToAdd,
            @RequestParam(value="password", required=true) String password,
            @RequestParam(value="permissions", required=true) long permissions
    )
    {
        log.info(String.format("Trying to add: username=[%1s]", usernameToAdd));

        User user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_ADD_USER);
        if (null == user)
        {
            log.error(String.format("User with token [%1s] does not have enough permissions to add user [%2s]", token, usernameToAdd));
            return new ResponseEntity<>("Failed to add user", HttpStatus.NOT_FOUND);
        }
        
        if (!ctrlUser.AddUser(usernameToAdd, password, permissions))
        {
            log.error("Failed to add user: " + usernameToAdd);
            return new ResponseEntity<>("Failed to add user", HttpStatus.NOT_FOUND); 
        }
        
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "deluser", method = RequestMethod.POST)
    public ResponseEntity<?> DeleteUser(
            @RequestParam(value="token", required=true) String token,
            @RequestParam(value="username", required=true) String usernameToDelete
    )
    {
        log.info(String.format("Trying to get: username=[%1s]", usernameToDelete));
        
        User user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_DELETE_USER);
        if (null == user)
        {
            log.error(String.format("User with token [%1s] does not have enough permissions to delete user [%2s]", token, usernameToDelete));
            return new ResponseEntity<>("Failed to delete user", HttpStatus.NOT_FOUND);
        }
    
        if (!ctrlUser.DeleteUser(usernameToDelete))
        {
            log.error(String.format("Failed to delete user [%1s] ", usernameToDelete));
            return new ResponseEntity<>("Failed to delete user", HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
