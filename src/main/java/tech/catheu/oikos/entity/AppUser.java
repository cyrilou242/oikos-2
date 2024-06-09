package tech.catheu.oikos.entity;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import java.util.List;

@Entity
@UserDefinition
public class AppUser extends PanacheEntity {
  @Username
  public String username;
  @Password
  public String password;
  @Roles
  public String role;

  /**
   * Adds a new user to the database
   * @param username the username
   * @param password the unencrypted password (it is encrypted with bcrypt)
   * @param role the comma-separated roles
   */
  public static void add(String username, String password, String role) {
    if (findByName(username) != null) {
      throw new IllegalArgumentException(String.format("Username %s already taken", username));
    }
    final AppUser user = new AppUser();
    user.username = username;
    user.password = BcryptUtil.bcryptHash(password);
    user.role = role;
    user.persist();
  }
  
  public static AppUser findByName(String username) {
    final List<AppUser> users = find("username", username).list();
    return users.isEmpty() ? null: users.get(0);
  }
}
