package ro.ubbcluj.cs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.repository.DocumentRepository;
import sun.awt.Mutex;

/**
 * Created by hlupean on 17-Nov-16.
 */
public class DocumentController
{
    private static DocumentController instance = null;
    
    @Autowired
    private DocumentRepository repo;
    private static Logger log = LogManager.getLogger("file");
    
    public static DocumentController getInstance()
    {
        if (instance == null)
        {
            synchronized (DocumentController.class)
            {
                if (instance == null)
                {
                    instance = new DocumentController();
                }
            }
        }
        return instance;
    }
    
    private DocumentController()
    {
        
    }
    
}
