package ro.ubbcluj.cs.domain;

/**
 * Created by hlupean on 17-Jan-17.
 */
//OrdinRector_1235316_DRAFT_0.22536.docx
public class Document
{
    private int     id;
    private String  baseName;
    private String  path;
    private String  userName; // owner
    private String  signatures;
    private String  currentSignature;
    private int     status;
    private boolean markAsFinal;
    
    public Document(int id, String baseName, String path, String userName, String signatures, String currentSignature, int status, boolean markAsFinal)
    {
        this.id = id;
        this.baseName = baseName;
        this.path = path;
        this.userName = userName;
        this.signatures = signatures;
        this.currentSignature = currentSignature;
        this.status = status;
        this.markAsFinal = markAsFinal;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getSignatures()
    {
        return signatures;
    }
    
    public void setSignatures(String signatures)
    {
        this.signatures = signatures;
    }
    
    public String getCurrentSignature()
    {
        return currentSignature;
    }
    
    public void setCurrentSignature(String currentSignature)
    {
        this.currentSignature = currentSignature;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public boolean isMarkAsFinal()
    {
        return markAsFinal;
    }
    
    public void setMarkAsFinal(boolean markAsFinal)
    {
        this.markAsFinal = markAsFinal;
    }
    
    public String getBaseName()
    {
        return baseName;
    }
    
    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }
}
