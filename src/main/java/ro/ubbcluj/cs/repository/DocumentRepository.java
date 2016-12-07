package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by hzugr on 11/15/2016.
 */
@Repository
public class DocumentRepository {
    
    private static Logger log = LogManager.getLogger(UserRepository.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;

}
