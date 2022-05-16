package dk.minkostplan.backend.models;

public enum MeasureType {
    TSP("teske"),
    PIECES("styk(s)"),
    LITER("liter"),
    MILLILITER("milliliter"),
    GRAMS("gram"),
    KILOGRAMS("kilogram");

    private final String name;

    MeasureType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
