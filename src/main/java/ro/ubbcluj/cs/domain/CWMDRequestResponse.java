package ro.ubbcluj.cs.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * Created by hlupean on 28-Nov-16.
 */
public class CWMDRequestResponse
{
    private String      message;
    private String      token;
    private long        permissions;
    private HttpStatus  status;
    
    
    public CWMDRequestResponse()
    {
        
    }
    
    public CWMDRequestResponse(String message, String token, HttpStatus status, long permissions)
    {
        this.message        = message;
        this.token          = token;
        this.permissions    = permissions;
        this.status         = status;
    }
    
    public CWMDRequestResponse(String message, HttpStatus status)
    {
        this.message    = message;
        this.status     = status;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public HttpStatus getStatus()
    {
        return status;
    }
    
    public void setStatus(HttpStatus status)
    {
        this.status = status;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public long getPermissions()
    {
        return permissions;
    }
    
    public void setPermissions(long permissions)
    {
        this.permissions = permissions;
    }
    
    public ResponseEntity<?> createResponse()
    {
        return createResponse(this.token, this.message, this.permissions, this.status);
    }
    
    public static ResponseEntity<?> createResponse(Object message, HttpStatus status)
    {
        return createResponse(null, message, 0, status);
    }
    
    public static ResponseEntity<?> createResponse(String token, Object message, long permissions, HttpStatus status)
    {
        HashMap<String, Object> resp = new HashMap<>();
        
        resp.put("token",       token);
        resp.put("message",     message);
        resp.put("permissions", String.valueOf(permissions));
        resp.put("status",      String.valueOf(status));
        
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        String jsonResp = g.toJson(resp);

        return new ResponseEntity<>(jsonResp, HttpStatus.OK);
    }
}
