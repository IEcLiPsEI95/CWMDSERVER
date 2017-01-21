package ro.ubbcluj.cs.domain;

/**
 * Created by cluca on 21-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class UserGroup {
    private int id;
    private String name;

    public UserGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
