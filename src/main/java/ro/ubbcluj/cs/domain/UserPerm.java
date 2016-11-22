package ro.ubbcluj.cs.domain;

/**
 * Created by hlupean on 17-Nov-16.
 */

// ar fi mai ok sa fie enum... da nu am chef sa ma complic in java...
public class UserPerm
{
    public static final long PERM_NO                = 0x0;
    
    public static final long PERM_GET_USER          = 0x1;
    public static final long PERM_ADD_USER          = 0x2;
    public static final long PERM_UPDATE_USER       = 0x4;
    public static final long PERM_DELETE_USER       = 0x8;
    
    public static final long PERM_ALL               = -1; // asta-i dumnezo
}
