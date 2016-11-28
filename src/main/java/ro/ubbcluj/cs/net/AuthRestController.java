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
import ro.ubbcluj.cs.domain.CWMDRequestResponse;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.session.SessionManager;

/**
 * Created by hlupean on 16-Nov-16.
 */

@RequestMapping("auth")
@RestController
public class AuthRestController
{
    private static Logger log = LogManager.getLogger(AuthRestController.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static final String TOKEN_HEADER = "Authorization";
    
    
    @Autowired
    private SessionManager sm;
    
    @Autowired
    private UserController ctrlUser;
//    
//    @Autowired
//    private UserRepository repoUser;
//    
    AuthRestController()
    {
        log.info("AuthRestController()");
    }
    
    
    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public
    @ResponseBody
    String Test(@PathVariable int id)
    {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<?> Login(
            @RequestBody User reqUser
    )
    {
        String username = reqUser.getUsername();
        String password = reqUser.getPassword();
        User user;
        
        log.info(String.format("Trying to login: username=[%1s], pass=[%2s]", username, password)); // parola plain text in loguri... PERFECT!!!!
        try
        {
            user = sm.Login(username, password);
        } 
        catch (UserController.RequestException e)
        {
            log.error("Failed to login user: " + username);
            return CWMDRequestResponse.createResponse(null, e.getMessage(), 0, e.getStatus());
        }
        
        log.info(String.format("User [%1s] logged-in with success. Token received: [%2s]", username, user.getPermissions()));
        return CWMDRequestResponse.createResponse(user.getToken(), "Success", user.getPermissions(), HttpStatus.OK);
    }
 
    @RequestMapping(value = "getuser", method = RequestMethod.POST)
    public ResponseEntity<?> GetUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
            
    )
    {
        String usernameToGet = reqUser.getUsername();
        log.info(String.format("Trying to get: username=[%1s]", usernameToGet));
    
        try
        {
            User user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_GET_USER);
            log.info(String.format("User [%1s] is trying to get user [%1s}", user.getUsername(), usernameToGet));
            
            User userResp = ctrlUser.GetUserByUsername(usernameToGet);
            log.info(String.format("Found user [%1s]", userResp.getUsername()));
    
            String response = mapper.writeValueAsString(userResp);
            log.info(response);
            return CWMDRequestResponse.createResponse(response, HttpStatus.OK);
        } 
        catch (UserController.RequestException e)
        {
//            log.error(String.format("User with token [%1s] does not have enough permissions to get user [%2s]", token, usernameToGet));
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        } catch (JsonProcessingException e)
        {
            log.error(String.format("Failed to convert user [%1s] to json", usernameToGet));
            return CWMDRequestResponse.createResponse(String.format("Failed to convert user [%1s] to json", usernameToGet), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @RequestMapping(value = "adduser", method = RequestMethod.POST)
    public ResponseEntity<?> AddUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
    )
    {
        String usernameToAdd = reqUser.getUsername();
        String password = reqUser.getPassword();
        long permissions = reqUser.getPermissions();
        
        log.info(String.format("Trying to add: username=[%1s]", usernameToAdd));
    
        User user = null;
        try
        {
            user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_ADD_USER);
        } catch (UserController.RequestException e)
        {
            e.printStackTrace();
        }
        
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
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "username", required = true) String usernameToDelete
    )
    {
        log.info(String.format("Trying to get: username=[%1s]", usernameToDelete));
    
        User user = null;
        try
        {
            user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_DELETE_USER);
        } catch (UserController.RequestException e)
        {
            e.printStackTrace();
        }
        
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
    
    
    @RequestMapping(value = "updateuser", method = RequestMethod.POST)
    public ResponseEntity<?> UpdateUser(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "username", required = true) String usernameToUpdate,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "permissions", required = true) long permissions
    )
    {
        log.info(String.format("Trying to update: username=[%1s]", usernameToUpdate));
    
        User user = null;
        try
        {
            user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_ADD_USER);
        } catch (UserController.RequestException e)
        {
            e.printStackTrace();
        }
        
        if (null == user)
        {
            log.error(String.format("User with token [%1s] does not have enough permissions to add user [%2s]", token, usernameToUpdate));
            return new ResponseEntity<>("Failed to add user", HttpStatus.NOT_FOUND);
        }
        
        if (!ctrlUser.UpdateUser(usernameToUpdate, password, permissions))
        {
            log.error("Failed to update user: " + usernameToUpdate);
            return new ResponseEntity<>("Failed to update user", HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    
}
