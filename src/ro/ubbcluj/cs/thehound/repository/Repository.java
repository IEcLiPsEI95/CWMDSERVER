package ro.ubbcluj.cs.thehound.repository;

import java.sql.Connection;

/**
 * Created by cristi on 11/13/2016.
 */
public class Repository {

    private final String sqlConnectionUrl;
    private Connection sqlConnection;

    public Repository(String jdbcDirver, String sqlDatabase) {
        this.sqlConnectionUrl = jdbcDirver + sqlDatabase;
    }
}
