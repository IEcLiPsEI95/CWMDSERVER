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
    private static long lastUpdateTime = 0;
    
    @Autowired
    private UserRepository repoUser;
    
    public UserController()
    {
        log.info("UserController()");
    }
    
    public long getLastUpdateTime()
    {
        return lastUpdateTime;
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
        catch (Exception ex)
        {
            throw new RequestException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
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
    
    public void AddUser(String username, String password, long permissions, String lastname, String firstname, String cnp, String phone, String groupName) throws RequestException
    {
        if (null == username || username.isEmpty())
        {
            throw new RequestException("Username was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == password || password.isEmpty())
        {
            throw new RequestException("Password was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == lastname || lastname.isEmpty())
        {
            throw new RequestException("Lastname was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == firstname || firstname.isEmpty())
        {
            throw new RequestException("Firstname was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == cnp || cnp.isEmpty())
        {
            throw new RequestException("Cnp was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (cnp.length() != 13)
        {
            throw new RequestException("Cnp invalid.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == phone || phone.isEmpty())
        {
            throw new RequestException("Phone was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (phone.length() != 10)
        {
            throw new RequestException("Phone invalid.", HttpStatus.BAD_REQUEST);
        }
        
        try
        {
            int groupId = repoUser.GetGroupIdByName(groupName);
            User user = new User(username, password, permissions, lastname, firstname, cnp, phone, groupId);
            repoUser.insert(user);
            log.info("User: " + username + " was added with success");
            lastUpdateTime = System.currentTimeMillis();
        }
        catch (Throwable e)
        {
            log.error("Failed to add user: " + username);
            log.error(e.getMessage());
            throw new RequestException("Insert failed.", HttpStatus.BAD_REQUEST);
        }
    }
    
    public boolean DeleteUser(String username)
    {
        try
        {
            repoUser.delete(username);
            lastUpdateTime = System.currentTimeMillis();
        }
        catch (Throwable ex)
        {
            log.error("Failed to delete user: " + username);
            log.error(ex.toString());
            return false;
        }
        
        return true;
    }
    
    public void UpdateUser(String username, String password, long permissions, String lastName, String firstName, String cnp, String phone, String groupName, User oldUser) throws RequestException
    {
        String passToAdd;
        if (null == username || username.isEmpty())
        {
            throw new RequestException("Username was not provided.", HttpStatus.BAD_REQUEST);
        }

        if (null == password || password.isEmpty())
        {
            passToAdd = oldUser.getPassword();
        }
        else
        {
            passToAdd = password;
        }
        
        if (null == lastName || lastName.isEmpty())
        {
            throw new RequestException("Last Name was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == firstName || firstName.isEmpty())
        {
            throw new RequestException("First Name was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == cnp || cnp.isEmpty())
        {
            throw new RequestException("Cnp was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (cnp.length() != 13)
        {
            throw new RequestException("Cnp invalid.", HttpStatus.BAD_REQUEST);
        }
        
        if (null == phone || phone.isEmpty())
        {
            throw new RequestException("Phone was not provided.", HttpStatus.BAD_REQUEST);
        }
        
        if (phone.length() != 10)
        {
            throw new RequestException("Phone invalid.", HttpStatus.BAD_REQUEST);
        }
        
        try
        {
            int groupId = repoUser.GetGroupIdByName(groupName);
            User user = new User(username, passToAdd, permissions, lastName, firstName, cnp, phone, groupId);
            repoUser.updateUser(username, user);
            log.info("User: " + username + " was updated with success");
            lastUpdateTime = System.currentTimeMillis();
        }
        catch (UserRepository.UsernameIsNull usernameIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(usernameIsNull.getMessage());
            throw new RequestException("Update failed. UsernameIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.UsersUsernameIsNull usersUsernameIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(usersUsernameIsNull.getMessage());
            throw new RequestException("Update failed. UsersUsernameIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.UserNotFound userNotFound)
        {
            log.error("Failed to update user: " + username);
            log.error(userNotFound.getMessage());
            throw new RequestException("Update failed. UserNotFound", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.PasswordIsNull passwordIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(passwordIsNull.getMessage());
            throw new RequestException("Update failed. PasswordIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.PhoneIsNull phoneIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(phoneIsNull.getMessage());
            throw new RequestException("Update failed. PhoneIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.LastnameIsNull lastnameIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(lastnameIsNull.getMessage());
            throw new RequestException("Update failed. LastnameIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.FirstnameIsNull firstnameIsNull)
        {
            log.error("Failed to update user: " + username);
            log.error(firstnameIsNull.getMessage());
            throw new RequestException("Update failed. FirstnameIsNull", HttpStatus.BAD_REQUEST);
        }
        catch (UserRepository.CnpIsNullOrInvalid cnpIsNullOrInvalid)
        {
            log.error("Failed to update user: " + username);
            log.error(cnpIsNullOrInvalid.getMessage());
            throw new RequestException("Update failed. CnpIsNullOrInvalid", HttpStatus.BAD_REQUEST);
        }
    }
    
    public List<User> GetAllUsers() throws RequestException
    {
        try
        {
            List<User> list = repoUser.getAllUsers();
            if (null == list)
            {
                log.error("There are no users in DataBase.");
                throw new RequestException("There are no users.", HttpStatus.NOT_FOUND);
            }
            
            return list;
        }
        catch (Exception ex)
        {
            throw new RequestException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public int GetGroupId(String groupName) throws RequestException
    {
        try
        {
            int id = repoUser.GetGroupIdByName(groupName);
            log.info("Found id " + id + " for group: " + groupName);
            return id;
        }
        catch (Exception ex)
        {
            throw new RequestException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public String getType(long permissions) {

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
