package tech.catheu.oikos.entity;

import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.qute.TemplateExtension;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
public class Recipe extends PanacheEntity {

  public String name;
  public String description;

  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public SeasonMonth notUsed; // used to generate the DDL of the enum automatically - can be removed once flyway is introduced
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public NutritionalType notUsed2; // used to generate the DDL of the enum automatically  - can be removed once flyway is introduced

  @Type(
      value = EnumArrayType.class,
      parameters = @Parameter(
          name = AbstractArrayType.SQL_ARRAY_TYPE,
          value = "NutritionalType"
      )
  )
  @Column(
      name = "nutritionalTypes",
      columnDefinition = "NutritionalType[]"
  )
  public NutritionalType[] nutritionalTypes;
  
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public ServiceType service;

  @Type(
      value = EnumArrayType.class,
      parameters = @Parameter(
          name = AbstractArrayType.SQL_ARRAY_TYPE,
          value = "SeasonMonth"
      )
  )
  @Column(
      name = "seasonMonths",
      columnDefinition = "SeasonMonth[]"
  )
  public SeasonMonth[] seasonMonths;
  
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public SpeedCategory totalTimeCategory;
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public SpeedCategory preparationTimeCategory;
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public SpeedCategory cookingTimeCategory;
  public boolean requiresWatch;
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  public DifficultyLevel difficulty;
  public boolean isWarm;
  public boolean isTupperFriendly;

  public enum NutritionalType {
    COMPLET, LEGUME, VIANDE, POISSON, OEUF, FECULENT, LEGUMINEUSE
  }

  public enum ServiceType {
    ENTREE, PLAT, DESSERT
  }

  public enum SeasonMonth {
    JANVIER, FEVRIER, MARS, AVRIL, MAI, JUIN, JUILLET, AOUT, SEPTEMBRE, OCTOBRE, NOVEMBRE, DECEMBRE
  }

  public enum SpeedCategory {
    RAPIDE, MOYEN, LONG
  }

  public enum DifficultyLevel {
    FACILE, MOYEN, DIFFICILE
  }

  @TemplateExtension
  static class RecipeTemplateExtensions {
    static String commaSepNutritionTypes(Recipe recipe) {
      if (recipe.nutritionalTypes == null) {
        return "";
      }
      return Arrays.stream(recipe.nutritionalTypes)
          .map(Enum::name)
          .collect(Collectors.joining(","));
    }
  }
}
