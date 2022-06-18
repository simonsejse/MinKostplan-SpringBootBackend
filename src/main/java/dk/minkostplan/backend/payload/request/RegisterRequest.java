package dk.minkostplan.backend.payload.request;

import dk.minkostplan.backend.constraints.EnumNamePattern;
import dk.minkostplan.backend.constraints.PasswordMatchConfirmPassword;
import dk.minkostplan.backend.models.ActivityState;
import dk.minkostplan.backend.models.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@PasswordMatchConfirmPassword
public class RegisterRequest {
    @Getter @Setter
    @Email
    @NotNull(message = "Du har ikke oplyst en e-mailadresse!")
    @NotBlank(message = "Du har ikke oplyst en e-mailadresse!")
    private String email;

    @Getter @Setter
    @NotNull(message = "Du har ikke oplyst et brugernavn!")
    @NotBlank(message = "Du har ikke oplyst et brugernavn!")
    private String username;

    @NotNull(message = "Du har ikke oplyst et kodeord!")
    @NotBlank(message = "Du har ikke oplyst et kodeord!")
    private String password;

    @NotNull(message = "Du har ikke oplyst et bekræftet kodeord!")
    @NotBlank(message = "Du har ikke oplyst et bekræftet kodeord!")
    private String confirmPassword;

    @Getter @Setter
    @NotNull
    @Min(value=30, message = "Du kan ikke være så tynd!")
    @Max(value=300, message = "Du kan ikke være så tyk!")
    private Integer weight;

    @Getter @Setter
    @NotNull
    @Min(value=150, message = "Du kan ikke være så lav!")
    @Max(value=300, message = "Du kan ikke være så høj!")
    private Integer height;

    @Getter @Setter
    @NotNull
    @EnumNamePattern(
        enumNames = "MALE|FEMALE",
        message = "Du har valgt et køn der ikke findes!"
    )
    private Gender gender;

    @Getter @Setter
    @NotNull
    @EnumNamePattern(
        enumNames = "EXTREMELY_ACTIVE|VERY_ACTIVE|MODERATELY_ACTIVE|SEDENTARY",
        message = "Aktivitetsniveauet findes ikke!"
    )
    private ActivityState activity;

    @Getter @Setter
    private LocalDate birthday;
}
