import java.io.Serializable;

@Table(name = "users")
class User implements Serializable {
    private static final long serialVersionUID = 965492277031384770L;

    @Column(name = "user_id")
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    String command = ""; // is used to send different commands to server ("signIn" or "signUp")


    User() {
    }

    User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }
}