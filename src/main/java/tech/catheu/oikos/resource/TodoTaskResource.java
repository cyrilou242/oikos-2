package tech.catheu.oikos.resource;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateData;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import javax.print.attribute.standard.Media;
import org.jboss.resteasy.reactive.RestForm;
import tech.catheu.oikos.entity.TodoTask;

@Path("tasks")
@TemplateData
@RolesAllowed("admin")
public class TodoTaskResource {

  // used as a way to pass constants easily
  public static class Constant {
    private static final Constant INSTANCE = new Constant();
    public static final String FORM_KEY_DESCRIPTION = "description";
    public static final String FORM_KEY_COMPLETED = "completed";
    public final String formKeyDescription = FORM_KEY_DESCRIPTION;
    public final String formKeyCompleted = FORM_KEY_COMPLETED;
    private Constant() {}
    
  }
  
  public record Content(List<TodoTask> tasks, Constant constants) {
  }
  
  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance overview(Content content);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getAll() {
    final List<TodoTask> tasks = TodoTask
        .listAll(Sort.descending("id"));
    return Templates.overview(new Content(tasks, Constant.INSTANCE));
  }
  
  @POST
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance addOne(
      // FIXME CYRIL should be the same as in CONSTANTS  
      @RestForm(Constant.FORM_KEY_DESCRIPTION) String description) {
    // FIXME CYRIL perform validation!
    final var newTask = new TodoTask();
    newTask.description = description;
    newTask.completed = false;
    // createTask contains the transaction
    TodoTask.createNew(newTask);
    return getAll();
  }
  
  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("delete-completed")
  public TemplateInstance deleteCompleted() {
    TodoTask.deleteCompleted();
    return getAll();
  }
  
  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("{taskId}/delete")
  public TemplateInstance deleteOne(final long taskId) {
    TodoTask.deleteById(taskId);
    return getAll();
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("{taskId}/toggle-completion")
  public TemplateInstance toggleOne(final long taskId) {
    TodoTask.toggleById(taskId);
    return getAll();
  }
}
