//package ro.ubbcluj.cs.net;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import ro.ubbcluj.cs.repository.DocumentRepository;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import org.apache.commons.logging.LogFactory;
//import ro.ubbcluj.cs.session.SessionManager;
//import ro.ubbcluj.cs.domain.User;
//import java.util.Collection;
//
///**
// * Created by hzugr on 11/15/2016.
// */
//@RestController
//public class DocumentRestController
//{
//    
//    public static final String API_DOCS = "/api/docs";
//    private static Logger LOG = LogManager.getLogger("file");
////    private SessionManager sm = SessionManager.getInstance();
//    
//    @Autowired
//    private DocumentRepository noteRepository;
//    
//    
//    @RequestMapping(value = API_DOCS, method = RequestMethod.GET)
//    public String getAll()
//    {
////        String token = "aaa"; // asta trebuie luat de undeva din header
////        User user;
////        user = sm.GetLoggedInUser(token, 5);
////        
////        if (null == user)
////        {
////            LOG.error("User si not logged in or does not have enough rights");
////            return null; // ceva ce sa ii zica ca nu are acces
////        }
////    
////        System.out.println("aa");
//        return "muie";
//        
////        return "adgda";
//    }
//}
