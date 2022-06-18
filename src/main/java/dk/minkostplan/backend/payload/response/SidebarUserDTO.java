package dk.minkostplan.backend.payload.response;

import dk.minkostplan.backend.interfaceprojections.UserSidebarProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class SidebarUserDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String activity;
    @Getter
    @Setter
    private String gender;
    @Getter
    @Setter
    private int weight;
    @Getter
    @Setter
    private int height;
    @Getter
    @Setter
    private LocalDate birthday;

    protected SidebarUserDTO() { }

    public SidebarUserDTO(UserSidebarProjection userSidebarProjectionByEmail) {
        this.id = userSidebarProjectionByEmail.getId();
        this.username = userSidebarProjectionByEmail.getUsername();
        this.activity = userSidebarProjectionByEmail.getActivityState().getLanguage();
        this.gender = userSidebarProjectionByEmail.getGenderState().getLanguage();
        this.weight = userSidebarProjectionByEmail.getWeight();
        this.height = userSidebarProjectionByEmail.getHeight();
        this.birthday = userSidebarProjectionByEmail.getBirthday();
    }

    @Override
    public String toString() {
        return "SidebarUserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", activity='" + activity + '\'' +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", birthday=" + birthday +
                '}';
    }

}
