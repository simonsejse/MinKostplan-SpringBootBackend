package dk.minkostplan.backend.interfaceprojections;

public interface FoodProjection {
    Long getId();
    String getFoodType();
    String getName();
    float getKj();
    float getKcal();
    float getProtein();
    float getCarbs();
    float getFat();
    float getAddedSugars();
    float getFibers();
}
