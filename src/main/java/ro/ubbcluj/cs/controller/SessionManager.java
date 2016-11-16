package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.cs.domain.User;

import sun.awt.Mutex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;


public class SessionManager
{
    private static SessionManager instance = null;
    
    
    private Object repo;
    private static Logger LOG = LogManager.getLogger("file");
    
    private Mutex listMutex;
    private HashMap<String, User> dictUsers;
    private SecureRandom random;
    private MessageDigest md;
    
    public static SessionManager getInstance()
    {
        if (instance == null)
        {
            synchronized (SessionManager.class)
            {
                if (instance == null)
                {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }
    
    
    private SessionManager()
    {
        this.dictUsers  = new HashMap<>();
        this.random     = new SecureRandom();
        
        this.listMutex  = new Mutex();
        
        try
        {
            this.md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            // promit ca nu se ajunge aici
        }
    }
    
    public String Login(String username, String password)
    {
        try
        {
            String token;
            
            User user = this.GetUser(username, password);
            if (null == user)
            {
                LOG.error("Login :: Failed to get user " + username + " from repository");
                return null;
            }
            
            token = this.AddUser(user);
            if (null == token)
            {
                LOG.error("Login :: Failed to add " + username + " in memory");
                return null;
            }
            
            LOG.info("User " + username + " is now logged in");
            return token;
        }
        catch (Exception ex)
        {
            LOG.error("Login :: bag pula in java");
            LOG.error(ex.toString());
            return null;
        }
    }
    
    // verifica tokenul daca este logat si daca are permisiunile necesare
    public boolean IsLoggedIn(String token, int requiredPermissions)
    {
        User user = null;
        boolean result = false;
        
        listMutex.lock();
        // asta nu ramane asa... o sa modific metoda de generare a tokenului ca sa pot lua username-ul din token
        for (String username : dictUsers.keySet())
        {
            user = dictUsers.get(username);
            if (user.getToken().equals(token))
            {
                result = true;
                break;
            }
        }
        listMutex.unlock();
        
        if (!result)
        {
            LOG.error("No user is logged in with token " + token);
            return false;
        }
        LOG.info("Found " + user.getUsername() + " for token " + token);
        LOG.info("User " + user.getUsername() + " has " + user.getPermissions() + " rights. Required rights: " + requiredPermissions);
        
        result = (0 != (user.getPermissions() & requiredPermissions));
        LOG.info("User " + user.getUsername() + " has " + (result ? "ENOUGH" : "INSUFFICIENT") + " rights");
        
        return result;
    }
    
    
    private String AddUser(User user)
    {
        listMutex.lock();
        
        if (dictUsers.containsKey(user.getUsername()))
        {
            LOG.error("User " + user.getUsername() + " is already logged in.");
            listMutex.unlock();
            return null;
        }
        
        user.setLoggedInTime(System.currentTimeMillis());
        String token = CreateToken(user);
        user.setToken(token);
        
        dictUsers.put(user.getUsername(), user);
        
        listMutex.unlock();
        
        LOG.info("Token [" + token + "] was created for user " + user.getUsername());
        return token;
    }
    
    private String CreateToken(User user)
    {
        String token;
        
        md.reset();
        byte[] array = md.digest(user.getUsername().getBytes());
        
        StringBuilder sb = new StringBuilder();
        for (byte b : array)
        {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        token = sb.toString();
        
        String cacat1 = (new BigInteger(130, random)).toString(32);
        String cacat2 = (new BigInteger(130, random)).toString(32);
        String cacat3 = (new BigInteger(130, random)).toString(32);
        token = token + cacat1 + cacat2 + cacat3;
        
        return token;
    }
    
    private User GetUser(String username, String password)
    {
        if (username.equals("a@a.com") && password.equals("a"))
        {
            return new User(username, password, 2);
        }
        
        if (username.equals("b@b.com") && password.equals("b"))
        {
            return new User(username, password, 1);
        }
        
        if (username.equals("a") && password.equals("a"))
        {
            return new User(username, password, 3);
        }
        
        return null;
    }
}
