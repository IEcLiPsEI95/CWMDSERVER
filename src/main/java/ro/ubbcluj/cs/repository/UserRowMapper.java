package ro.ubbcluj.cs.repository;


import org.springframework.jdbc.core.RowMapper;
import ro.ubbcluj.cs.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cluca on 19-Nov-16.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
//        user.setPassword(resultSet.getString("password"));
        user.setUsername(resultSet.getString("username"));
        user.setPermissions(resultSet.getLong("permissions"));
        user.setLastName(resultSet.getString("lastname"));
        user.setFirstName(resultSet.getString("firstname"));
        user.setCnp(resultSet.getString("cnp"));
        user.setPhone(resultSet.getString("phone"));
        return user;
    }
}
