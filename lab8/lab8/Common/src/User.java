import java.io.Serializable;

class User implements Serializable {
    private static final long serialVersionUID = 965492277031384770L;

    String login;

    String password;

    String command = ""; // is used to send different commands to server ("LogIn" or "signUp")

    String token = "";


    User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    String getLogin() {
        return login;
    }
}