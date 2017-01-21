package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.domain.Document;

import java.sql.PreparedStatement;
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

    public String getNameForDownload(String documentType, int documentStatus, String username) {
        //TODO get the name of last document(specified by documentType and documentStatus) upload by user(specified by username)

        return "CoolName";
    }
}
