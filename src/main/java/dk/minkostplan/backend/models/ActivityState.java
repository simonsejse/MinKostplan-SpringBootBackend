package dk.minkostplan.backend.models;

import dk.minkostplan.backend.constraints.EnumNamePattern;

public enum ActivityState {
    SEDENTARY("Stillesiddende", "stillesiddende arbejde samt ingen til næsten ingen fysisk aktivitet", 1.1d),
    MODERATELY_ACTIVE("Moderat aktiv","nogen aktivitet på enten arbejde eller i fritid", 1.4d),
    VERY_ACTIVE("Meget aktiv", "træning 3-5 gange ugentligt samt anden bevægelse", 1.7d),
    EXTREMELY_ACTIVE("Ekstrem aktiv", "træning min. 6 gange ugentligt samt anden bevægelse", 1.9d);

    private final String language;
    private final String info;
    private final double activityRate;

    ActivityState(String language, String info, double activityRate){
        this.language = language;
        this.info = info;
        this.activityRate = activityRate;
    }

    public String getLanguage() {
        return language;
    }

    public String getInfo() {
        return info;
    }

    public double getActivityRate() {
        return activityRate;
    }
}
