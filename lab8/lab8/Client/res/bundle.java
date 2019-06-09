import java.util.ListResourceBundle;

public class bundle extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"titleAuth", "Authorisation"},
            {"login", "Login"},
            {"password", "Password"},
            {"signUp", "Sign Up"},
            {"logIn", "Log In"},
            {"logTooShort", "Login must be at least 3 characters long"},
            {"passTooShort", "Password must be at least 3 characters long"},
            {"userAlreadyExists", "User already exists"},
            {"wrongEmail", "Wrong Email Address"},
            {"userDoesntExist", "User doesn't exist"},
            {"waiting", "Waiting..."},
            {"wrongPassword", "Wrong password"},
            {"token", "Token:"},
            {"deadToken", "Your time limit is over"},
            {"wrongToken", "Wrong Token"},
            {"signUpError", "Sign Up failed"},
            {"SQLException", "Data base is unavailable"},
            {"incorrectCommand", " Command is incorrect"},
            {"send", "Send"},
            {"title", "Creature Collection Viewer"},
            {"language", "Language"},
            {"isWorking", "isWorking"},
            {"exit", "exit"},
            {"info", "Creature info"},
            {"name", "Name"},
            {"family", "Family"},
            {"hunger", "Hunger"},
            {"location", "Location"},
            {"creationTime", "Creation time"},
            {"time", "Time"},
            {"inventory", "Inventory"},
            {"user", "User"},
            {"format", "format"},
            {"nameFilter", "Name starts with"},
            {"FootPath", "FootPath"},
            {"LightHouse", "Light House"},
            {"Hangar", "Hangar"},
            {"Hill", "Hill"},
            {"Yard", "Yard"},
            {"GroundFloor", "Ground Floor"},
            {"TopFloor", "Top Floor"},
            {"NaN", "NaN"},
            {"fernGreen", "Fern Green"},
            {"other", "Other"},
            {"black", "Black"},
            {"white", "White"},
            {"pareGold", "Pare Gold"},
            {"deepRed", "Deep Red"},
            {"purple", "Purple"},
            {"size", "Size"},
            {"change", "Change"},
            {"cancel", "Cancel"},
            {"refresh", "Refresh"},
            {"table", "Table"},
            {"graphics", "Graphics"},
            {"disconnected", "Server is unavailable"},
            {"connected", "Connection: true"},
            {"AddedSuccess", "Added Success"},
            {"RemovedSuccess", "Removed Success"},
            {"SavedSuccess", "Saved Success"},
            {"AddedFailing", "Added Failing"},
            {"RemovedFailingDontYours", "Creature don't yours"},
            {"RemovedFailingDontExists", ""},
            {"SavedFailing", "Saved failing"},
            {"loadFileError", "Load file failing"},
            {"JSONError", ""},
            {"greeting", "I wonder what will happen..."},
            {"newCreature", "New"},
            {"creatures", "creatures"},
            {"add", "Add"},
            {"add_if_max", "Add if Max"},
            {"remove", "Remove"},
            {"isEmpty", "field is empty!"},
            {"isnNumber", "value is not a number!"},
            {"isntPositive", "can be positive!"},
            {"isnChosen", "isn't chosen"},
            {"added", "Added"},
            {"tryAgain", "Server is unavailable, try again later"},
            {"incorrectDate", "Date format is"},
            {"filtersCorrect", "Filters are correct"},

            {"ChangedSuccess", "Changed Success"},
            {"ChangedFailing", "Changed Failing"},
            {"ChangedFailingDontYours","Changed Failing: Don't Yours"},
            {"windowWillBeClosed", "Dead Token. Window will be closed!"}
    };
}

/*
public class bundle_en_AU extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"s1", "Yes"},
            {"s2", "No"},
    };
}
public class bundle extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"s1", "Yes"},
            {"s2", "No"},
    };
}
public class bundle_sl extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"s1", "Yes"},
            {"s2", "No"},
    };
}

public class bundle_sv  extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"s1", "Yes"},
            {"s2", "No"},
    };
}

public class bundle_zh_CN extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {
            {"s1", "Yes"},
            {"s2", "No"},
    };
}

*/
