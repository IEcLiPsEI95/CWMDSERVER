package ro.ubbcluj.cs.domain;

import java.util.Date;

/**
 * Created by hlupean on 16-Nov-16.
 */
public class User
{
    private int         id;
    private String      username;
    private String      password;
    private long        permissions;
    private String      token;
    private long        loggedInTime;
    
    public User(String username, String password, long permissions)
    {
        this.username       = username;
        this.password       = password;
        this.permissions    = permissions;
        
        token               = null;
        loggedInTime        = 0;
    }

    public User() {

    }

    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public long getLoggedInTime()
    {
        return loggedInTime;
    }
    
    public void setLoggedInTime(long loggedInTime)
    {
        this.loggedInTime = loggedInTime;
    }
    
    public long getPermissions()
    {
        return permissions;
    }
    
    public void setPermissions(long permissions)
    {
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
