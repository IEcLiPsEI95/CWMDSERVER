package ro.ubbcluj.cs.domain;

/**
 * Created by hlupean on 21-Jan-17.
 */
public class DocumentType
{
    private int     id;
    private int     idFlow;
    private String  name;
    private String  templateName;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getIdFlow()
    {
        return idFlow;
    }
    
    public void setIdFlow(int idFlow)
    {
        this.idFlow = idFlow;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getTemplateName()
    {
        return templateName;
    }
    
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }
}
