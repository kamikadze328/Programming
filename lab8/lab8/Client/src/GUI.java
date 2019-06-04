import javax.swing.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Timer;
import java.awt.*;
import java.util.*;

class GUI extends JFrame {
    private List<Creature> creatureList = new ArrayList<>();
    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault(), new UTF8Control());
    static private ResourceBundle staticBundle = ResourceBundle.getBundle("bundle", Locale.getDefault(), new UTF8Control());
    private Locale rulocale = new Locale("ru");
    private Locale sllocale = new Locale("sl");
    private Locale enlocale = new Locale("en", "AU");
    private Locale svlocale = new Locale("sv");
    private Locale zhlocale = new Locale("zh", "CN");
    private JMenu language = new JMenu(bundle.getString("language"));


    private static JLabel connectionText = new JLabel();
    private static boolean isConnected = false;
    private JPanel p3 = new JPanel();
    private ArrayList<Timer> timers;
    private JLabel infoObjectText = new JLabel(bundle.getString("info") + ":");
    private JLabel nameText = new JLabel(bundle.getString("name") + ":");
    private JLabel familyText = new JLabel(bundle.getString("family") + ":");
    private JLabel hungerText = new JLabel(bundle.getString("hunger") + ":");
    private JLabel locationText = new JLabel(bundle.getString("location") + ":");
    private JLabel timeText = new JLabel(bundle.getString("creationTime") + ":");
    private JLabel inventoryText = new JLabel(bundle.getString("inventory") + ":");
    private JTextField xValue = new JTextField();
    private JTextField yValue = new JTextField();
    private JTextField sizeValue = new JTextField();

    private JTextField nameValue = new JTextField();
    private JTextField familyValue = new JTextField();
    private JTextField hungerValue = new JTextField();
    private JTextField locationValue = new JTextField();
    private JTextField timeValue = new JTextField();
    private JTextField inventoryValue = new JTextField();

    Sender sender;
    private Color color;

    private JButton refreshButton = new JButton(bundle.getString("refresh"));
    private JLabel infoText = new JLabel(" " + bundle.getString("greeting"));
    private int filtersTextNumber = 0;
    private String topFloorComboBox = bundle.getString("TopFloor");
    private String groundFloorComboBox = bundle.getString("GroungFloor");
    private String yardComboBox = bundle.getString("Yard");
    private String hillComboBox =bundle.getString("Hill");
    private String hangarComboBox = bundle.getString("Hangar");
    private String footPathComboBox = bundle.getString("FootPath");
    private String lightHouseComboBox = bundle.getString("LightHouse");
    private String nanComboBox = bundle.getString("NaN");
    private JLabel nameStarts = new JLabel(bundle.getString("nameFilter"));
    private JLabel nameFromTo = new JLabel(bundle.getString("name"));
    private JTextField nameTo = new JTextField();
    private JLabel familyFromTo = new JLabel(bundle.getString("family"));
    private JTextField familyTo = new JTextField();
    private JLabel timeFromTo = new JLabel(bundle.getString("time"));
    private JTextField timeFrom = new JTextField();
    private JTextField timeTo = new JTextField();
    private JLabel locationFromTo = new JLabel(bundle.getString("location"));
    private JLabel infoConnectionText = new JLabel("<html><h1 align=\"center\">ВЫ никуда не подключены<br>LOL</h1></html>");
    private JLabel loginInfo;

    private JLabel hungerFromTo = new JLabel(bundle.getString("hunger"));
    private JTextField hungerTo = new JTextField();
    private JLabel X = new JLabel("X:");
    private JLabel Y = new JLabel("Y:");
    private JLabel size = new JLabel(bundle.getString("size")+  ":");
    private JLabel sizeText = new JLabel(bundle.getString("size")+  ":");

    private JSlider xFromSlider = new JSlider();
    private JSlider yFromSlider = new JSlider();
    private JSlider sizeFromSlider = new JSlider();


    private JButton changeButton = new JButton(bundle.getString("change"));
    private JButton newCreatureButton = new JButton(bundle.getString("newCreature"));
    private JButton addButton = new JButton(bundle.getString("add"));
    private JButton addIfMaxButton = new JButton(bundle.getString("add_if_max"));
    private JButton removeButton = new JButton(bundle.getString("remove"));
    private JButton exitButton = new JButton(bundle.getString("exit"));


    private JButton cancelButton = new JButton(bundle.getString("cancel"));

    private JComboBox<String> locationBox;

    private DateTimeFormatter displayDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.MEDIUM).withLocale(Locale.getDefault());
    private Creature chosenCreature;
    private DateTimeFormatter filterDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");

    GUI(Locale locale, Color color, String login) {
        loginInfo = new JLabel(bundle.getString("user") + ": " + login);
        this.color = color;
        changeLanguage(locale);
        nameValue.setEditable(false);
        familyValue.setEditable(false);
        hungerValue.setEditable(false);
        locationValue.setEditable(false);
        locationValue.setEnabled(false);
        timeValue.setEditable(false);
        inventoryValue.setEditable(false);
        xValue.setEditable(false);
        yValue.setEditable(false);
        sizeValue.setEditable(false);
        JLabel xText = new JLabel("X:");
        JLabel yText = new JLabel("Y:");

        JMenuBar menuBar = new JMenuBar();
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
        menuBar.add(language);
        setJMenuBar(menuBar);

        //Right panel elements
        Font font = new Font("Sans-Serif", Font.PLAIN, 16);
        Font font1 = new Font("Calibri", Font.BOLD, 16);

        infoObjectText.setFont(font1);
        nameText.setFont(font1);
        familyText.setFont(font1);
        hungerText.setFont(font1);
        timeText.setFont(font1);
        locationText.setFont(font1);
        inventoryText.setFont(font1);
        xValue.setFont(font1);
        yValue.setFont(font1);
        sizeValue.setFont(font1);
        yText.setFont(font1);
        xText.setFont(font1);
        sizeText.setFont(font1);
        loginInfo.setFont(font1);

        connectionText.setBackground(Color.BLACK);
        connectionText.setOpaque(true);
        connectionText.setText("");
        connectionText.setBackground(Color.BLACK);
        connectionText.setFont(font);
        infoText.setBackground(Color.BLACK);
        infoText.setOpaque(true);
        infoText.setFont(font);
        infoText.setForeground(Color.GREEN);
        refreshButton.addActionListener(arg0 ->{
            printText("", false);
            new Thread(sender::getCollection).start();
        });

        timeFrom.setToolTipText(bundle.getString("format") + ": dd.MM.yyyy HH:mm:ss");
        timeTo.setToolTipText(bundle.getString("format") + ": dd.MM.yyyy HH:mm:ss");
        xFromSlider.setMinimum(-1000);
        xFromSlider.setMaximum(1000);
        xFromSlider.setValue(-1000);
        JLabel xFrom = new JLabel("-1000");
        xFromSlider.addChangeListener(e -> {
            xFrom.setText(Integer.toString(xFromSlider.getValue()));
            printText("", false);
        });
        yFromSlider.setMinimum(-1000);
        yFromSlider.setMaximum(1000);
        yFromSlider.setValue(-1000);
        JLabel yFrom = new JLabel("-1000");
        yFromSlider.addChangeListener(e ->
        {
            yFrom.setText(Integer.toString(yFromSlider.getValue()));
            printText("", false);
        });
        sizeFromSlider.setMinimum(10);
        sizeFromSlider.setMaximum(99);
        sizeFromSlider.setValue(35);
        JLabel sizeFrom = new JLabel("35");
        sizeFromSlider.addChangeListener(e -> {sizeFrom.setText(Integer.toString(sizeFromSlider.getValue()));
            printText("", false);
        });

        xFromSlider.setEnabled(false);
        yFromSlider.setEnabled(false);
        sizeFromSlider.setEnabled(false);
        newCreatureButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(this::newCreature).start();});
        addButton.setEnabled(false);
        addButton.addActionListener(args0 -> {
            printText("", false);
            checkInput("add");
        });
        addIfMaxButton.setEnabled(false);
        addIfMaxButton.addActionListener(args0 -> {
            printText("", false);
            checkInput("add_if_max");
        });
        removeButton.setEnabled(false);
        addIfMaxButton.addActionListener(args0 -> {
            printText("", false);
            checkInput("remove");
        });
        changeButton.setEnabled(false);
        changeButton.addActionListener(args0 -> {
            printText("", false);

            new Thread(this::checkFilters).start();
        });
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(this::cancel).start();
        });
        exitButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(this::exit).start();
        });

        String[] locationArrange = {
                "",
                topFloorComboBox,
                groundFloorComboBox,
                yardComboBox,
                hillComboBox,
                hangarComboBox,
                footPathComboBox,
                lightHouseComboBox,
                nanComboBox
        };
        locationBox = new JComboBox<>(locationArrange);
        locationBox.setEditable(false);
        locationBox.setEnabled(false);
        JComboBox<String> locationComboBox = new JComboBox<>(locationArrange);
        locationComboBox.setEditable(false);

        //Panels
        JPanel p17 = new JPanel();
        p17.setMaximumSize(new Dimension(500, 10));
        p17.setLayout(new GridLayout());
        p17.add(connectionText);
        JPanel refreshButtonPanel = new JPanel();
        refreshButtonPanel.setLayout(new BoxLayout(refreshButtonPanel, BoxLayout.X_AXIS));
        refreshButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        refreshButtonPanel.add(refreshButton);
        p17.add(refreshButton);

        JPanel p16 = new JPanel();
        p16.setLayout(new BorderLayout());
        p16.setMaximumSize(new Dimension(500, 50));
        p16.setPreferredSize(new Dimension(500, 50));
        p16.add(infoText);

        JPanel p15 = new JPanel();
        p15.setLayout(new BoxLayout(p15, BoxLayout.X_AXIS));
        p15.add(infoConnectionText);

        JPanel p13 = new JPanel();
        p13.setLayout(new BoxLayout(p13, BoxLayout.X_AXIS));
        p13.add(Box.createRigidArea(new Dimension(0, 70)));
        p13.add(infoObjectText);

        JPanel p12 = new JPanel();
        p12.setLayout(new BoxLayout(p12, BoxLayout.X_AXIS));
        nameValue.setPreferredSize(new Dimension(190, 30));
        nameValue.setMaximumSize(new Dimension(190, 30));
        p12.add(Box.createRigidArea(new Dimension(7, 0)));
        p12.add(nameText, Component.LEFT_ALIGNMENT);
        p12.add(Box.createHorizontalGlue());
        p12.add(nameValue, Component.RIGHT_ALIGNMENT);

        JPanel p11 = new JPanel();
        p11.setLayout(new BoxLayout(p11, BoxLayout.X_AXIS));
        familyValue.setPreferredSize(new Dimension(190, 30));
        familyValue.setMaximumSize(new Dimension(190, 30));
        p11.add(Box.createRigidArea(new Dimension(7, 0)));
        p11.add(familyText, Component.LEFT_ALIGNMENT);
        p11.add(Box.createHorizontalGlue());
        p11.add(familyValue, Component.RIGHT_ALIGNMENT);

        JPanel p111 = new JPanel();
        p111.setLayout(new BoxLayout(p111, BoxLayout.X_AXIS));
        hungerValue.setPreferredSize(new Dimension(190, 30));
        hungerValue.setMaximumSize(new Dimension(190, 30));
        p111.add(Box.createRigidArea(new Dimension(7, 0)));
        p111.add(hungerText, Component.LEFT_ALIGNMENT);
        p111.add(Box.createHorizontalGlue());
        p111.add(hungerValue, Component.RIGHT_ALIGNMENT);

        JPanel p10 = new JPanel();
        p10.setLayout(new BoxLayout(p10, BoxLayout.X_AXIS));
        timeValue.setPreferredSize(new Dimension(190, 30));
        timeValue.setMaximumSize(new Dimension(190, 30));
        p10.add(Box.createRigidArea(new Dimension(7, 0)));
        p10.add(timeText, Component.LEFT_ALIGNMENT);
        p10.add(Box.createHorizontalGlue());
        p10.add(timeValue, Component.RIGHT_ALIGNMENT);

        JPanel p9 = new JPanel();
        p9.setLayout(new BoxLayout(p9, BoxLayout.X_AXIS));
        locationBox.setPreferredSize(new Dimension(190, 30));
        locationBox.setMaximumSize(new Dimension(190, 30));
        p9.add(Box.createRigidArea(new Dimension(7, 0)));
        p9.add(locationText, Component.LEFT_ALIGNMENT);
        p9.add(Box.createHorizontalGlue());
        p9.add(locationBox, Component.RIGHT_ALIGNMENT);

        JPanel p999 = new JPanel();
        p999.setLayout(new BoxLayout(p999, BoxLayout.X_AXIS));
        p999.add(Box.createRigidArea(new Dimension(7, 0)));
        p999.add(inventoryText, Component.LEFT_ALIGNMENT);
        p999.add(Box.createHorizontalGlue());
        inventoryValue.setPreferredSize(new Dimension(190, 30));
        inventoryValue.setMaximumSize(new Dimension(190, 30));
        p999.add(inventoryValue, Component.RIGHT_ALIGNMENT);


        JPanel p99 = new JPanel();
        p99.setLayout(new BoxLayout(p99, BoxLayout.X_AXIS));
        xValue.setMaximumSize(new Dimension(300, 100));
        yValue.setMaximumSize(new Dimension(300, 100));
        sizeValue.setMaximumSize(new Dimension(300, 100));

        p99.add(Box.createRigidArea(new Dimension(7, 0)));
        p99.add(xText);
        p99.add(Box.createRigidArea(new Dimension(7, 0)));
        p99.add(xValue);
        p99.add(Box.createRigidArea(new Dimension(20, 0)));
        p99.add(yText);
        p99.add(Box.createRigidArea(new Dimension(7, 0)));
        p99.add(yValue);
        p99.add(Box.createRigidArea(new Dimension(20, 0)));
        p99.add(sizeText);
        p99.add(Box.createRigidArea(new Dimension(7, 0)));
        p99.add(sizeValue);

        JPanel p8 = new JPanel();
        p8.setLayout(new BoxLayout(p8, BoxLayout.X_AXIS));
        xFromSlider.setMaximumSize(new Dimension(150, 17));
        p8.add(X, Component.LEFT_ALIGNMENT);
        p8.add(Box.createRigidArea(new Dimension(100, 10)));
        p8.add(xFromSlider);
        p8.add(Box.createHorizontalGlue());
        p8.add(xFrom, Component.RIGHT_ALIGNMENT);

        JPanel p7 = new JPanel();
        p7.setLayout(new BoxLayout(p7, BoxLayout.X_AXIS));
        yFromSlider.setMaximumSize(new Dimension(150, 17));
        p7.add(Y, Component.LEFT_ALIGNMENT);
        p7.add(Box.createRigidArea(new Dimension(100, 10)));
        p7.add(yFromSlider);
        p7.add(Box.createHorizontalGlue());
        p7.add(yFrom, Component.RIGHT_ALIGNMENT);

        JPanel p6 = new JPanel();
        p6.setLayout(new BoxLayout(p6, BoxLayout.X_AXIS));
        sizeFromSlider.setMaximumSize(new Dimension(250, 17));
        p6.add(size, Component.LEFT_ALIGNMENT);
        p6.add(Box.createHorizontalGlue());
        p6.add(sizeFromSlider);
        p6.add(Box.createRigidArea(new Dimension(25, 0)));
        p6.add(sizeFrom, Component.RIGHT_ALIGNMENT);

        JPanel p5 = new JPanel();
        p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
        p5.add(changeButton);
        p5.add(Box.createRigidArea(new Dimension(15, 0)));
        p5.add(newCreatureButton);
        p5.add(Box.createRigidArea(new Dimension(15, 0)));
        p5.add(cancelButton);


        JPanel p55 = new JPanel();
        p55.setLayout(new BoxLayout(p55, BoxLayout.X_AXIS));
        p55.add(addButton);
        p55.add(Box.createRigidArea(new Dimension(15, 0)));
        p55.add(addIfMaxButton);
        p55.add(Box.createRigidArea(new Dimension(15, 0)));
        p55.add(removeButton);


        JPanel p4 = new JPanel();
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        p4.setPreferredSize(new Dimension(350, 1000));
        p4.setMaximumSize(new Dimension(350, 1000));
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p17);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p16);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p15);
        p4.add(Box.createVerticalGlue());
        p4.add(p13);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p12);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p11);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p111);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p10);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p9);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p999);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p99);
        p4.add(Box.createRigidArea(new Dimension(0, 30)));
        p4.add(p8);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p7);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p6);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p5);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p55);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel p4extended = new JPanel();
        p4extended.setLayout(new BoxLayout(p4extended, BoxLayout.X_AXIS));
        p4extended.add(Box.createRigidArea(new Dimension(15, 0)));
        p4extended.add(p4);
        p4extended.add(Box.createRigidArea(new Dimension(10, 0)));
        p4extended.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK));

        p3.setLayout(null);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(nameFromTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        nameTo.setMaximumSize(new Dimension(90, 50));
        nameTo.setPreferredSize(new Dimension(90, 30));
        p2.add(nameTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(familyFromTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        familyTo.setMaximumSize(new Dimension(60, 50));
        familyTo.setPreferredSize(new Dimension(60, 30));
        p2.add(familyTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(hungerFromTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        hungerTo.setMaximumSize(new Dimension(25, 50));
        hungerTo.setPreferredSize(new Dimension(25, 30));
        p2.add(hungerTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(timeFromTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        timeTo.setMaximumSize(new Dimension(120, 50));
        timeTo.setPreferredSize(new Dimension(120, 30));
        p2.add(timeTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(locationFromTo);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        locationComboBox.setMaximumSize(new Dimension(100, 50));
        locationComboBox.setPreferredSize(new Dimension(100, 30));
        p2.add(locationComboBox);
        p2.add(Box.createRigidArea(new Dimension(100, 0)));
        p2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JPanel p3extended = new JPanel();
        p3extended.setLayout(new BorderLayout());
        p3extended.add(p2, BorderLayout.NORTH);
        p3extended.add(p3, BorderLayout.CENTER);


        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.X_AXIS));
        exitButton.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        loginInfo.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        userInfo.add(Box.createHorizontalGlue());
        userInfo.add(loginInfo);
        userInfo.add(Box.createRigidArea(new Dimension(8,0)));
        userInfo.add(exitButton);
        userInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JPanel pInfoExtended = new JPanel();
        pInfoExtended.setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        add(userInfo, BorderLayout.NORTH);
        add(p3extended, BorderLayout.CENTER);
        add(p4extended, BorderLayout.EAST);

        setTitle(bundle.getString("title"));
        setSize(1500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void exit() {
        this.setVisible(false);
        sender.exit();
        sender=null;
        this.dispose();
        new Auth();
    }

    static void setConnectionInfo(boolean isWorking) {
        isConnected = isWorking;
        if (isWorking) {
            connectionText.setForeground(Color.GREEN);
            connectionText.setText(" " + staticBundle.getString("connected"));
        } else {
            connectionText.setForeground(Color.RED);
            connectionText.setText(" " + staticBundle.getString("disconnected"));
        }
    }

    private void setTopPanelInfo(Creature creature) {
        chosenCreature = creature;
        nameValue.setText(creature.getName());
        familyValue.setText(creature.getFamily());
        hungerValue.setText(String.valueOf(creature.getHunger()));
        locationValue.setText(bundle.getString(creature.getLocation().name().toLowerCase()));
        timeValue.setText(displayDateTimeFormatter.format(creature.getCreationTime()));
        inventoryValue.setText(creature.getInventory().toString().substring(1, creature.getInventory().size()-1));
    }

    private void checkFilters() {
        changeButton.setEnabled(false);
        /*if ((notANumeric(sizeFrom.getText()) && !sizeFrom.getText().isEmpty()) || (notANumeric(sizeTo.getText()) && !sizeTo.getText().isEmpty())) {
            printText(bundle.getString("incorrectSize"), true);
            filtersTextNumber = 1;
            changeButton.setEnabled(true);
        } else *//*if ((notANumeric(powerFrom.getText()) && !powerFrom.getText().isEmpty()) || (notANumeric(powerTo.getText()) && !powerTo.getText().isEmpty())) {
            printText(bundle.getString("incorrectPower"), true);
            filtersTextNumber = 2;
            changeButton.setEnabled(true);
        } else {*/
        try {
            if (!timeFrom.getText().isEmpty()) {
                filterDateTimeFormatter.parse(timeFrom.getText());
            }
            if (!timeTo.getText().isEmpty()) {
                filterDateTimeFormatter.parse(timeTo.getText());
            }
            printText(bundle.getString("filtersCorrect"), false);
            filtersTextNumber = 4;
            applyFilters();
        } catch (DateTimeParseException e) {
            printText(bundle.getString("incorrectDate") + " dd.MM.yyyy HH:mm:ss!", true);
            filtersTextNumber = 3;
            changeButton.setEnabled(true);
        }
        //  }
    }

    private boolean notANumeric(String str) {
        return !str.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
    }

    void printTextToConsole(String message, boolean isError) {
        if (isError) infoText.setForeground(Color.RED);
        else infoText.setForeground(Color.GREEN);
        infoText.setText(bundle.getString(message));
    }

    private void printText(String message, boolean isError) {
        if (isError) infoText.setForeground(Color.RED);
        else infoText.setForeground(Color.GREEN);
        infoText.setText(" " + message);
    }

    void printTextToConsole(String message, int i) {
        infoText.setForeground(Color.GREEN);
        infoText.setText(bundle.getString(message) + " "+ bundle.getString("creatures") +": " + i);
    }

    private void applyFilters() {
        /*filteredCircles = new ArrayList<>();
        Arrays.stream(circleList)
                .filter(o -> o.creature.getHunger() >= hungerFromSlider.getValue() && o.creature.getHunger() <= hungerToSlider.getValue())
                .filter(o -> o.creature.getX() >= xFromSlider.getValue() && o.creature.getX() <= xToSlider.getValue())
                .filter(o -> {
                    if (dateFrom.getText().isEmpty() || o.creature.getCreationDate().isAfter(LocalDateTime.parse(dateFrom.getText(), filterDateTimeFormatter))) {
                        return dateTo.getText().isEmpty() || o.creature.getCreationDate().isBefore(LocalDateTime.parse(dateTo.getText(), filterDateTimeFormatter));
                    }
                    return false;
                })
                .filter(o -> powerFrom.getText().isEmpty() || o.creature.getPower() >= Integer.parseInt(powerFrom.getText()))
                .filter(o -> powerTo.getText().isEmpty() || o.creature.getPower() <= Integer.parseInt(powerTo.getText()))
                .filter(o -> sizeFrom.getText().isEmpty() || o.creature.getSize() >= Double.parseDouble(sizeFrom.getText()))
                .filter(o -> sizeTo.getText().isEmpty() || o.creature.getSize() <= Double.parseDouble(sizeTo.getText()))
                .filter(o -> o.creature.getName().startsWith(nameField.getText()))
                .filter(o -> o.creature.getColor().equals(Colors.Blue) && blueCheckBox.isSelected() ||
                        o.creature.getColor().equals(Colors.Sapphire) && sapphireCheckBox.isSelected() ||
                        o.creature.getColor().equals(Colors.Navy) && navyCheckBox.isSelected() ||
                        o.creature.getColor().equals(Colors.Cyan) && cyanCheckBox.isSelected() ||
                        o.creature.getColor().equals(Colors.Mint) && mintCheckBox.isSelected() ||
                        o.creature.getColor().equals(Colors.Emerald) && emeraldCheckBox.isSelected())
                .forEach(filteredCircles::add);

        if (filteredCircles.size() == 0) {
            changeButton.setEnabled(true);
            printText(bundle.getString("noObjectsFound"), false);
            filtersTextNumber = 5;
        } else {
            cancelButton.setEnabled(true);
            printText(filteredCircles.size() + " " + bundle.getString("objectsFound"), false);
            filtersTextNumber = 6;
            timers = new ArrayList<>();
            for (Circle circle : filteredCircles) {
                timers.add(new Timer());
                timers.get(timers.size() - 1).schedule(new TimerTask() {
                    int counter = 0;

                    @Override
                    public void run() {
                        circle.transition();
                        p3.revalidate();
                        p3.repaint();
                        counter++;
                        if (counter == 70) {
                            stopAnimation();
                        }
                    }
                }, 100, 100);
            }
        }*/
    }

    private void stopAnimation() {
        for (Timer timer : timers) {
            timer.cancel();
        }
        /*for (Circle c : filteredCircles) {
            c.setNormalColor();
        }*/
        p3.revalidate();
        p3.repaint();
        changeButton.setEnabled(true);
        cancelButton.setEnabled(false);
    }

    void refreshCollection(List<Creature> tempCr) {
        List<Creature> newCreature = new ArrayList<>(tempCr);
        List<Creature> changedCr = new ArrayList<>();
        for (Creature creatureTemp : tempCr) {
            for (Creature creature : creatureList) {
                if (creature.equals(creatureTemp)) {
                    creatureList.remove(creature);
                    tempCr.remove(creatureTemp);
                    if (!creature.equalsAll(creatureTemp))
                        changedCr.add(creatureTemp);
                    break;
                }
            }
        }
        changedCr.addAll(tempCr);
        creatureList = newCreature;
            /*circleList = new Circle[creatureList.size()];
            p3.removeAll();
            for (int i = 0; i < creatureList.size(); i++) {
                Circle c = new Circle(creatureList.get(i), this);
                circleList[i] = c;
                p3.add(c);
            }*/
            /*printText(bundle.getString("acquired") + " " + creatureList.size() + " " + bundle.getString("elements"), false);
            filtersTextNumber = 7;
            p3.revalidate();
            p3.repaint();
            changeButton.setEnabled(true);
            cancelButton.setEnabled(false);
        } else {
            printText(bundle.getString("tryAgain"), true);
            filtersTextNumber = 8;*/
    }


    private void firstTimeRefresh() {
        while (true) {
//            List<Creature> tempCreatureList = sender.getCollection();
//            if (tempCreatureList != null) {
                /*creatureList = tempCreatureList;
                circleList = new Circle[creatureList.size()];
                for (int i = 0; i < creatureList.size(); i++) {
                    Circle c = new Circle(creatureList.get(i), this);
                    circleList[i] = c;
                    p3.add(c);
                }*/
                p3.revalidate();
                p3.repaint();
                break;
            }
        }


    private void changeLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("Bundle", locale, new UTF8Control());
        staticBundle = ResourceBundle.getBundle("Bundle", locale, new UTF8Control());
        displayDateTimeFormatter = displayDateTimeFormatter.withLocale(locale);
        connectionText.setText(" " + (isConnected ? staticBundle.getString("connected") : staticBundle.getString("disconnected")));
        setTitle(bundle.getString("title"));
        language.setText(bundle.getString("language"));
        infoObjectText.setText(bundle.getString("info"));
        nameText.setText(bundle.getString("name") + ":");
        familyText.setText(bundle.getString("family") + ":");
        hungerText.setText(bundle.getString("hunger") + ":");
        locationText.setText(bundle.getString("location") + ":");
        timeText.setText(bundle.getString("creationTime") + ":");
        inventoryText.setText(bundle.getString("inventory") + ":");
        refreshButton.setText(bundle.getString("refresh"));
        topFloorComboBox = bundle.getString("TopFloor");
        groundFloorComboBox = bundle.getString("GroungFloor");
        yardComboBox = bundle.getString("Yard");
        hillComboBox = bundle.getString("Hill");
        hangarComboBox = bundle.getString("Hangar");
        footPathComboBox = bundle.getString("FootPath");
        lightHouseComboBox = bundle.getString("LightHouse");
        nanComboBox = bundle.getString("NaN");
        nameStarts.setText(bundle.getString("nameFilter"));
        nameFromTo.setText(bundle.getString("name"));
        familyFromTo.setText(bundle.getString("family"));
        timeFromTo.setText(bundle.getString("time"));
        locationFromTo.setText(bundle.getString("location"));
        hungerFromTo.setText(bundle.getString("hunger"));
        changeButton.setText(bundle.getString("change"));
        newCreatureButton.setText(bundle.getString("newCreature"));
        addButton.setText(bundle.getString("add"));
        addIfMaxButton.setText(bundle.getString("add_if_max"));
        removeButton.setText(bundle.getString("remove"));
        cancelButton.setText(bundle.getString("cancel"));
        size.setText(bundle.getString("size")+  ":");
        sizeText.setText(bundle.getString("size")+  ":");
        timeFromTo.setText(bundle.getString("time"));

        if (chosenCreature != null) {
            setTopPanelInfo(chosenCreature);
        }
        /*switch (filtersTextNumber) {
            case 0:
                printText("greeting", false);
                break;
            case 1:
                printText("incorrectSize", true);
                break;
            case 2:
                printText("incorrectPower", true);
                break;
            case 3:
                printText("incorrectDate" + " dd-MM-yyyy!", true);
                break;
            case 4:
                printText(bundle.getString("filtersCorrect"), false);
                break;
            case 5:
                printText(bundle.getString("noObjectsFound"), false);
                break;
            case 6:
                //  printText(filteredCircles.size() + " " + bundle.getString("objectsFound"), false);
                break;
            case 7:
                printText(bundle.getString("acquired") + " " + creatureList.size() + " " + bundle.getString("elements"), false);
                break;
            case 8:
                printText(bundle.getString("tryAgain"), true);
                break;*/

    }
    private void newCreature(){
        addButton.setEnabled(true);
        addIfMaxButton.setEnabled(true);
        removeButton.setEnabled(true);
        cancelButton.setEnabled(true);
        newCreatureButton.setEnabled(false);
        changeButton.setEnabled(false);
        nameValue.setEditable(true);
        familyValue.setEditable(true);
        hungerValue.setEditable(true);
        locationBox.setEnabled(true);
        inventoryValue.setEditable(true);
        xFromSlider.setEnabled(true);
        yFromSlider.setEnabled(true);
        sizeFromSlider.setEnabled(true);
    }

    private void cancel(){
        addButton.setEnabled(false);
        addIfMaxButton.setEnabled(false);
        removeButton.setEnabled(false);
        cancelButton.setEnabled(false);
        newCreatureButton.setEnabled(true);
        changeButton.setEnabled(false);
        nameValue.setEditable(false);
        nameValue.setText("");
        familyValue.setEditable(false);
        familyValue.setText("");
        hungerValue.setEditable(false);
        hungerValue.setText("");
        locationBox.setSelectedIndex(0);
        locationBox.setEnabled(false);
        inventoryValue.setEditable(false);
        inventoryValue.setText("");
        xFromSlider.setValue(-1000);
        xFromSlider.setEnabled(false);
        yFromSlider.setValue(-1000);
        yFromSlider.setEnabled(false);
        sizeFromSlider.setValue(35);
        sizeFromSlider.setEnabled(false);
    }

    private void checkInput(String command) {
        if (nameValue.getText().isEmpty()) {
            printText(bundle.getString("name")  +" "+ bundle.getString("isEmpty"), true);
        } else if (familyValue.getText().isEmpty()) {
            printText(bundle.getString("family") +" " + bundle.getString("isEmpty"), true);
        } else if (hungerValue.getText().isEmpty()) {
            printText(bundle.getString("hunger") +" " + bundle.getString("isEmpty"), true);
        } else if (notANumeric(hungerValue.getText())) {
            printText(bundle.getString("hunger") + " " + bundle.getString("isnNumber"), true);
        } else if (Double.parseDouble(hungerValue.getText()) <= 0) {
            printText(bundle.getString("hunger") +" "+ bundle.getString("isNegative"), true);
        } else if (locationBox.getSelectedItem().equals("")) {
            printText(bundle.getString("location")  +" "+ bundle.getString("isnChosen"), true);
        } else {
            Creature creature = new Creature(nameValue.getText(), (int) Double.parseDouble(hungerValue.getText()), Location.valueOf(((String)locationBox.getSelectedItem()).replace(" ", "")), OffsetDateTime.now(), familyValue.getText(), xFromSlider.getValue(), yFromSlider.getValue(), sizeFromSlider.getValue(), Colors.Black);
            switch (command) {
                case "add":
//                    Auth.sm.add(creature);
                    printText(nameValue.getText() +" "+ bundle.getString("added"), false);
                    cancel();
                    break;

                case "add_if_max":
                    if (true) {
                        printText(bundle.getString("added"), false);
                        cancel();
                    } else {
                        printText(bundle.getString("NotMax"), false);
                    }
                    break;

                case "remove":
                    cancel();
//                    Auth.sm.remove_greater(creature);

                    break;
            }
        }
    }

    private void printToConsole(){

    }

    /*private void load(){
        Creature creature = new Creature(nameValue.getText(), )
    }*/
}