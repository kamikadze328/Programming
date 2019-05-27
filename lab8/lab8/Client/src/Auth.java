import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.ResourceBundle;

class Auth {
    private Locale rulocale = new Locale("ru");
    private Locale sllocale = new Locale("sl");
    private Locale enlocale = new Locale("en", "AU");
    private Locale svlocale = new Locale("sv");
    private Locale zhlocale = new Locale("zh", "CN");
    private Locale chosenLocale = Locale.getDefault();
    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", chosenLocale, new UTF8Control());

    private JMenu language = new JMenu(bundle.getString("language"));
    private JLabel loginText = new JLabel(bundle.getString("login"));
    private JTextField loginField = new JTextField();
    private JLabel passwordText = new JLabel(bundle.getString("password"));
    private JPasswordField passwordField = new JPasswordField();
    private JFrame frame = new JFrame();
    private JLabel logTooShort = new JLabel(bundle.getString("logTooShort"));
    private JLabel passTooShort = new JLabel(bundle.getString("passTooShort"));
    private JLabel objection = new JLabel(bundle.getString("objection"));
    private JLabel serverUnavailable = new JLabel(bundle.getString("disconnected"));
    private JLabel userAlreadyExists = new JLabel(bundle.getString("userAlreadyExists"));
    private JButton LogInButton = new JButton(bundle.getString("logIn"));
    private JButton signUpButton = new JButton(bundle.getString("signUp"));
    private SocketAddress server = new InetSocketAddress("localhost", 5001);

    Auth() {
        loginText.setHorizontalAlignment(SwingConstants.CENTER);
        passwordText.setHorizontalAlignment(SwingConstants.CENTER);
        logTooShort.setForeground(Color.RED);
        passTooShort.setForeground(Color.RED);
        objection.setForeground(Color.RED);
        userAlreadyExists.setForeground(Color.RED);
        loginField.setPreferredSize(new Dimension(130, 20));
        passwordField.setPreferredSize(new Dimension(130, 20));

        Font font = new Font("Comic Sans MS", Font.PLAIN, 14);
        Font font1 = new Font("Comic Sans MS", Font.BOLD, 13);
        Font font2 = new Font("Times New Roman", Font.BOLD, 14);
        loginText.setFont(font);
        passwordText.setFont(font);
        LogInButton.setFont(font1);
        signUpButton.setFont(font1);
        passTooShort.setFont(font2);
        logTooShort.setFont(font2);
        userAlreadyExists.setFont(font2);
        objection.setFont(font2);
        serverUnavailable.setFont(font2);

        logTooShort.setVisible(false);
        passTooShort.setVisible(false);
        userAlreadyExists.setVisible(false);
        objection.setVisible(false);
        serverUnavailable.setVisible(false);

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

        LogInButton.addActionListener(args0 -> {
            User user = new User(loginField.getText(), new String(passwordField.getPassword()));
            logUserIn(user);
        });

        signUpButton.addActionListener(e -> {
            User user = new User(loginField.getText(), new String(passwordField.getPassword()));
            signUserUp(user);
        });

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill   = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 4, 4, 4);
        c.gridx = 0;
        c.gridy = 0;
        frame.add(loginText, c);
        c.gridx++;
        frame.add(loginField, c);
        c.gridx = 0;
        c.gridy++;
        frame.add(passwordText, c);
        c.gridx++;
        frame.add(passwordField, c);
        c.gridx = 0;
        c.gridy++;
        frame.add(signUpButton, c);
        c.gridx++;
        frame.add(LogInButton, c);
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        frame.add(logTooShort, c);
        frame.add(passTooShort, c);
        frame.add(userAlreadyExists, c);
        frame.add(objection, c);
        frame.add(serverUnavailable, c);

        frame.setTitle(bundle.getString("titleAuth"));
        frame.setSize(360, 240);
        frame.setLocationRelativeTo(null);
       // frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        work();
    }

    private void logUserIn(User user) {
        if (checkInput(user)) {
            try {
                SocketChannel sc = SocketChannel.open(server);
                ObjectOutputStream oos = new ObjectOutputStream(sc.socket().getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(sc.socket().getInputStream());
                user.command = "signIn";
                oos.writeObject(user);
                handleServerErrors((String) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                setErrorMessage(serverUnavailable);
            }
        }
    }

    private void signUserUp(User user) {
        if (checkInput(user)) {
            try {
                SocketChannel sc = SocketChannel.open(server);
                ObjectOutputStream oos = new ObjectOutputStream(sc.socket().getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(sc.socket().getInputStream());
                user.command = "signUp";
                oos.writeObject(user);
                handleServerErrors((String) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                setErrorMessage(serverUnavailable);
            }
        }
    }

    private boolean checkInput(User user) {
        if (new String(passwordField.getPassword()).length() <= 3) { //Can't check user's password because its encrypted
            setErrorMessage(passTooShort);
            return false;
        } else if (user.getLogin().length() <= 3) {
            setErrorMessage(logTooShort);
            return false;
        } else return true;
    }

    private void work() {
        frame.setVisible(false);
        frame.dispose();
        new MySwingWorker(chosenLocale).execute();
    }

    private void changeLanguage(Locale locale) {
        chosenLocale = locale;
        bundle = ResourceBundle.getBundle("bundle", locale, new UTF8Control());
        frame.setTitle(bundle.getString("titleAuth"));
        language.setText(bundle.getString("language"));
        loginText.setText(bundle.getString("login"));
        passwordText.setText(bundle.getString("password"));
        LogInButton.setText(bundle.getString("logIn"));
        signUpButton.setText(bundle.getString("signUp"));
        objection.setText(bundle.getString("objection"));
        logTooShort.setText(bundle.getString("logTooShort"));
        passTooShort.setText(bundle.getString("passTooShort"));
        userAlreadyExists.setText(bundle.getString("userAlreadyExists"));
        serverUnavailable.setText(bundle.getString("disconnected"));
    }

    private void handleServerErrors(String error) {
        switch (error) {
            case "":
                work();
                break;
            case "logTooShort":
                setErrorMessage(logTooShort);
                break;
            case "userAlreadyExists":
                setErrorMessage(userAlreadyExists);
                break;
            case "objection":
                setErrorMessage(objection);
                break;
        }
    }

    private void setErrorMessage(JLabel message) {
        logTooShort.setVisible(false);
        passTooShort.setVisible(false);
        userAlreadyExists.setVisible(false);
        objection.setVisible(false);
        serverUnavailable.setVisible(false);
        message.setVisible(true);
    }
}