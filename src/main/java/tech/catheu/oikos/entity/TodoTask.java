package tech.catheu.oikos.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@Entity
public class TodoTask extends PanacheEntity {
  public String description;
  public boolean completed;
  
  
  @Transactional
  public static TodoTask createNew(TodoTask newTask) {
    TodoTask.persist(newTask);
    return  newTask;
  }
  
  @Transactional
  public static void deleteCompleted() {
    TodoTask.delete("completed", true);
  }
  
  @Transactional
  public static void deleteById(final Long taskId) {
    final TodoTask task = TodoTask.findById(taskId);
    if(task == null) {
      throw new NotFoundException();
    }
    task.delete();
  }

  @Transactional
  public static void toggleById(final Long taskId) {
    final TodoTask task = TodoTask.findById(taskId);
    if(task == null) {
      throw new NotFoundException();
    }
    task.completed = !task.completed;
  }
}
