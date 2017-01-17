package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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
}
