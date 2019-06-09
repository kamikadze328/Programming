import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.ResourceBundle;

class Auth {
    private User currentUser;
    private Color color= null;
    private String login;
    private String token;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Locale rulocale = new Locale("ru");
    private Locale sllocale = new Locale("sl");
    private Locale enlocale = new Locale("en", "AU");
    private Locale svlocale = new Locale("sv");
    private Locale zhlocale = new Locale("zh", "CN");
    private Locale chosenLocale = Locale.getDefault();
    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", chosenLocale);

    private JLabel tokenText = new JLabel(bundle.getString("token"));
    private JMenu language = new JMenu(bundle.getString("language"));
    private JLabel loginText = new JLabel(bundle.getString("login"));
    private JTextField loginField = new JTextField();
    private JLabel passwordText = new JLabel(bundle.getString("password"));
    private JPasswordField passwordField = new JPasswordField();
    private JFrame frame = new JFrame();
    private JLabel logTooShort = new JLabel(bundle.getString("logTooShort"));
    private JLabel passTooShort = new JLabel(bundle.getString("passTooShort"));
    private JLabel serverUnavailable = new JLabel(bundle.getString("disconnected"));
    private JLabel userAlreadyExists = new JLabel(bundle.getString("userAlreadyExists"));
    private JLabel wrongEmail = new JLabel(bundle.getString("wrongEmail"));
    private JLabel deadToken = new JLabel(bundle.getString("deadToken"));
    private JLabel wrongToken = new JLabel(bundle.getString("wrongToken"));
    private JLabel signUpError = new JLabel(bundle.getString("signUpError"));
    private JLabel SQLException = new JLabel(bundle.getString("SQLException"));
    private JLabel incorrectCommand = new JLabel(bundle.getString("incorrectCommand"));
    private JLabel wait = new JLabel(bundle.getString("waiting"));
    private JLabel wrongPassword = new JLabel(bundle.getString("wrongPassword"));
    private JLabel userDoesntExist = new JLabel(bundle.getString("userDoesntExist"));

    private JButton logInButton = new JButton(bundle.getString("logIn"));
    private JButton signUpButton = new JButton(bundle.getString("signUp"));
    private JButton sendButton = new JButton(bundle.getString("send"));
    private JButton cancelButton = new JButton(bundle.getString("cancel"));

    private SocketAddress server = new InetSocketAddress("localhost", 5001);

    Auth() {
        loginText.setHorizontalAlignment(SwingConstants.CENTER);
        passwordText.setHorizontalAlignment(SwingConstants.CENTER);
        logTooShort.setForeground(Color.RED);
        passTooShort.setForeground(Color.RED);
        userAlreadyExists.setForeground(Color.RED);
        wrongEmail.setForeground(Color.RED);
        deadToken.setForeground(Color.RED);
        wrongToken.setForeground(Color.RED);
        wrongPassword.setForeground(Color.RED);
        userDoesntExist.setForeground(Color.RED);
        wait.setForeground(Color.BLACK);
        signUpError.setForeground(Color.BLACK);
        SQLException.setForeground(Color.BLACK);
        incorrectCommand.setForeground(Color.YELLOW);
        sendButton.setVisible(false);
        cancelButton.setVisible(false);
        loginField.setPreferredSize(new Dimension(130, 20));
        passwordField.setPreferredSize(new Dimension(130, 20));
        /*loginField.setMaximumSize(new Dimension(220, 20));
        loginField.setMinimumSize(new Dimension(220, 20));
        passwordField.setMaximumSize(new Dimension(220, 20));
        passwordField.setMinimumSize(new Dimension(220, 20));
        signUpButton.setMaximumSize(new Dimension(220, 20));
        signUpButton.setMinimumSize(new Dimension(220, 20));
        logInButton.setMaximumSize(new Dimension(20, 20));
        logInButton.setMinimumSize(new Dimension(20, 20));*/



        Font font = new Font("Comic Sans MS", Font.PLAIN, 14);
        Font font1 = new Font("Comic Sans MS", Font.BOLD, 13);
        Font font2 = new Font("Times New Roman", Font.BOLD, 14);

        loginText.setFont(font);
        passwordText.setFont(font);
        logInButton.setFont(font1);
        signUpButton.setFont(font1);
        cancelButton.setFont(font1);
        sendButton.setFont(font1);
        passTooShort.setFont(font2);
        logTooShort.setFont(font2);
        userAlreadyExists.setFont(font2);
        wrongEmail.setFont(font2);
        serverUnavailable.setFont(font2);
        deadToken.setFont(font2);
        wrongToken.setFont(font2);
        signUpError.setFont(font2);
        SQLException.setFont(font2);
        incorrectCommand.setFont(font2);
        tokenText.setFont(font);
        wait.setFont(font2);
        wrongPassword.setFont(font2);
        userDoesntExist.setFont(font2);

        userDoesntExist.setVisible(false);
        wrongPassword.setVisible(false);
        wait.setVisible(false);
        logTooShort.setVisible(false);
        passTooShort.setVisible(false);
        userAlreadyExists.setVisible(false);
        serverUnavailable.setVisible(false);
        wrongEmail.setVisible(false);
        deadToken.setVisible(false);
        wrongToken.setVisible(false);
        signUpError.setVisible(false);
        SQLException.setVisible(false);
        incorrectCommand.setVisible(false);
        sendButton.setVisible(false);
        cancelButton.setVisible(false);
        tokenText.setVisible(false);

        JMenuItem en_item = new JMenuItem("Australian");
        JMenuItem ru_item = new JMenuItem("Русский");
        JMenuItem sv_item = new JMenuItem("Slovenski");
        JMenuItem sl_item = new JMenuItem("Svenska");
        JMenuItem zh_item = new JMenuItem("中国");
        language.add(en_item);
        language.add(ru_item);
        language.add(sv_item);
        language.add(sl_item);
        language.add(zh_item);
        en_item.addActionListener(arg0 -> changeLanguage(enlocale));
        ru_item.addActionListener(arg0 -> changeLanguage(rulocale));
        sv_item.addActionListener(arg0 -> changeLanguage(svlocale));
        sl_item.addActionListener(arg0 -> changeLanguage(sllocale));
        zh_item.addActionListener(arg0 -> changeLanguage(zhlocale));
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(language);
        frame.setJMenuBar(menuBar);

        logInButton.addActionListener(args0 -> {
            setErrorMessage(wait);
            logInButton.setEnabled(false);
            signUpButton.setEnabled(false);
            loginField.setEditable(false);
            passwordField.setEditable(false);
            new Thread(() -> {
                User user = new User(loginField.getText(), new String(passwordField.getPassword()));
                logUserIn(user);
            }).start();
        });


        signUpButton.addActionListener(e -> {
            setErrorMessage(wait);
            logInButton.setEnabled(false);
            signUpButton.setEnabled(false);
            loginField.setEditable(false);
            passwordField.setEditable(false);
            new Thread(() -> {
                User user = new User(loginField.getText(), new String(passwordField.getPassword()));
                signUserUp(user);
            }).start();
        });

        cancelButton.addActionListener(args0 -> {
            setErrorMessage(wait);
            sendButton.setVisible(false);
            cancelButton.setVisible(false);
            tokenText.setVisible(false);
            sendButton.setVisible(false);
            cancelButton.setVisible(false);
            tokenText.setVisible(false);
            passwordField.setVisible(true);
            passwordText.setVisible(true);
            signUpButton.setVisible(true);
            logInButton.setVisible(true);
            loginText.setVisible(true);
            loginField.setText("");
            wait.setVisible(false);
        });

        sendButton.addActionListener(e -> {
            setErrorMessage(wait);
            /*cancelButton.setEnabled(false);
            sendButton.setEnabled(false);*/
            new Thread(() -> {
                currentUser.token = loginField.getText();
                sendToken(currentUser);
                wait.setVisible(false);
            }).start();
        });



        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 2, 5, 2);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        frame.add(tokenText, c);
        frame.add(loginText, c);
        c.gridx++;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        frame.add(loginField, c);
        c.gridx = 0;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        frame.add(passwordText, c);
        c.gridx++;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.EAST;
        frame.add(passwordField, c);
        c.gridx = 0;
        c.gridy++;
        frame.add(cancelButton, c);
        frame.add(signUpButton, c);
        c.gridx++;
        frame.add(logInButton, c);
        frame.add(sendButton, c);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridheight = 5;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        frame.add(logTooShort, c);
        frame.add(passTooShort, c);
        frame.add(userAlreadyExists, c);
        frame.add(serverUnavailable, c);
        frame.add(wrongEmail, c);
        frame.add(wrongToken, c);
        frame.add(deadToken, c);
        frame.add(signUpError);
        frame.add(SQLException, c);
        frame.add(incorrectCommand, c);
        frame.add(wait, c);
        frame.add(wrongPassword, c);
        frame.add(userDoesntExist, c);

        frame.setTitle(bundle.getString("titleAuth"));
        frame.setSize(360, 240);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void logUserIn(User user) {
        if (checkInput(user)) {
            try {
                SocketChannel sc = SocketChannel.open(server);
                oos = new ObjectOutputStream(sc.socket().getOutputStream());
                ois = new ObjectInputStream(sc.socket().getInputStream());
                user.command = "logIn";
                oos.writeObject(user);
                currentUser = user;
                handleServerCommands((String) ois.readObject());
                if(color!=null) handleServerCommands((String) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(serverUnavailable);
            }
        }
    }

    private void signUserUp(User user) {
        if (checkInput(user)) {
            try {
                SocketChannel sc = SocketChannel.open(server);
                oos = new ObjectOutputStream(sc.socket().getOutputStream());
                ois = new ObjectInputStream(sc.socket().getInputStream());
                user.command = "signUp";
                oos.writeObject(user);
                currentUser = user;
                handleServerCommands((String) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(serverUnavailable);
            }
        }
    }

    private void sendToken(User user) {
        try {
            try{
                this.oos.close();
                this.ois.close();
            }catch (IOException ignored){}
            SocketChannel sc = SocketChannel.open(server);
            oos = new ObjectOutputStream(sc.socket().getOutputStream());
            ois = new ObjectInputStream(sc.socket().getInputStream());
            user.command = "checkEmail";
            oos.writeObject(user);
            handleServerCommands((String) ois.readObject());
            if(color!=null) handleServerCommands((String) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            logInButton.setEnabled(true);
            signUpButton.setEnabled(true);
            loginField.setEditable(true);
            passwordField.setEditable(true);
            setErrorMessage(serverUnavailable);
        }
    }

    private boolean checkInput(User user) {
        if (new String(passwordField.getPassword()).length() <= 3) {
            logInButton.setEnabled(true);
            signUpButton.setEnabled(true);
            loginField.setEditable(true);
            passwordField.setEditable(true);
            setErrorMessage(passTooShort);
            return false;
        } else if (user.getLogin().length() <= 3) {
            logInButton.setEnabled(true);
            signUpButton.setEnabled(true);
            loginField.setEditable(true);
            passwordField.setEditable(true);
            setErrorMessage(logTooShort);
            return false;
        } else return true;
    }

    private void work() {
        frame.setVisible(false);
        frame.dispose();
        new MySwingWorker(chosenLocale, color, login, token).execute();
    }

    private void changeLanguage(Locale locale) {
        chosenLocale = locale;
        bundle = ResourceBundle.getBundle("bundle", locale);
        frame.setTitle(bundle.getString("titleAuth"));
        language.setText(bundle.getString("language"));
        loginText.setText(bundle.getString("login"));
        passwordText.setText(bundle.getString("password"));
        logInButton.setText(bundle.getString("logIn"));
        signUpButton.setText(bundle.getString("signUp"));
        logTooShort.setText(bundle.getString("logTooShort"));
        passTooShort.setText(bundle.getString("passTooShort"));
        wrongEmail.setText(bundle.getString("wrongEmail"));
        userAlreadyExists.setText(bundle.getString("userAlreadyExists"));
        serverUnavailable.setText(bundle.getString("disconnected"));
        tokenText.setText(bundle.getString("token"));
        deadToken.setText(bundle.getString("deadToken"));
        wrongToken.setText(bundle.getString("wrongToken"));
        signUpError.setText(bundle.getString("signUpError"));
        SQLException.setText(bundle.getString("SQLException"));
        incorrectCommand.setText(bundle.getString("incorrectCommand"));
        wait.setText(bundle.getString("waiting"));
        wrongPassword.setText(bundle.getString("wrongPassword"));
        userDoesntExist.setText(bundle.getString("userDoesntExist"));
    }

    private void handleServerCommands(String command) {
        switch (command) {
            case "TokenSent":
                wait.setVisible(false);
                checkEmail();
                break;
            case "LoginExists":
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(userAlreadyExists);
                break;
            case "LoginDoesntExist":
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(userDoesntExist);
                break;

            case "WrongEmailAddress":
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(wrongEmail);
                break;
            case "DeadToken":
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                cancelButton.doClick();
                setErrorMessage(deadToken);
                break;
            case "WrongToken":
                setErrorMessage(wrongToken);
                break;
            case "SignUpError":
                cancelButton.doClick();
                setErrorMessage(signUpError);
                break;
            case "WrongPassword":
                logInButton.setEnabled(true);
                signUpButton.setEnabled(true);
                loginField.setEditable(true);
                passwordField.setEditable(true);
                setErrorMessage(wrongPassword);
                break;


            case "SQLException":

                setErrorMessage(SQLException);
                break;
            case "IncorrectCommand":

                setErrorMessage(incorrectCommand);
                break;
        }
        if (command.contains("COLOR: ")) {
            color = new Color(Integer.parseInt(command.substring(7)));

        } else if (command.contains("TOKEN: ")) {
            token = command.substring(7);
            login = currentUser.login;
            try {
                oos.close();
                ois.close();
            } catch (IOException ignored) {
            }

            work();
        }
    }

    private void checkEmail() {
        sendButton.setVisible(true);
        cancelButton.setVisible(true);
        tokenText.setVisible(true);
        loginField.setEditable(true);
        passwordField.setVisible(false);
        passwordText.setVisible(false);
        signUpButton.setVisible(false);
        logInButton.setVisible(false);
        loginText.setVisible(false);
        loginField.setText("");
        passwordField.setText("");
    }


    private void setErrorMessage(JLabel message) {
        logTooShort.setVisible(false);
        passTooShort.setVisible(false);
        userAlreadyExists.setVisible(false);
        serverUnavailable.setVisible(false);
        wrongEmail.setVisible(false);
        deadToken.setVisible(false);
        wrongToken.setVisible(false);
        signUpError.setVisible(false);
        SQLException.setVisible(false);
        wait.setVisible(false);
        userDoesntExist.setVisible(false);
        incorrectCommand.setVisible(false);
        wrongPassword.setVisible(false);
        message.setVisible(true);
    }
}