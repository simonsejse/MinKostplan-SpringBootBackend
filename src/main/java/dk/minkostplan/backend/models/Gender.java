package dk.minkostplan.backend.models;

public enum Gender {
    MALE("Mand"),
    FEMALE("Kvinde");

    private final String language;
    Gender(final String language){
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
