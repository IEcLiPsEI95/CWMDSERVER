package ro.ubbcluj.cs.domain;

/**
 * Created by hlupean on 17-Nov-16.
 */

// ar fi mai ok sa fie enum... da nu am chef sa ma complic in java...
public class UserPerm
{
    public static final long PERM_NO                = 0x0;
    
    public static final long PERM_GET_USER          = 0x00000001;
    public static final long PERM_ADD_USER          = 0x00000002;
    public static final long PERM_UPDATE_USER       = 0x00000004;
    public static final long PERM_DELETE_USER       = 0x00000018; // permission to delete users (this includes permission to logout other users)
    public static final long PERM_LOGOUT_USER       = 0x00000010; // permission to logout OTHER users
    
    
    public static final long PERM_ALL               = -1; // asta-i dumnezo
}
