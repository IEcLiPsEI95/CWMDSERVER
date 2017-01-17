package ro.ubbcluj.cs.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ro.ubbcluj.cs.domain.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Created by cluca on 19-Nov-16.
 * Project name CWMDSERVER
 * Email: luca_corneliu2003@yahoo.com
 */
@Repository
public class UserRepository {
    
    private static Logger log = LogManager.getLogger(UserRepository.class);
    
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
    public void insert(User user) throws UsersPasswordIsNull, UserIsNull, UsersUsernameIsNull, UsersFirstnameIsNull, UsersCnpIsInvalid, UsersPhoneIsInvalid, UsersLastnameIsNull {
        try {
            user.setId(insertAndGetId(user));
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            throw new UserIsNull();
        }
    }

    /**
     
     * @return null daca user-ul nu este in baza de date sau user-ul care sa sters
     * @throws InvalidUserId daca userId este <= 0
     */
    public User delete(String username) throws InvalidUserId, UsersUsernameIsNull
    {
        User user = getUserByUsername(username);
        if (user != null) {
            try {
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps;
                    ps = connection.prepareStatement("DELETE FROM users WHERE username=?");
                    ps.setString(1, username);
                    return ps;
                });
                return user;
            } catch (Exception e) {
                log.error(e.getMessage());
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
    
        if (username == null) throw new UsernameIsNull();
        if (password == null) throw new PasswordIsNull();

        try {

            List<User> users = jdbcTemplate.query(
                    "SELECT id, username, password, permissions, lastname, firstname, cnp, phone FROM users WHERE username = ? AND password = ?",
                    new Object[]{username, password}, new UserRowMapper());
            return users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
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
     
     * @param password noua parola
     * @throws InvalidUserId  daca userId <= 0
     * @throws UserNotFound   daca user-ul nu exista in baza de date
     * @throws PasswordIsNull daca password == null
     */
    public void updatePassword(String username, String password) throws UsernameIsNull, UserNotFound, PasswordIsNull, UsersUsernameIsNull {
        if (username == null) throw new  UsernameIsNull();
        if (password == null) throw new PasswordIsNull();
        if (getUserByUsername(username) == null) throw new UserNotFound();
        
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
                ps.setString(1, password);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     
     * @param permissions noile permisiuni
     * @throws InvalidUserId daca userId <= 0
     * @throws UserNotFound  daca user-ul nu exista in baza de date
     */
    public void updatePermissions(String  username, long permissions) throws UsersUsernameIsNull, UserNotFound {
        if (username == null) throw new UsersUsernameIsNull();
        if (getUserByUsername(username) == null) throw new UserNotFound();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET permissions = ? WHERE username = ?");
                ps.setLong(1, permissions);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public User getUserById(int userId) throws InvalidUserId {
        if (userId <= 0) throw new InvalidUserId();

        try {
            List<User> users = jdbcTemplate.query("SELECT id, username,password,permissions,lastname,firstname,cnp,phone FROM users WHERE id = ?",
                    new Object[]{userId}, new UserRowMapper());
            return users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public List<User> getAllUsers(){
        try {
            return jdbcTemplate.query("SELECT id, username,password,permissions,lastname,firstname,cnp,phone FROM users",
                    new UserRowMapper());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    
    public User getUserByUsername(String username) throws UsersUsernameIsNull {
        if (username == null) throw new UsersUsernameIsNull();
        
        try {
            List<User> users = jdbcTemplate.query("SELECT id, username,password,permissions,lastname,firstname,cnp,phone FROM users WHERE username = ?",
                    new Object[]{username}, new UserRowMapper());
            return users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    
    private int insertAndGetId(User user) throws UserIsNull, UsersPasswordIsNull, UsersUsernameIsNull, UsersLastnameIsNull, UsersFirstnameIsNull, UsersCnpIsInvalid, UsersPhoneIsInvalid {
        if (user == null) throw new UserIsNull();
        if (user.getPassword() == null) throw new UsersPasswordIsNull();
        if (user.getUsername() == null) throw new UsersUsernameIsNull();
        if (user.getLastName() == null) throw new UsersLastnameIsNull();
        if (user.getFirstName() == null) throw new UsersFirstnameIsNull();
        if (user.getCnp() == null || user.getCnp().length() != 13) throw new UsersCnpIsInvalid();
        if (user.getPhone() == null || user.getPhone().length() != 10) throw new UsersPhoneIsInvalid();

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement(
                        "INSERT INTO users (username,password,permissions,lastname,firstname,cnp,phone) VALUES (?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setLong(3, user.getPermissions());
                ps.setString(4, user.getLastName());
                ps.setString(5,user.getFirstName());
                ps.setString(6, user.getCnp());
                ps.setString(7, user.getPhone());
                return ps;
            
            }, keyHolder);
            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    
    public void updateUser(String username, User user) throws UsernameIsNull, UsersUsernameIsNull, UserNotFound, PasswordIsNull, PhoneIsNull, LastnameIsNull, FirstnameIsNull, CnpIsNullOrInvalid {
        updatePassword(username, user.getPassword());
        updatePermissions(username, user.getPermissions());
        updateLastname(username, user.getLastName());
        updateFirstname(username, user.getFirstName());
        updateCnp(username, user.getCnp());
        updatePhone(username, user.getPhone());
    }

    private void updatePhone(String username, String phone) throws UsersUsernameIsNull, UsernameIsNull, UserNotFound, PhoneIsNull {
        if (username == null) throw new  UsernameIsNull();
        if (phone == null) throw new PhoneIsNull();
        if (getUserByUsername(username) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET phone = ? WHERE username = ?");
                ps.setString(1, phone);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void updateCnp(String username, String cnp) throws UsernameIsNull, CnpIsNullOrInvalid, UserNotFound, UsersUsernameIsNull {
        if (username == null) throw new  UsernameIsNull();
        if (cnp == null || cnp.length() != 13) throw new CnpIsNullOrInvalid();
        if (getUserByUsername(username) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET cnp = ? WHERE username = ?");
                ps.setString(1, cnp);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void updateFirstname(String username, String firstname) throws UsernameIsNull, UsersUsernameIsNull, UserNotFound, FirstnameIsNull {
        if (username == null) throw new  UsernameIsNull();
        if (firstname == null) throw new FirstnameIsNull();
        if (getUserByUsername(username) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET firstname = ? WHERE username = ?");
                ps.setString(1, firstname);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void updateLastname(String username, String lastname) throws UsernameIsNull, LastnameIsNull, UsersUsernameIsNull, UserNotFound {
        if (username == null) throw new  UsernameIsNull();
        if (lastname == null) throw new LastnameIsNull();
        if (getUserByUsername(username) == null) throw new UserNotFound();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps;
                ps = connection.prepareStatement("UPDATE users SET lastname = ? WHERE username = ?");
                ps.setString(1, lastname);
                ps.setString(2, username);
                return ps;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
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

    private class UsersLastnameIsNull extends Throwable {
    }

    private class UsersFirstnameIsNull extends Throwable {
    }

    private class UsersCnpIsInvalid extends Throwable {
    }

    private class UsersPhoneIsInvalid extends Throwable {
    }

    public class PhoneIsNull extends Throwable {
    }

    public class CnpIsNullOrInvalid extends Throwable {
    }

    public class FirstnameIsNull extends Throwable {
    }

    public class LastnameIsNull extends Throwable {
    }
}
