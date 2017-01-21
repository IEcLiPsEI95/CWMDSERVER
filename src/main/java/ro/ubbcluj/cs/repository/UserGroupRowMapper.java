package ro.ubbcluj.cs.repository;

import ro.ubbcluj.cs.domain.UserGroup;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by cluca on 21-Jan-17.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class UserGroupRowMapper implements RowMapper<UserGroup> {

    @Override
    public UserGroup mapRow(ResultSet resultSet, int i) throws SQLException {
        UserGroup userGroup = new UserGroup();
        userGroup.setId(resultSet.getInt("id"));
        userGroup.setName(resultSet.getString("name"));
        return userGroup;
    }
}
