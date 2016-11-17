package ro.ubbcluj.cs.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.cs.domain.User;

import ro.ubbcluj.cs.domain.UserPerm;
import sun.awt.Mutex;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;


public class SessionManager
{
    private static SessionManager instance = null;
    
    private Object repo;
    private static Logger LOG = LogManager.getLogger("file");
    
    private Mutex                   listMutex;
    private HashMap<String, User>   dictUsers;
    private SecureRandom            random;
    private byte                    secret1;
    private byte                    secret2;
    private char                    padding;
    private CleanupThread           hThCleanup;
    
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
        
        this.secret1    = (byte) (random.nextInt(20) + 5);
        this.secret2    = (byte) (random.nextInt(50) + 5);
        this.padding    = (char) (random.nextInt(26) + 'A');
        
        this.hThCleanup = new CleanupThread(listMutex, dictUsers);
        this.hThCleanup.run();
        
    }
    
    public String Login(String username, String password)
    {
        try
        {
            String token;
            
            User user = this.GetFakeUser(username, password);
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
    public User GetLoggedInUser(String token, int requiredPermissions)
    {
        User user;
        boolean result;
        String username = GetUsernameFromToken(token);
        
        if (null == username)
        {
            LOG.error("Could not get username from token " + token);
            return null;
        }
        
        listMutex.lock();
        user = dictUsers.get(username);
        listMutex.unlock();
        
        if ((null == user) || !(user.getToken().equals(token)))
        {
            LOG.error("No user is logged in with token " + token);
            return null;
        }
        
        LOG.info("Found " + user.getUsername() + " for token " + token);
        LOG.info("User " + user.getUsername() + " has " + user.getPermissions() + " rights. Required rights: " + requiredPermissions);
        
        result = (0 != (user.getPermissions() & requiredPermissions));
        LOG.info("User " + user.getUsername() + " has " + (result ? "ENOUGH" : "INSUFFICIENT") + " rights");
        
        user.setLoggedInTime(System.currentTimeMillis());
        return user;
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
        
        token = new String(auxToken);
        token = token + (new BigInteger(130, random)).toString(32) +
                (new BigInteger(130, random)).toString(32) + (new BigInteger(130, random)).toString(32);
        
        System.out.println("TOKEEEEEEEEEEEN: " + token);
        return token;
    }
    
    private String GetUsernameFromToken(String token)
    {
        if (null == token || token.isEmpty() || token.length() < 10)
        {
            LOG.error("token is null / empty / too short");
            return  null;
        }
        
        byte startIndex;
        //noinspection StatementWithEmptyBody
        for (startIndex = 0; token.charAt(startIndex) == padding; startIndex++);
        int len = Integer.parseInt(token.substring(startIndex, 5));
        len = (len - secret1) / secret2;
        
        if (token.length() < len + 5)
        {
            LOG.error("token [" + token + "] is to short");
            return  null;
        }
        
        char[] auxUser = token.substring(5, len + 5).toCharArray();
        
        for (int i = 0; i < auxUser.length; i++)
        {
            auxUser[i] = (char) (auxUser[i] ^ secret1);
        }
        
        return new String(auxUser);
    }
    
    private User GetFakeUser(String username, String password)
    {
        if (username.equals("abcdefgh@aaaa.com") && password.equals("a"))
        {
            return new User(username, password, UserPerm.PERM_MODIFY);
        }
        
        if (username.equals("b@b.com") && password.equals("b"))
        {
            return new User(username, password, UserPerm.PERM_ADD | UserPerm.PERM_READ | UserPerm.PERM_DELETE);
        }
        
        if (username.equals("a") && password.equals("a"))
        {
            return new User(username, password, UserPerm.PERM_ALL);
        }
        
        return null;
    }
}
