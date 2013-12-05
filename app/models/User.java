/**
 *
 */
package models;


import com.clarkparsia.empire.annotation.Namespaces;
import com.clarkparsia.empire.annotation.RdfProperty;
import com.clarkparsia.empire.annotation.RdfsClass;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Liam
 */
@Namespaces({"frbr", "http://vocab.org/frbr/core#",
        "dc", "http://purl.org/dc/terms/",
        "cos", "http://circlesofsustainability.org/ontology#",
        "foaf", "http://xmlns.com/foaf/0.1/"})
@RdfsClass("cos:User")
@Entity
@Table(name = "COS_USERS")
@EntityListeners(CoSEntityListener.class)
public class User extends RdfModel {
    @Id
    public Long id;

    @RdfProperty("cos:hasName")
    public String name;

    @RdfProperty("cos:hasUsername")
    public String username;

    @RdfProperty("cos:hasPassword")
    public String password;

    public User() {
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    
    /**
     * Authenticate a User.
     */
    public static User authenticate(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return find.where()
            .eq("username", username)
            .eq("password", password)
            .findUnique();
    }


    /**
     * Retrieve a User from username.
     */
    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }


    // ORM methods

    public static Model.Finder<Long,User> find = new Model.Finder(
            Long.class, User.class
    );

    public static List<User> all() {
        return find.all();
    }

    public static void create(User task) {
        task.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

}
