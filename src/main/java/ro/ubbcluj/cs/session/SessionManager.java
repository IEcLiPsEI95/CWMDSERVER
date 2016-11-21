package ro.ubbcluj.cs.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
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
    private static SessionManager instance = null;
    
    
    private static Logger log = LogManager.getLogger(SessionManager.class);
    
    private Mutex                   listMutex;
    private HashMap<String, User>   dictUsers;
    private SecureRandom            random;
    private byte                    secret1;
    private byte                    secret2;
    private char                    padding;
    private CleanupThread           hThCleanup;
    private final long              maxMils     = 10 * 1000; //60 * 60 * 1000;
    
    @Autowired
    private UserController ctrlUser;// = UserController.getInstance();
    
    @Autowired
    private UserRepository repoUser;
//    
//    public static SessionManager getInstance()
//    {
//        if (instance == null)
//        {
//            synchronized (SessionManager.class)
//            {
//                if (instance == null)
//                {
//                    instance = new SessionManager();
//                }
//            }
//        }
//        return instance;
//    }
    
    public SessionManager()
    {
        this.dictUsers  = new HashMap<>();
        this.random     = new SecureRandom();
        
        this.listMutex  = new Mutex();
        
        this.secret1    = (byte) (random.nextInt(20) + 5);
        this.secret2    = (byte) (random.nextInt(50) + 5);
        this.padding    = (char) (random.nextInt(26) + 'A');
        
        this.hThCleanup = new CleanupThread(listMutex, dictUsers, maxMils);
        this.hThCleanup.start();
    }
    
    public String Login(String username, String password)
    {
        try
        {
            User user = this.GetUser(username, password);
            if (null == user)
            {
                log.error("Login :: Failed to get user " + username + " from repository");
                return null;
            }
    
            String token = this.AddUser(user);
            if (null == token)
            {
                log.error("Login :: Failed to add " + username + " in memory");
                return null;
            }
            
            log.info("User " + username + " is now logged in");
            return token;
        }
        catch (Exception ex)
        {
            log.error("Login :: bag pula in java");
            log.error(ex.toString());
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
            log.error("Could not get username from token " + token);
            return null;
        }
        
        listMutex.lock();
        user = dictUsers.get(username);
        listMutex.unlock();
        
        if ((null == user) || !(user.getToken().equals(token)))
        {
            log.error("No user is logged in with token " + token);
            return null;
        }
        
        if ((System.currentTimeMillis() - user.getLoggedInTime()) >= (maxMils - 1))
        {
            log.error("Token expired for user: " + user.getUsername());
            ResetTokeForUser(user.getUsername());
            return null;
        }
        
        log.info("Found " + user.getUsername() + " for token " + token);
        log.info("User " + user.getUsername() + " has " + user.getPermissions() + " rights. Required rights: " + requiredPermissions);
        
        result = (0 != (user.getPermissions() & requiredPermissions));
        log.info("User " + user.getUsername() + " has " + (result ? "ENOUGH" : "INSUFFICIENT") + " rights");
        
        user.setLoggedInTime(System.currentTimeMillis());
        return user;
    }
    
    private String AddUser(User user)
    {
        listMutex.lock();
        
        if (dictUsers.containsKey(user.getUsername()))
        {
            log.error("User " + user.getUsername() + " is already logged in.");
            listMutex.unlock();
            return null;
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
            log.error("token is null / empty / too short");
            return  null;
        }
        
        byte startIndex;
        //noinspection StatementWithEmptyBody
        for (startIndex = 0; token.charAt(startIndex) == padding; startIndex++);
        int len = Integer.parseInt(token.substring(startIndex, 5));
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
    
    private void ResetTokeForUser(String username)
    {
        // nu stiu sigur daca are vreun rost functia asta, da' o las momentan
        // probabil ca nu e nevoie sa fac de 2 ori get... da mai bine asa decat sa ma risc cu java...
        
        listMutex.lock();
        dictUsers.get(username).setToken(null);
        dictUsers.get(username).setLoggedInTime(0);
        listMutex.unlock();
    }
    
    private User GetUser(String username, String password)
    {
        return ctrlUser.GetUserByUsernameAndPassword(username, password);
    }
}
