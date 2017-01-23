package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.DocumentTemplate;
import ro.ubbcluj.cs.domain.User;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("SqlDialectInspection")
@Repository
public class DocumentRepository {

    private static Logger log = LogManager.getLogger(DocumentRepository.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void Sign(int documentId) {
        try {
            readWriteLock.writeLock().lock();
            List<Document> documents = jdbcTemplate.query(
                    "select docflows.groupOrder, documents.nextGroup as currentSigningUser from documents " +
                            "inner join doctype on doctype.id = documents.idType " +
                            "inner join docflows on doctype.idFlow = docflows.id " +
                            "where documents.id = ?", new Object[]{documentId}, new SignDocumentRowMapper());
            Document document = documents.get(0);
            int whosNextIndex = document.getSignOrder().indexOf(document.getWhosNext());
            if (whosNextIndex == document.getSignOrder().size() - 1) {
                // status -> blocat
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps;
                    ps = connection.prepareStatement("update documents set idStatus = 4 where documents.id = ?");
                    ps.setInt(1, documentId);
                    return ps;
                });
            } else {
                document.setWhosNext(document.getSignOrder().get(whosNextIndex + 1));
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps;
                    ps = connection.prepareStatement("update documents set nextGroup = ? where documents.id = ?");
                    ps.setInt(1, document.getWhosNext());
                    ps.setInt(2, documentId);
                    return ps;
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    // -1 daca status == blocat
    public int NextToSign(int documentId) {
        try {
            readWriteLock.writeLock().lock();
            List<Document> query = jdbcTemplate.query(
                    "select documents.nextGroup as nextToSign, documents.idStatus from documents " +
                            "where documents.id = ?", new Object[]{documentId}, new NextToSignDocumentRowMapper());
            return query.get(0).getStatus() == 4 ? -1 : query.get(0).getWhosNext();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }


    public void Reject(int documentId) {
        try {
            readWriteLock.writeLock().lock();
            List<Document> documents = jdbcTemplate.query(
                    "select docflows.groupOrder, documents.nextGroup as currentSigningUser, documents.idStatus, documents.versionDraftMinor from documents " +
                    "inner join doctype on doctype.id = documents.idType " +
                    "inner join docflows on doctype.idFlow = docflows.id " +
                    "where documents.id = ?", new Object[]{documentId}, new RejectDocumentRowMapper());
            Document document = documents.get(0);
            document.setStatus(1);
            document.setVersionDraftMinor(document.getVersionDraftMinor() + 1);
            document.setWhosNext(document.getSignOrder().get(0));

            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("update documents set idStatus = ?, versionDraftMinor = ?, nextGroup = ? where documents.id = ?");
                ps.setInt(1, document.getStatus());
                ps.setInt(2, document.getVersionDraftMinor());
                ps.setInt(3, document.getWhosNext());
                ps.setInt(4, documentId);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public String getNameForDownload(int documentType, int documentStatus, String username) {
        int maxStatus = 0;
        try {
            readWriteLock.writeLock().lock();
            List<Document> documents = jdbcTemplate.query(
                    "select  docflows.groupOrder, " +
                            "documents.id as idDoc, " +
                            "documents.path, " +
                            "documents.versionDraftMinor, " +
                            "documents.versiuneFinRevMinor, " +
                            "documents.idStatus, " +
                            "documents.nextGroup " +
                            "from documents " +
                            "inner join doctype on doctype.id = documents.idType " +
                            "inner join docflows on doctype.idFlow = docflows.id " +
                            "inner join users on documents.idUser = users.id "+
                            "where documents.idType = ? and users.username = ? and documents.idStatus = ?",
                    new Object[]{documentType, username, documentStatus}, new DocumentRowMapper());
            // 1 - draft; 2 - final; 3 - final_revizuit, 4 - blocked (gata)
            for (Document document : documents){
                if (maxStatus < document.getStatus())
                    maxStatus = document.getStatus();
            }
            if (maxStatus == 4){
                for (Document document : documents){
                    if (maxStatus == document.getStatus()){
                        return document.getBaseName();
                    }
                }
            } else if (maxStatus == 3){
                int maxRevFinVer = 0;
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxRevFinVer < document.getVersionFinRevMinor()){
                        maxRevFinVer = document.getVersionFinRevMinor();
                    }
                }
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxRevFinVer == document.getVersionFinRevMinor()){
                        return document.getBaseName();
                    }
                }
            } else if (maxStatus == 1){
                int maxDraftVer = 0;
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxDraftVer < document.getVersionDraftMinor()){
                        maxDraftVer = document.getVersionDraftMinor();
                    }
                }
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxDraftVer == document.getVersionDraftMinor()){
                        return document.getBaseName();
                    }
                }
            } else {
                for (Document document : documents){
                    if (maxStatus == document.getStatus()){
                        return document.getBaseName();
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return null;
    }

    public Document getDocument(int docType, User user) {
        int maxStatus = 0;
        try {
            readWriteLock.writeLock().lock();
            List<Document> documents = jdbcTemplate.query(
                    "select  docflows.groupOrder, " +
                            "documents.id as idDoc, " +
                            "documents.path, " +
                            "documents.versionDraftMinor, " +
                            "documents.versiuneFinRevMinor, " +
                            "documents.idStatus, " +
                            "documents.nextGroup " +
                    "from documents " +
                    "inner join doctype on doctype.id = documents.idType " +
                    "inner join docflows on doctype.idFlow = docflows.id " +
                    "where documents.idType = ? and documents.idUser = ?", new Object[]{docType, user.getId()}, new DocumentRowMapper());
            // 1 - draft; 2 - final; 3 - final_revizuit, 4 - blocked (gata)
            for (Document document : documents){
                if (maxStatus < document.getStatus())
                    maxStatus = document.getStatus();
            }
            if (maxStatus == 4){
                for (Document document : documents){
                    if (maxStatus == document.getStatus()){
                        return document;
                    }
                }
            } else if (maxStatus == 3){
                int maxRevFinVer = 0;
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxRevFinVer < document.getVersionFinRevMinor()){
                        maxRevFinVer = document.getVersionFinRevMinor();
                    }
                }
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxRevFinVer == document.getVersionFinRevMinor()){
                        return document;
                    }
                }
            } else if (maxStatus == 1){
                int maxDraftVer = 0;
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxDraftVer < document.getVersionDraftMinor()){
                        maxDraftVer = document.getVersionDraftMinor();
                    }
                }
                for (Document document : documents){
                    if (maxStatus == document.getStatus() && maxDraftVer == document.getVersionDraftMinor()){
                        return document;
                    }
                }
            } else {
                for (Document document : documents){
                    if (maxStatus == document.getStatus()){
                        return document;
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return null;
    }

    public List<DocumentTemplate> GetTemplates() {
        return jdbcTemplate.query(
                "select docType.id, docType.name, docType.docTemplatePath, docFlows.groupOrder from docType " +
                "inner join docFlows on docFlows.id = docType.idFlow", new Object[]{}, new DocumentTemplateRowMapper());
    }

    public boolean UserHasDocument(User user, int documentType) {
        return getDocument(documentType, user) != null;
    }

    public DocumentTemplate GetDocumentTemplate(int documentType) throws InvalidDocumentTemplateId {
        DocumentTemplate documentTemplate = null;
        Boolean found = false;
        List<DocumentTemplate> documentTemplates = GetTemplates();
        for (DocumentTemplate template: documentTemplates) {
            if (template.getId() == documentType){
                found = true;
                documentTemplate = template;
                break;
            }
        }
        if (found) {
            return documentTemplate;
        }
        else {
            throw new InvalidDocumentTemplateId();
        }
    }

    public List<Document> getAllDocsToSignForGroup(int groupId)
    {
        try {
            readWriteLock.writeLock().lock();
            return jdbcTemplate.query(
                    "select  docflows.groupOrder, " +
                            "documents.id as idDoc, " +
                            "documents.path, " +
                            "documents.versionDraftMinor, " +
                            "documents.versiuneFinRevMinor, " +
                            "documents.idStatus, " +
                            "documents.nextGroup " +
                            "from documents " +
                            "inner join doctype on doctype.id = documents.idType " +
                            "inner join docflows on doctype.idFlow = docflows.id " +
                            "where documents.nextGroup = ?",
                    new Object[]{groupId}, new DocumentRowMapper());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public void setDocsToSignForGroup(Document document, User user)
    {
        try {
            readWriteLock.writeLock().lock();
            jdbcTemplate.update(
                    "insert INTO `documents`(`id`, `path`, `versionDraftMinor`, `versiuneFinRevMinor`, `idUser`, `idType`, `idStatus`, `nextGroup`) VALUES ( NULL , ?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{document.getBaseName(), document.getVersionDraftMinor(), document.getVersionFinRevMinor(), user.getId(),document.getIdDocumentType(),document.getStatus(),document.getWhosNext()},
                    new int[] {Types.VARCHAR,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.INTEGER});
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public class InvalidDocumentTemplateId extends Throwable {
    }
}
