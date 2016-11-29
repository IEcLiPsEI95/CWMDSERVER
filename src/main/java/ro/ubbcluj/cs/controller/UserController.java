package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.repository.UserRepository;

import java.util.List;

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
    
    public void AddUser(String username, String password, long permissions) throws RequestException
    {
        if (null == username || username.isEmpty())
        {
            throw new RequestException("Username was not provided.", HttpStatus.BAD_REQUEST);
        }
    
        if (null == password || password.isEmpty())
        {
            throw new RequestException("Password was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        try
        {
            User user = new User(username, password, permissions);
            repoUser.insert(user);
            log.info("User: " + username + " was added with success");
        }
        catch (Throwable e)
        {
            log.error("Failed to add user: " + username);
            log.error(e.getMessage());
            throw  new RequestException("Insert failed. WHY???", HttpStatus.BAD_REQUEST);
        }
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
    
    public void UpdateUser(String username, String password, long permissions) throws RequestException
    {
        if (null == username || username.isEmpty())
        {
            throw new RequestException("Username was not provided.", HttpStatus.BAD_REQUEST);
        }
    
        if (null == password || password.isEmpty())
        {
            throw new RequestException("Password was not provided.", HttpStatus.BAD_REQUEST);
        }
    
        try
        {
            User user = new User(username, password, permissions);
            repoUser.updateUser(username, user);
            log.info("User: " + username + " was updated with success");
        }
        catch (Throwable e)
        {
            log.error("Failed to update user: " + username);
            log.error(e.getMessage());
            throw  new RequestException("Update failed. WHY???", HttpStatus.BAD_REQUEST);
        }
    }
    
    public List<User> GetAllUsers()
    {
//        repoUser.getAllUsers();
        return null;
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