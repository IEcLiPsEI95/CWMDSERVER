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
    private final long maxMills =  60 * 60 * 1000;
    private static Logger LOG = LogManager.getLogger("CleanupThread");
    
    private Mutex listMutex;
    private HashMap<String, User> dictUsers;
    
    CleanupThread(Mutex listMutex, HashMap<String, User> dictUsers)
    {
        this.listMutex = listMutex; // daca nu li se modifica valoarea si aici imi bag ^&*% in java
        this.dictUsers = dictUsers;
    }
    
    @Override
    public void run()
    {
        LOG.info("Cleanup thread started!");
        
        long sleepTime;
        
        while (true) // cum il opresc?????
        {
            sleepTime = System.currentTimeMillis();
            
            while (true)
            {
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored)
                {
                } // de ce????????/
                // poate mai trebuie verificat ceva aici
                if (System.currentTimeMillis() - sleepTime >= maxMills)
                {
                    break;
                }
            }
            LOG.info("Good morning");
    
            long currentTime = System.currentTimeMillis();
            
            listMutex.lock();
            Iterator<Map.Entry<String, User>> iter = dictUsers.entrySet().iterator();
            while (iter.hasNext())
            {
                Map.Entry<String, User> entry = iter.next();
                User user = entry.getValue();
                
                if (user.getLoggedInTime() - currentTime > maxMills)
                {
                    LOG.info("Will remove user: " + user.getUsername());
                    iter.remove();
                }
            }
            listMutex.unlock();
        }
    }
}
