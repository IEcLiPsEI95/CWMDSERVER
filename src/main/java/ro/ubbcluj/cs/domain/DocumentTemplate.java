package ro.ubbcluj.cs.domain;

import java.util.ArrayList;

/**
 * Created by cluca on 22-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class DocumentTemplate {
    private int id;
    private String name;
    private String path;
    private ArrayList<Integer> flow;

    public DocumentTemplate() {
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Integer> getFlow() {
        return flow;
    }

    public void setFlow(ArrayList<Integer> flow) {
        this.flow = flow;
    }
}
