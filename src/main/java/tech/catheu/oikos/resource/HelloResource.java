package tech.catheu.oikos.resource;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("hello")
public class HelloResource {

  @Inject
  Template hello;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public TemplateInstance get(@QueryParam("name") String name) {
    return hello.data("name", name);
  }

  @GET
  @Path("json")
  @Produces(MediaType.APPLICATION_JSON)
  @PermitAll
  public Map<Object,Object> get() {
    return Map.of("Hey", "Man");
  }
}
