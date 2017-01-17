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

import java.util.List;

/**
 * Created by hlupean on 16-Nov-16.
 */

@RequestMapping("auth")
@RestController
public class AuthRestController
{
    private static Logger log = LogManager.getLogger(AuthRestController.class);
//    private static ObjectMapper mapper = new ObjectMapper();
    private static final String TOKEN_HEADER = "Authorization";
    
    @Autowired
    private SessionManager sm;
    
    @Autowired
    private UserController ctrlUser;
    
    AuthRestController()
    {
        log.info("AuthRestController");
    }
    
    
    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public @ResponseBody String Test(@PathVariable int id)
    {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }
    
    // login
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<?> Login(
            @RequestBody User reqUser
    )
    {
        String username = reqUser.getUsername();
        String password = reqUser.getPassword();
        
        log.info(String.format("Trying to login: username=[%1s], pass=[%2s]", username, password)); // parola plain text in loguri... PERFECT!!!!
        try
        {
            User user = sm.Login(username, password);
            log.info(String.format("User [%1s] logged-in with success. User permissions: [%2s]", username, user.getPermissions()));
            return CWMDRequestResponse.createResponse(user.getToken(), user, user.getPermissions(), HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error("Failed to login user: " + username);
            return CWMDRequestResponse.createResponse(null, e.getMessage(), 0, e.getStatus());
        }
    }
    
    // force logunt (un admin ii da logunt altui utilizator)
    @RequestMapping(value = "flogout", method = RequestMethod.POST)
    public ResponseEntity<?> ForceLogout(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
    )
    {
        String usernameToLogout = reqUser.getUsername();
        log.info(String.format("Trying to logout username [%1s]", usernameToLogout));
        
        try
        {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_LOGOUT_USER);
            log.error(String.format("User [%1s] has enough permissions to logout user [%2s]", user.getUsername(), usernameToLogout));
            
            sm.Logout(usernameToLogout);
            log.info(String.format("User [%1s] was logged-out with success by user [%2s]", usernameToLogout, user.getUsername()));
            
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    // logout (userul isi da lui logout)
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<?> Logout(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token
    )
    {
        try
        {
            User user = sm.GetLoggedInUser(token, 0);
            log.info(String.format("User [%1s] will be logged-out.", user.getUsername()));
            
            sm.Logout(user.getUsername());
            log.info(String.format("User [%1s] was logged-out with success by user [%2s]", user.getUsername(), user.getUsername()));
            
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    // get user by username
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
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_GET_USER);
            log.info(String.format("User [%1s] is trying to get user [%1s}", user.getUsername(), usernameToGet));
            
            User userResp = ctrlUser.GetUserByUsername(usernameToGet);
            log.info(String.format("Found user [%1s]", userResp.getUsername()));
            
            return CWMDRequestResponse.createResponse(userResp, HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(String.format("User with token [%1s] does not have enough permissions to get user [%2s]", token, usernameToGet));
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    // adaugre user
    @RequestMapping(value = "adduser", method = RequestMethod.POST)
    public ResponseEntity<?> AddUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
    )
    {
        String usernameToAdd    = reqUser.getUsername();
        String passwordToAdd    = reqUser.getPassword();
        long permissionsToAdd   = reqUser.getPermissions();
        String lastName         = reqUser.getLastName();
        String firstName        = reqUser.getFirstName();
        String cnp              = reqUser.getCnp();
        String phone            = reqUser.getPhone();
        
        log.info(String.format("Trying to add: username=[%1s]", usernameToAdd));
        try
        {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_ADD_USER);
            log.info(String.format("User [%1s] has enough permissions to add user [%2s]", user.getUsername(), usernameToAdd));
            
            ctrlUser.AddUser(usernameToAdd, passwordToAdd, permissionsToAdd, lastName, firstName, cnp, phone);
            log.info(String.format("User [%1s] was created with success by user [%2s]", usernameToAdd, user.getUsername()));
            
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    // delete user by username
    @RequestMapping(value = "deluser", method = RequestMethod.POST)
    public ResponseEntity<?> DeleteUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
    )
    {
        String usernameToDelete = reqUser.getUsername(); 
        log.info(String.format("Trying to delete username [%1s]", usernameToDelete));
        
        try
        {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_DELETE_USER | UserPerm.PERM_LOGOUT_USER);
            log.info(String.format("User [%1s] has enough permissions to delete user [%2s]", user.getUsername(), usernameToDelete));
    
            ctrlUser.DeleteUser(usernameToDelete);
            log.info(String.format("User [%1s] was deleted with success by user [%2s]", usernameToDelete, user.getUsername()));
            
            sm.RemoveUser(usernameToDelete);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    @RequestMapping(value = "updateuser", method = RequestMethod.POST)
    public ResponseEntity<?> UpdateUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token,
            @RequestBody User reqUser
    )
    {
        log.info(String.format("Trying to update: username=[%1s]", reqUser.getUsername()));
        String usernameToUpdate     = reqUser.getUsername();
        String password             = reqUser.getPassword();
        String lastName             = reqUser.getLastName();
        String firstName            = reqUser.getFirstName();
        String cnp                  = reqUser.getCnp();
        String phone                = reqUser.getPhone();
        long permissions            = reqUser.getPermissions();

        try
        {
            User user = sm.GetLoggedInUser(token, (int) UserPerm.PERM_UPDATE_USER);
            log.info(String.format("User [%1s] has enough permissions to update user [%2s]", user.getUsername(), usernameToUpdate));
    
            ctrlUser.UpdateUser(usernameToUpdate, password, permissions, lastName, firstName, cnp, phone);
            sm.UpdateInfo(new User(usernameToUpdate, password, permissions, lastName, firstName, cnp, phone));
            log.info(String.format("User [%1s] was updated with success", usernameToUpdate));
            
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
    
    
    @RequestMapping(value = "getallusers", method = RequestMethod.GET)
    public ResponseEntity<?> GetAllUser(
            @RequestHeader(value = TOKEN_HEADER, required = true) String token
    )
    {
        log.info("Trying to get all users");
        
        try
        {
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_GET_USER);
            log.info(String.format("User [%1s] is trying to get all users", user.getUsername()));
            
            List<User> userResp = ctrlUser.GetAllUsers();
            log.info(String.format("Found [%1d] users", userResp.size()));

            return CWMDRequestResponse.createResponse(userResp, HttpStatus.OK);
        }
        catch (UserController.RequestException e)
        {
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
    }
}
