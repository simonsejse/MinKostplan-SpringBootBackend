package dk.minkostplan.backend.utils;

public class Messaging {
    public static final String HTML_SIGNATURE =
            "<div>" +
            "  Med venlig hilsen," +
            "  <br />" +
            "  Simon" +
            "  <br />" +
            "  Udvikler" +
            " <br />" +
            " <img style=\"border-radius:5px;margin-top:´20px;\" src=\"https://i.imgur.com/FC4dhyt.png\" alt=\"picture\" />" +
            "  <br />" +
            "  www.min-kostplan.dk" +
            "</div>";

    public static final String RESET_CREDENTIALS_HTML =
            "<div style=\"background-color:#7E7E7E; color:white; padding:50px;\">" +
                "<h2>Årh nej! Du har glemt dit password, men bare rolig!</h2>" +
                "<h3>Reset dit kordord ved <a style=\"color:white;\" href=\"${url}\">at klikke på linket her</a>!</h3> " +
            "</div>";
}
