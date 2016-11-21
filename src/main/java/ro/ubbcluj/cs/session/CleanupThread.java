package ro.ubbcluj.cs.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.ubbcluj.cs.domain.User;
import sun.awt.Mutex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hlupean on 17-Nov-16.
 */
class CleanupThread extends Thread
{
    private long maxMills;
    private static Logger log = LogManager.getLogger(CleanupThread.class);
    
    private Mutex listMutex;
    private HashMap<String, User> dictUsers;
    
    CleanupThread(Mutex listMutex, HashMap<String, User> dictUsers, long maxMills)
    {
        this.listMutex = listMutex; // daca nu li se modifica valoarea si aici imi bag ^&*% in java
        this.dictUsers = dictUsers;
        this.maxMills = maxMills;
    }
    
    @Override
    public void run()
    {
        log.info("Cleanup thread started!");
        
        long sleepTime;
        
        while (true) // cum il opresc?????
        {
            sleepTime = System.currentTimeMillis();
            
            log.info("Good night");
            while (true)
            {
                try
                {
                    Thread.sleep(1000);
                } 
                catch (InterruptedException ignored)
                {
                } // de ce????????/
                
                // poate mai trebuie verificat ceva aici
                if (System.currentTimeMillis() - sleepTime >= maxMills)
                {
                    break;
                }
            }
            log.info("Good morning");
    
            long currentTime = System.currentTimeMillis();
            
            listMutex.lock();
            Iterator<Map.Entry<String, User>> iter = dictUsers.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, User> entry = iter.next();
                User user = entry.getValue();
                               
                if ((currentTime - user.getLoggedInTime()) >= (maxMills - 2))
                {
                    log.info("Will remove user: " + user.getUsername());
                    user.setLoggedInTime(0);
                    user.setToken(null);
                    
                    iter.remove();
                }
            }
            listMutex.unlock();
        }
    }
}
