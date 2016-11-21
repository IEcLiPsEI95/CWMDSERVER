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
}
