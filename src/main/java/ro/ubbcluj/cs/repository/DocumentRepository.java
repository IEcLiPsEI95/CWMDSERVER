package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.domain.Document;
import ro.ubbcluj.cs.domain.User;

/**
 * Created by hzugr on 11/15/2016.
 */
@Repository
public class DocumentRepository {
    
    private static Logger log = LogManager.getLogger(DocumentRepository.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public String getSignatures(User user)
    {
        //TODO returning the signatures
        return "1";
    }

    public void Sign(int documentId) {
        //TODO change the signatures to next if you know the documentId
    }

    public int NextToSign(int documentId) {
        //TODO return next to sign from flows if you know the documentId
        return 1;
    }

    public void Reject(int documentId) {
        //TODO change the document to draft and
    }

    public String getNameForDownload(String documentType, int documentStatus, String username) {
        //TODO get the name of last document(specified by documentType and documentStatus) upload by user(specified by username)

        return "CoolName";
    }
}
