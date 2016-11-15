package ro.ubbcluj.cs.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by hzugr on 11/15/2016.
 */
@Repository
public class DocumentRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

}
