package dk.minkostplan.backend.models.dtos;

import dk.minkostplan.backend.models.MeasureType;
import lombok.*;

public class MeasureDTO {
    /**
     * The actual name of the Enum constants
     */
    private String name;
    /**
     * The name of the Enum constant in danish
     */
    private String displayName;

    public MeasureDTO(MeasureType measureType){
        this.name = measureType.name();
        this.displayName = measureType.getName();
    }

    protected MeasureDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
