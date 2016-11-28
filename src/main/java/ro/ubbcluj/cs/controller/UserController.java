package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.repository.UserRepository;

/**
 * Created by hlupean on 21-Nov-16.
 */

@Component
public class UserController
{
    private static Logger log = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserRepository repoUser;

    public UserController()
    {
        log.info("UserController()");
    }
    
    public User GetUserByUsernameAndPassword(String username, String password) throws RequestException
    {
        try
        {
            User resp = repoUser.getUserByUsernameAndPassword(username, password);
            if (null == resp)
            {
                throw new RequestException("Invalid username or password", HttpStatus.NOT_FOUND);
            }
            
            return resp;
        } 
        catch (UserRepository.PasswordIsNull ignored)
        {
            throw new RequestException("Password is empty", HttpStatus.BAD_REQUEST);
        } 
        catch (UserRepository.UsernameIsNull ignored)
        {
            throw new RequestException("Username is empty", HttpStatus.BAD_REQUEST);
        }
    }
    
    public User GetUserByUsername(String username) throws RequestException
    {
        try
        {
            return repoUser.getUserByUsername(username);
        } 
        catch (UserRepository.UsersUsernameIsNull ignore)
        {
            throw new RequestException("Username is empty", HttpStatus.BAD_REQUEST);
        }
    }
    
    public boolean AddUser(String username, String password, long permissions)
    {
        if (null == username || null == password)
        {
            log.error("(null == username || null == password)");
            return false;
        }
        
        User user = new User(username, password, permissions);
    
        try
        {
            repoUser.insert(user);
        } 
        catch (Throwable plm)
        {
            log.error("Failed to insert user: " + username);
            log.error(plm.toString());
            return false;
        }
        
        return  true;
    }
    
    public boolean DeleteUser(String username)
    {
        try
        {
            repoUser.delete(username);
        } 
        catch (Throwable ex)
        {
            log.error("Failed to delete user: " + username);
            log.error(ex.toString());
            return false;
        }
        
        return true;
    }
    
    public boolean UpdateUser(String username, String password, long permissions)
    {
        User user = new User(username, password, permissions);
        
        try
        {
            repoUser.updateUser(username, user);
        }
        catch (Throwable ex)
        {
            log.error("updateUser(" + username + ") failed");
            return false;
        }
        
        return true;
    }
    
    public static class RequestException extends Throwable
    {
        private HttpStatus status;
    
        public RequestException(String message, HttpStatus status)
        {
            super(message);
            this.status = status;
        }
    
        public HttpStatus getStatus()
        {
            return status;
        }
    }
}
