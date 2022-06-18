package dk.minkostplan.backend.interfaceprojections;

import dk.minkostplan.backend.models.ActivityState;
import dk.minkostplan.backend.models.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

public interface UserSidebarProjection {
    Long getId();
    String getUsername();
    ActivityState getActivityState();
    Gender getGenderState();
    int getWeight();
    int getHeight();
    LocalDate getBirthday();
}
