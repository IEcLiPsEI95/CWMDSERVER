package ro.ubbcluj.cs.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.domain.User;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by cluca on 19-Nov-16.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * seteaza pentru user-ul dat ca parametru id-ul acestuia din baza de date; folositor in viitorele operatii
     *
     * @param user user-ul de adaugat
     * @throws UsersPasswordIsNull parola este null-a
     * @throws UserIsNull          user-ul este null
     * @throws UsersUsernameIsNull username-ul este null
     */
    public void insert(User user) throws UsersPasswordIsNull, UserIsNull, UsersUsernameIsNull {
        try {
            user.setId(insertAndGetId(user));
        } catch (NullPointerException e) {
            throw new UserIsNull();
        }
    }

    /**
     * @param userId id-ul user-ului de sters
     * @return null daca user-ul nu este in baza de date sau user-ul care sa sters
     * @throws InvalidUserId daca userId este <= 0
     */
    public User delete(int userId) throws InvalidUserId {
        User user = getUserById(userId);
        if (user != null) {
            try {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps;
                    ps = connection.prepareStatement("DELETE FROM users WHERE id=?");
                    ps.setInt(1, userId);
                    return ps;
                });
                return user;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    /**
     * @param username username de cautat
     * @param password parola asociata user-ului
     * @return null daca username sau password incorecte altfel user-ul cu id, username, password, permissions
     * @throws PasswordIsNull daca password == null
     * @throws UsernameIsNull daca username == null
     */
    public User getUserByUsernameAndPassword(String username, String password) throws PasswordIsNull, UsernameIsNull {
        if (password == null) throw new PasswordIsNull();
        if (username == null) throw new UsernameIsNull();
        try {

            List<User> users = jdbcTemplate.query(
                    "SELECT id, username, password, permissions FROM users WHERE username = ? AND password = ?",
                    new Object[]{username, password}, new UserRowMapper());
            return users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param userId   id-ul userului pentru care se face update
     * @param username noul username
     * @throws InvalidUserId  daca userId <= 0
     * @throws UsernameIsNull daca username == null
     * @throws UserNotFound   daca user-ul nu exista in baza de date
     */
    public void updateUsername(int userId, String username) throws InvalidUserId, UsernameIsNull, UserNotFound {
        if (userId <= 0) throw new InvalidUserId();
        if (username == null) throw new UsernameIsNull();
        if (getUserById(userId) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET username = ? WHERE id = ?");
                ps.setString(1, username);
                ps.setInt(2, userId);
                return ps;
            });
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * @param userId   id-ul userului pentru care se face update
     * @param password noua parola
     * @throws InvalidUserId  daca userId <= 0
     * @throws UserNotFound   daca user-ul nu exista in baza de date
     * @throws PasswordIsNull daca password == null
     */
    public void updatePassword(int userId, String password) throws InvalidUserId, UserNotFound, PasswordIsNull {
        if (userId <= 0) throw new InvalidUserId();
        if (password == null) throw new PasswordIsNull();
        if (getUserById(userId) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
                ps.setString(1, password);
                ps.setInt(2, userId);
                return ps;
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param userId      id-ul userului pentru care se face update
     * @param permissions noile permisiuni
     * @throws InvalidUserId daca userId <= 0
     * @throws UserNotFound  daca user-ul nu exista in baza de date
     */
    public void updatePersmissions(int userId, long permissions) throws InvalidUserId, UserNotFound {
        if (userId <= 0) throw new InvalidUserId();
        if (getUserById(userId) == null) throw new UserNotFound();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET permissions = ? WHERE id = ?");
                ps.setLong(1, permissions);
                ps.setInt(2, userId);
                return ps;
            });
        } catch (Exception e) {
            throw e;
        }
    }

    private User getUserById(int userId) throws InvalidUserId {
        if (userId <= 0) throw new InvalidUserId();

        try {
            List<User> users = jdbcTemplate.query("SELECT id, username, password, permissions FROM users WHERE id = ?", new Object[]{userId}, new UserRowMapper());
            return users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            throw e;
        }
    }

    private int insertAndGetId(User user) throws UserIsNull, UsersPasswordIsNull, UsersUsernameIsNull {
        if (user == null) throw new UserIsNull();
        if (user.getPassword() == null) throw new UsersPasswordIsNull();
        if (user.getUsername() == null) throw new UsersUsernameIsNull();

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("INSERT INTO users (username, password, permissions) VALUES (?,?,?)");
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setLong(3, user.getPermissions());
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            throw e;
        }
    }


    public class InvalidUserId extends Throwable {
    }

    public class UserIsNull extends Throwable {
    }

    public class UsersPasswordIsNull extends Throwable {
    }

    public class UsersUsernameIsNull extends Throwable {
    }

    public class UsernameIsNull extends Throwable {
    }

    public class UserNotFound extends Throwable {
    }

    public class PasswordIsNull extends Throwable {
    }
}
