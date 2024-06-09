package tech.catheu.oikos.startup;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.Optional;
import javax.swing.text.html.Option;
import tech.catheu.oikos.entity.AppUser;

@Singleton
public class Startup {
  
  @Transactional
  public void loadUsers(@Observes StartupEvent evt) {
    final String adminUsername = Optional.ofNullable(System.getenv("ADMIN_USERNAME")).orElse("cyril");
    final String adminPassword = Optional.ofNullable(System.getenv("ADMIN_PASSWORD")).orElse("lebadboy");
    if (AppUser.findByName(adminUsername) == null) {
      AppUser.add(adminUsername, adminPassword, "admin"); 
    }
  }
}
