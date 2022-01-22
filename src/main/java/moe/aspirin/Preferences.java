package moe.aspirin;

//I just used Java specific feature for permanent save preferences without DB or anything else.
public class Preferences {
    private void preferenceSet(String key, String value) {                                              //Here is 3 types of Preferences the bot can save
        java.util.prefs.Preferences.userNodeForPackage(Main.class).put(key, String.valueOf(value));     //first one is "UsedTimes"
    }                                                                                                   //it means how much times someone requested something

    //also its String, cuz i use hacky method to cast "UsedTimes" to long
    private String preferenceGet(String key) {                                                          //but everything else as int. i must be retarded of this lol
        return java.util.prefs.Preferences.userNodeForPackage(Main.class).get(key, "0");
    }

    int settingsNSFWGet(Long chatID) {                                                                  //Second one is NSFW settings
        return Integer.parseInt(preferenceGet(String.valueOf(chatID)));                                 //The NSFW setting depends on chat where it was requested
    }                                                                                                   //1 is ON, 0 is off

    void settingsNSFWSet(Long chatID, int status) {
        preferenceSet(String.valueOf(chatID), String.valueOf(status));
    }

    //users without username considers as "null", so english only :(
    int settingsLanguageGet(String username) {                                                          //And last third is language settings
        if (username == null)
            return 0;                                                                                   //it uses telegram username to know what language to use
        return Integer.parseInt(preferenceGet(username));                                               //0 is english, 1 is whatever language you used in TextResponse class
    }

    //users without username considers as "null", so language change :(
    void settingsLanguageSet(String username, int status) {
        if (username == null) return;
        preferenceSet(username, String.valueOf(status));
    }

    String reqTimesGet() {
        return preferenceGet("reqTimes");
    }

    void reqTimesGet(long num) {
        preferenceSet("reqTimes", String.valueOf(num));
    }

    void reqTimesAdd(long num) {
        preferenceSet("reqTimes", String.valueOf(Long.parseLong(reqTimesGet()) + num));
    }
}



