package dk.minkostplan.backend.interfaceprojections;

public interface DisplayRecipeProjection {
    Long getId();
    String getName();
    Float getCalories();
    Float getFat();
    Float getProtein();
    Float getCarbs();
    float getPricePerServing();
    Integer getReadyInMinutes();
    String getImage();
}
