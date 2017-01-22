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
    private int versionDraftMinor;
    private int versionFinRevMinor;
    private String user;
    private ArrayList<Integer> signOrder;       // document flow
    private int status;                         // 1 - draft; 2 - final; 3 - final_revizuit, 4 - blocked (gata)
    private int whosNext;                       // id from signOrder
    private int idDocumentType;

    public Document() {
    }

    public Document(String name, int type) {
        this.baseName = name;
        idDocumentType= type;

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

    public int getIdDocumentType()
    {
        return idDocumentType;
    }

    public void setIdDocumentType(int idDocumentType)
    {
        this.idDocumentType = idDocumentType;
    }
}
