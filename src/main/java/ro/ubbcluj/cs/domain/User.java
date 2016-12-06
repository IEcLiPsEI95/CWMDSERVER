package ro.ubbcluj.cs.domain;

import java.util.Date;

/**
 * Created by hlupean on 16-Nov-16.
 */
public class User
{
    // form database
    private int         id;
    private String      username; // an email
    private String      password;
    private long        permissions;
    private String      lastname;
    private String      firstname;
    private String      cnp;   // 13 chars
    private String      phone; // 10 chars

    // application
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

    public User(String username, String password, long permissions, String lastname, String firstname, String cnp, String phone) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.lastname = lastname;
        this.firstname = firstname;
        this.cnp = cnp;
        this.phone = phone;

        token               = null;
        loggedInTime        = 0;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
