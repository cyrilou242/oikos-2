package tech.catheu.oikos.resource;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import tech.catheu.oikos.entity.Recipe;
import tech.catheu.oikos.entity.Recipe.DifficultyLevel;
import tech.catheu.oikos.entity.Recipe.NutritionalType;
import tech.catheu.oikos.entity.Recipe.SeasonMonth;
import tech.catheu.oikos.entity.Recipe.ServiceType;
import tech.catheu.oikos.entity.Recipe.SpeedCategory;
import tech.catheu.oikos.entity.TodoTask;
import tech.catheu.oikos.resource.TodoTaskResource.Constant;
import tech.catheu.oikos.resource.TodoTaskResource.Content;

@Path("recipes")
@RolesAllowed("admin")
public class RecipeResource {

  // used as a way to pass constants easily
  public static class Constant {
    private static final Constant INSTANCE = new Constant();
    private Constant() {}
  }

  public record Content(List<Recipe> recipes, RecipeResource.Constant constants) {
  }

  @CheckedTemplate
  public static class Templates {
    public static native TemplateInstance overview(Content content);
  }
  
  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance getAll() {
    final Content content = new Content(Recipe.listAll(), Constant.INSTANCE);
    return Templates.overview(content);
  }
  
  @GET
  @Transactional
  @Path("load")
  public Response loadRecipes() {
    try (final InputStream t = getClass().getClassLoader().getResourceAsStream("recettes.tsv");
        BufferedReader TSVReader = new BufferedReader(new InputStreamReader(t))) {
      String line = null;
      boolean firstLineSkipped = false;
      while ((line = TSVReader.readLine()) != null) {
        if (!firstLineSkipped) {
          firstLineSkipped = true;
          continue;
        }
        final String[] lineItems = line.split("\t");
        final Recipe r = fromLine(lineItems);
        Recipe.persist(r);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return Response.ok().build();
  }

  private Recipe fromLine(final String[] lineItems) {
    // 0 name	1 description	2 service	
    // 3 nutritional_type	4 total_time_category	5 preparation_time_category	
    // 6 cooking_time_category	7 requires_watch	8 difficulty	
    // 9 is_warm	10 is_tupper_friendly	11 season_month
    final Recipe r = new Recipe();
    r.name = lineItems[0];
    if (lineItems.length <= 1) {
      return r;
    }
    r.description = lineItems[1];
    if (lineItems.length <= 2) {
      return r;
    }
    r.service = !lineItems[2].isEmpty() ? ServiceType.valueOf(clean(lineItems[2])) : null;
    if (lineItems.length <= 3) {
      return r;
    }
    r.nutritionalTypes = Arrays.stream(lineItems[3].split(","))
        .map(RecipeResource::clean)
        .map(NutritionalType::valueOf)
        .toList().toArray(new NutritionalType[]{});
    if (lineItems.length <= 4) {
      return r;
    }
    r.totalTimeCategory = SpeedCategory.valueOf(clean(lineItems[4]));
    r.preparationTimeCategory = SpeedCategory.valueOf(clean(lineItems[5]));
    r.cookingTimeCategory = SpeedCategory.valueOf(clean(lineItems[6]));
    r.requiresWatch = Boolean.parseBoolean(lineItems[7].trim().toLowerCase());
    r.difficulty = DifficultyLevel.valueOf(clean(lineItems[8]));
    r.isWarm = Boolean.parseBoolean(lineItems[9].trim().toLowerCase());
    r.isTupperFriendly = Boolean.parseBoolean(lineItems[10].trim().toLowerCase());
    if (lineItems.length >= 12) {
      r.seasonMonths = Arrays.stream(lineItems[11].split(","))
          .map(RecipeResource::clean)
          .map(SeasonMonth::valueOf)
          .toList().toArray(new SeasonMonth[]{});  
    }
    
    return r;
  }
  
  private static String clean(String s) {
    return s.trim()
        .replace("é", "e")
        .replace("û", "u")
        .replace("è", "e")
        .replace("ê", "e")
        .toUpperCase()
        .replace("Œ", "OE")
        ; // todo replace accents
  }
}
