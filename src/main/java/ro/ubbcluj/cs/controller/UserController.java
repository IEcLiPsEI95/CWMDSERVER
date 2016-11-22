package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static UserController instance = null;
    private static Logger log = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserRepository repoUser;
//    
//    
//    public static UserController getInstance()
//    {
//        if (instance == null)
//        {
//            synchronized (UserController.class)
//            {
//                if (instance == null)
//                {
//                    instance = new UserController();
//                }
//            }
//        }
//        return instance;
//    }
    
    public UserController()
    {
        log.info("UserController()");
    }
    
    public User GetUserByUsernameAndPassword(String username, String password)
    {
        try
        {
            return repoUser.getUserByUsernameAndPassword(username, password);
        } 
        catch (UserRepository.PasswordIsNull passwordIsNull)
        {
            return null;
        } 
        catch (UserRepository.UsernameIsNull usernameIsNull)
        {
            return null;
        }
    }
    
    public User GetUserByUsername(String username)
    {
        try
        {
            return repoUser.getUserByUsername(username);
        } 
        catch (UserRepository.UsersUsernameIsNull ignore)
        {
            return null;
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
}
