package ro.ubbcluj.cs.domain;

/**
 * Created by hlupean on 17-Nov-16.
 */

// ar fi mai ok sa fie enum... da nu am chef sa ma complic in java...
public class UserPerm
{
    public static final long PERM_NO                = 0x0; 
    
    public static final long PERM_READ              = 0x1; // sunt doar ca exemplu permisiunile astea
    public static final long PERM_ADD               = 0x2;
    public static final long PERM_MODIFY            = 0x4;
    public static final long PERM_DELETE            = 0x8;
    
    
    public static final long PERM_ALL               = -1; // asta-i dumnezo
}
