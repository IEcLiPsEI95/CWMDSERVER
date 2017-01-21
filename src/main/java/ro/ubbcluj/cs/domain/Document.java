package ro.ubbcluj.cs.domain;

import java.util.ArrayList;

/**
 * Created by hlupean on 17-Jan-17.
 */
//OrdinRector_1235316_DRAFT_0.22536.docx
public class Document
{
    private int id;
    private String baseName;
    private String path;
    private int versionDraftMinor;
    private int versionFinRevMinor;
    private boolean markAsFinal;
    private String user;
    private ArrayList<Integer> signOrder; // document flow
    private int status; // 1 - draft; 2 - final; 3 - final_revizuit
    private int whosNext; // id from signOrder

    public Document(int id, String baseName, String path, int versionDraftMinor, int versionFinRevMinor, boolean markAsFinal, String user, ArrayList<Integer> signOrder, int status, int whosNext) {
        this.id = id;
        this.baseName = baseName;
        this.path = path;
        this.versionDraftMinor = versionDraftMinor;
        this.versionFinRevMinor = versionFinRevMinor;
        this.markAsFinal = markAsFinal;
        this.user = user;
        this.signOrder = signOrder;
        this.status = status;
        this.whosNext = whosNext;
    }

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getVersionDraftMinor() {
        return versionDraftMinor;
    }

    public void setVersionDraftMinor(int versionDraftMinor) {
        this.versionDraftMinor = versionDraftMinor;
    }

    public int getVersionFinRevMinor() {
        return versionFinRevMinor;
    }

    public void setVersionFinRevMinor(int versionFinRevMinor) {
        this.versionFinRevMinor = versionFinRevMinor;
    }

    public boolean isMarkAsFinal() {
        return markAsFinal;
    }

    public void setMarkAsFinal(boolean markAsFinal) {
        this.markAsFinal = markAsFinal;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Integer> getSignOrder() {
        return signOrder;
    }

    public void setSignOrder(ArrayList<Integer> signOrder) {
        this.signOrder = signOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWhosNext() {
        return whosNext;
    }

    public void setWhosNext(int whosNext) {
        this.whosNext = whosNext;
    }
}
