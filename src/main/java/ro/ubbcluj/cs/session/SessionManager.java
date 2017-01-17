package ro.ubbcluj.cs.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.domain.User;

import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.repository.UserRepository;
import sun.awt.Mutex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

@Component
@Configurable
public class SessionManager
{
    
    private static Logger log = LogManager.getLogger(SessionManager.class);
    
    private final String tokenPrefix = "Bearer ";
    private Mutex                   listMutex;
    private HashMap<String, User>   dictUsers;
    private SecureRandom            random;
    private byte                    secret1;
    private byte                    secret2;
    private char                    padding;
    private CleanupThread           hThCleanup;
    private final long              maxMils     = 60 * 60 * 1000;
    
    @Autowired
    private UserController ctrlUser;

    public SessionManager()
    {
        this.dictUsers  = new HashMap<>();
        this.random     = new SecureRandom();
        
        this.listMutex  = new Mutex();
        
        this.secret1    = (byte) (random.nextInt(20) + 5);
        this.secret2    = (byte) (random.nextInt(50) + 5);
        this.padding    = (char) (random.nextInt(26) + 'A');
        
        log.info("secret1: " + this.secret1);
        log.info("secret2: " + this.secret2);
        log.info("padding: " + this.padding);
        
        this.hThCleanup = new CleanupThread(listMutex, dictUsers, maxMils);
        this.hThCleanup.start();
    }
    
    public User Login(String username, String password) throws UserController.RequestException
    {
        try
        {
            User user = ctrlUser.GetUserByUsernameAndPassword(username, password);
            
            String token = this.AddUser(user);
            if (null == token)
            {
                log.error("Login :: Failed to add " + username + " in memory");
                return null;
            }
            
            log.info("User " + username + " is now logged in");
            return user;
        }
        catch (Exception ex)
        {
            log.error("Login :: bag pula in java");
            log.error(ex.toString());
            return null;
        }
    }
    
    
    public void Logout(String username) throws UserController.RequestException
    {
        if (null == username || username.isEmpty())
        {
            throw new UserController.RequestException("Username was not provided", HttpStatus.BAD_REQUEST);
        }
        
        if (!RemoveUser(username))
        {
            throw new UserController.RequestException("Username " + username + " is not logged-in", HttpStatus.NOT_FOUND);
        }
    }
    
    public boolean RemoveUser(String username)
    {
        if (null == username || username.isEmpty())
        {
            return false;
        }
        
        listMutex.lock();
        
        User user = dictUsers.get(username);
        if (null == user)
        {
            log.warn(String.format("Username [%s] is not logged-in", username));
            listMutex.unlock();
            return false;
        }
        
        user.setToken(null);
        user.setLoggedInTime(0);
        dictUsers.remove(username);
        
        listMutex.unlock();
        
        log.info(String.format("Username [%s] was loggedout", username));
        return true;
    }
    
    // verifica tokenul daca este logat si daca are permisiunile necesare
    public User GetLoggedInUser(String token, long requiredPermissions) throws UserController.RequestException
    {
        if (token.startsWith(tokenPrefix))
        {
            token = token.substring(tokenPrefix.length(), token.length());
        }
        log.info("Trying to get username from token: " + token);
        
        User user;
        boolean result;
        String username;
        
        username = GetUsernameFromToken(token);
        if (null == username)
        {
            log.error("Could not get username from token [" + token + "]");
            throw new UserController.RequestException("User is not logged-in (Invalid token)", HttpStatus.BAD_REQUEST);
        }
        
        listMutex.lock();
        user = dictUsers.get(username);
        listMutex.unlock();
        
        if ((null == user) || !(user.getToken().equals(token)))
        {
            log.error(String.format("Got username [%1s] from token [%2s], but there is no such user with this token ", username, token));
            throw new UserController.RequestException("User is not logged-in (session may be expired)", HttpStatus.NOT_FOUND);
        }
        
        if ((System.currentTimeMillis() - user.getLoggedInTime()) >= (maxMils - 1))
        {
            log.error("Token expired for user: " + user.getUsername());
            RemoveUser(user.getUsername());
            throw new UserController.RequestException("Session expired", HttpStatus.BAD_REQUEST);
        }
        
        log.info("Found " + user.getUsername() + " for token " + token);
        log.info("User " + user.getUsername() + " has " + user.getPermissions() + " rights. Required rights: " + requiredPermissions);
        
        result = (requiredPermissions == (user.getPermissions() & requiredPermissions));
        log.info("User " + user.getUsername() + " has " + (result ? "ENOUGH" : "INSUFFICIENT") + " rights");
    
        if (!result)
        {
            throw new UserController.RequestException("ACCESS DENIED. Not enough rights.", HttpStatus.NOT_ACCEPTABLE);
        }
        
        user.setLoggedInTime(System.currentTimeMillis());
        return user;
    }
    
    public void UpdateInfo(User newUser)
    {
        if (null == newUser) return;
        
        listMutex.lock();
        
        User existingUser = dictUsers.get(newUser.getUsername());
        if (null == existingUser)
        {
            listMutex.unlock();
            log.warn(String.format("Username [%s] is not logged-in", newUser.getUsername()));
            return;
        }
        
//        existingUser.setPassword(newUser.getPassword());
        existingUser.setPermissions(newUser.getPermissions());
        listMutex.unlock();
        
        log.info(String.format("Username [%s] was updated with success", newUser.getUsername()));
    }
    
    
    //
    // PRIVATE FUNCTIONS
    //
    private String AddUser(User user) throws UserController.RequestException
    {
        listMutex.lock();
        
        if (dictUsers.containsKey(user.getUsername()))
        {
            log.error("User " + user.getUsername() + " is already logged in.");
            listMutex.unlock();
            throw new UserController.RequestException("User " + user.getUsername() + " is already logged in.", HttpStatus.CONFLICT);
        }
        
        user.setLoggedInTime(System.currentTimeMillis());
        String token = CreateToken(user);
        user.setToken(token);
        
        dictUsers.put(user.getUsername(), user);
        
        listMutex.unlock();
        
        log.info("Token [" + token + "] was created for user " + user.getUsername());
        return token;
    }
    
    private String CreateToken(User user)
    {
        String token;
        String aux;
        char[] auxToken = new char[100];
        int len     = user.getUsername().length() * secret2 + secret1;
        int digits  = (int) (Math.log10(len)+1) ;
        
        for (int i = 0; i < 5 - digits; i++)
        {
            auxToken[i] =  padding;
        }
        
        aux = Integer.toString(len);
        for (int i = 5 - digits; i < 5; i++)
        {
            auxToken[i] = aux.charAt(i - (5 - digits));
        }
        
        for (int i = 0; i < user.getUsername().length(); i++)
        {
            auxToken[5 + i] = (char) (user.getUsername().charAt(i) ^ secret1);
        }
        
        StringBuilder sb = new StringBuilder();
        for (char c : auxToken)
        {
            if (0 == c) break;
            sb.append(c);
        }
        
        token = sb.toString();
        token = token + (new BigInteger(130, random)).toString(32) + (new BigInteger(130, random)).toString(32) + (new BigInteger(130, random)).toString(32);
        
        System.out.println("TOKEEEEEEEEEEEN: " + token);
        return token;
    }
    
    private String GetUsernameFromToken(String token)
    {
        if (null == token || token.isEmpty() || token.length() < 10)
        {
            log.error("token is null / empty / too short");
            return null;
        }
        
        byte startIndex;
        //noinspection StatementWithEmptyBody
        for (startIndex = 0; token.charAt(startIndex) == padding && startIndex < 5; startIndex++);
        int len;
        try
        {
            len = Integer.parseInt(token.substring(startIndex, 5));    
        }
        catch (NumberFormatException ignored)
        {
            log.error("invalid format for token");
            return null;
        }
        
        len = (len - secret1) / secret2;
        
        if (token.length() < len + 5)
        {
            log.error("token [" + token + "] is to short");
            return  null;
        }
        
        char[] auxUser = token.substring(5, len + 5).toCharArray();
        
        for (int i = 0; i < auxUser.length; i++)
        {
            auxUser[i] = (char) (auxUser[i] ^ secret1);
        }
        
        return new String(auxUser);
    }
}
