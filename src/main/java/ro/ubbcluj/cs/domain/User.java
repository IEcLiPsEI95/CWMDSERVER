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
    private String      lastName;
    private String      firstName;
    private String      cnp;   // 13 chars
    private String      phone; // 10 chars
    private int         groupId;
    private String      groupName;

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
    
    public User(String username, String password, long permissions, String lastName, String firstName, String cnp, String phone, int groupId)
    {
        this.username       = username;
        this.password       = password;
        this.permissions    = permissions;
        this.lastName       = lastName;
        this.firstName      = firstName;
        this.cnp            = cnp;
        this.phone          = phone;
        this.groupId        = groupId;
        
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
    
    public int getGroupId()
    {
        return groupId;
    }
    
    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }
    
    public String getGroupName()
    {
        return groupName;
    }
}
