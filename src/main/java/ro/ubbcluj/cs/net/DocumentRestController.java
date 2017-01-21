package ro.ubbcluj.cs.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubbcluj.cs.controller.UserController;
import ro.ubbcluj.cs.domain.CWMDRequestResponse;
import ro.ubbcluj.cs.domain.User;
import ro.ubbcluj.cs.domain.UserPerm;
import ro.ubbcluj.cs.session.SessionManager;
import ro.ubbcluj.cs.controller.DocumentController;

/**
 * Created by hzugr on 11/15/2016.
 */

@RequestMapping("docs")
@RestController
public class DocumentRestController
{
    private static final String TOKEN_HEADER = "Authorization";
    private static Logger log = LogManager.getLogger(DocumentRestController.class);
    
    @Autowired
    private SessionManager sm;
    
    @Autowired
    private DocumentController ctrlDocs;
    
    
    // asta ii doar de test... sa vedem ca merge conexiunea
    @RequestMapping(value = "test/id={id}", method = RequestMethod.GET)
    public @ResponseBody String Test(@PathVariable int id)
    {
        log.info("s-o conectat: " + id);
        return Integer.toString(id * 2);
    }
    
    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> Sign(@RequestHeader(value = TOKEN_HEADER, required = true) String token,
                                                @RequestBody int documentId)
    {
        try{
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_MANAGER);
            ctrlDocs.Sign(documentId, user);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
        
    }
    
    @RequestMapping(value = "reject", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> Reject(@RequestHeader(value = TOKEN_HEADER, required = true) String token,
                             @RequestBody int documentId)
    {
        try{
            User user = sm.GetLoggedInUser(token, UserPerm.PERM_MANAGER);
            ctrlDocs.Reject(documentId, user);
            return CWMDRequestResponse.createResponse("OK", HttpStatus.OK);
        }
        catch (UserController.RequestException e) {
            log.error(e.getMessage());
            return CWMDRequestResponse.createResponse(e.getMessage(), e.getStatus());
        }
        
    }
    
}

