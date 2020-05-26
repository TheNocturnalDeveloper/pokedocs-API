package nl.fhict.s4.pokedocs.dal;

import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;



@Entity
@Table(name = "pokedocs_user")
public class User extends PanacheEntity {

    @Column(name = "username", updatable = false, nullable = false, unique = true)
    public String username;

    @Column(name = "password", updatable = true, nullable = false)
    public String password;

    @Column(name = "role", updatable = true, nullable = false)
    public String role;


     /**
     * Adds a new user in the database
     * @param username the user name
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role the comma-separated roles
     */
    public static User add(final String username, final String password, final String role) { 
        //TODO: password hashing
        final User user = new User();
        user.username = username;
        user.password = password;
        user.role = role;
        user.persist();

        return user;
    }

    /**
     * Attempts to retrieve a user based on the username and password
     * @param username the user name
     * @param password the unencrypted password 
     * @return the user that matches the username and password
     */
    public static Optional<User> authenticate(final String username, final String password) {
        return User.find("username = ?1 AND password = ?2", username, password).firstResultOptional();
    }

}