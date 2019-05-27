import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Timer;
import java.awt.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

class GUI extends JFrame {
    CopyOnWriteArrayList<Creature> creatureList = new CopyOnWriteArrayList<>();
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
    JPanel p3;
    private ArrayList<Timer> timers;
    private JLabel infoObjectText = new JLabel(bundle.getString("info") + ":");
    private JLabel nameText = new JLabel(bundle.getString("name") + ":");
    private JLabel familyText = new JLabel(bundle.getString("family") + ":");
    private JLabel hungerText = new JLabel(bundle.getString("hunger") + ":");
    private JLabel locationText = new JLabel(bundle.getString("location") + ":");
    private JLabel timeText = new JLabel(bundle.getString("creationTime") + ":");
    private JTextField xValue = new JTextField();
    private JTextField yValue = new JTextField();
    private JTextField sizeValue = new JTextField();

    private JTextField nameValue = new JTextField();
    private JTextField familyValue = new JTextField();
    private JTextField hungerValue = new JTextField();
    private JTextField locationValue = new JTextField();
    private JTextField timeValue = new JTextField();

    private Connector connector;

    private JButton refreshButton = new JButton(bundle.getString("refresh"));
    private JLabel infoText = new JLabel(" " + bundle.getString("greeting"));
    private int filtersTextNumber = 0;
    private JCheckBox topFloorCheckBox = new JCheckBox(bundle.getString("TopFloor"));
    private JCheckBox groundFloorCheckBox = new JCheckBox(bundle.getString("GroungFloor"));
    private JCheckBox yardCheckBox = new JCheckBox(bundle.getString("Yard"));
    private JCheckBox hillCheckBox = new JCheckBox(bundle.getString("Hill"));
    private JCheckBox hangarCheckBox = new JCheckBox(bundle.getString("Hangar"));
    private JCheckBox footPathCheckBox = new JCheckBox(bundle.getString("FootPath"));
    private JCheckBox lightHouseCheckBox = new JCheckBox(bundle.getString("LightHouse"));
    private JCheckBox nanCheckBox = new JCheckBox(bundle.getString("NaN"));
    private JLabel nameStarts = new JLabel(bundle.getString("nameFilter"));
    private JTextField nameField = new JTextField();
    private JLabel nameFromTo = new JLabel(bundle.getString("name"));
    private JTextField nameTo = new JTextField();
    private JLabel familyFromTo = new JLabel(bundle.getString("family"));
    private JTextField familyTo = new JTextField();
    private JLabel timeFromTo = new JLabel(bundle.getString("time"));
    private JTextField timeFrom = new JTextField();
    private JTextField timeTo = new JTextField();
    private JLabel locationFromTo = new JLabel(bundle.getString("location"));
    private JTextField locationTo = new JTextField();
    private JLabel hungerFromTo = new JLabel(bundle.getString("hunger"));
    private JTextField hungerTo = new JTextField();
    private JLabel X = new JLabel("X:");
    private JLabel Y = new JLabel("Y:");
    private JLabel size = new JLabel("Size:");


    private JSlider xFromSlider = new JSlider();
    private JSlider yFromSlider = new JSlider();
    private JSlider sizeFromSlider = new JSlider();


    private JButton startButton = new JButton(bundle.getString("start"));
    private JButton stopButton = new JButton(bundle.getString("stop"));
    private DateTimeFormatter displayDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.MEDIUM).withLocale(Locale.getDefault());
    private Creature chosenCreature;
    private DateTimeFormatter filterDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");
    private JPanel pane1;

    GUI(Connector connector, Locale locale) {
        changeLanguage(locale);
        this.connector = connector;
        nameValue.setEditable(false);
        familyValue.setEditable(false);
        hungerValue.setEditable(false);
        locationValue.setEditable(false);
        timeValue.setEditable(false);
        xValue.setEditable(false);
        yValue.setEditable(false);
        sizeValue.setEditable(false);
        JLabel xText = new JLabel("X:");
        JLabel yText = new JLabel("Y:");
        JLabel sizeText = new JLabel("Size:");
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
        connectionText.setBackground(Color.BLACK);
        connectionText.setOpaque(true);
        connectionText.setText(" ");
        connectionText.setBackground(Color.BLACK);
        connectionText.setFont(new Font("Sans-Serif", Font.PLAIN, 16));
        infoText.setBackground(Color.BLACK);
        infoText.setOpaque(true);
        infoText.setFont(new Font("Sans-Serif", Font.PLAIN, 16));
        infoText.setForeground(Color.GREEN);
        refreshButton.addActionListener(arg0 -> new Thread(this::refreshCollection).start());

        topFloorCheckBox.setSelected(true);
        groundFloorCheckBox.setSelected(true);
        yardCheckBox.setSelected(true);
        hillCheckBox.setSelected(true);
        hangarCheckBox.setSelected(true);
        footPathCheckBox.setSelected(true);
        lightHouseCheckBox.setSelected(true);
        nanCheckBox.setSelected(true);

        timeFrom.setToolTipText(bundle.getString("format") + ": dd.MM.yyyy HH:mm:ss");
        timeTo.setToolTipText(bundle.getString("format") + ": dd.MM.yyyy HH:mm:ss");
        xFromSlider.setMinimum(-1000);
        xFromSlider.setMaximum(1000);
        xFromSlider.setValue(-1000);
        JLabel xFrom = new JLabel("-1000");
        xFromSlider.addChangeListener(e -> xFrom.setText(Integer.toString(xFromSlider.getValue())));
        yFromSlider.setMinimum(-1000);
        yFromSlider.setMaximum(1000);
        yFromSlider.setValue(-1000);
        JLabel yFrom = new JLabel("-1000");
        yFromSlider.addChangeListener(e -> yFrom.setText(Integer.toString(yFromSlider.getValue())));
        sizeFromSlider.setMinimum(-1000);
        sizeFromSlider.setMaximum(1000);
        sizeFromSlider.setValue(-1000);
        JLabel sizeFrom = new JLabel("-1000");
        xFromSlider.addChangeListener(e -> sizeFrom.setText(Integer.toString(xFromSlider.getValue())));
        xFrom.setPreferredSize(new Dimension(35, 20));
        yFrom.setPreferredSize(new Dimension(35, 20));

        startButton.addActionListener(args0 -> new Thread(this::checkFilters).start());
        stopButton.setEnabled(false);
        stopButton.addActionListener(args0 -> new Thread(this::stopAnimation).start());

        //Panels
        JPanel p17 = new JPanel();
        p17.setLayout(new GridLayout());
        GridBagConstraints c = new GridBagConstraints();
        p17.add(connectionText);
        JPanel refreshButtonPanel = new JPanel();
        refreshButtonPanel.setLayout(new BoxLayout(refreshButtonPanel, BoxLayout.X_AXIS));
        refreshButtonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        refreshButtonPanel.add(refreshButton);
        p17.add(refreshButton);

        JPanel p16 = new JPanel();
        p16.setLayout(new BorderLayout());
        p16.add(infoText);

        JPanel p15 = new JPanel();
        p15.setLayout(new BoxLayout(p15, BoxLayout.X_AXIS));
        p15.add(locationText);

        JPanel p14 = new JPanel();
        p14.setLayout(new GridLayout(3, 3, 0, 5));


        JPanel p14extended = new JPanel();
        p14extended.setLayout(new BoxLayout(p14extended, BoxLayout.X_AXIS));
        p14extended.add(Box.createRigidArea(new Dimension(80, 0)));
        p14extended.add(p14);

        JPanel p13 = new JPanel();
        p13.setLayout(new BoxLayout(p13, BoxLayout.X_AXIS));
        p13.add(infoObjectText);
        p13.add(Box.createRigidArea(new Dimension(10, 0)));
        p13.add(nameField);

        JPanel p12 = new JPanel();
        p12.setLayout(new BoxLayout(p12, BoxLayout.X_AXIS));
        p12.add(Box.createRigidArea(new Dimension(7, 0)));
        p12.add(nameFromTo);
        p12.add(Box.createRigidArea(new Dimension(7, 0)));
        p12.add(nameTo);

        JPanel p11 = new JPanel();
        p11.setLayout(new BoxLayout(p11, BoxLayout.X_AXIS));
        p11.add(Box.createRigidArea(new Dimension(7, 0)));
        p11.add(familyFromTo);
        p11.add(Box.createRigidArea(new Dimension(7, 0)));
        p11.add(familyTo);

        JPanel p111 = new JPanel();
        p111.setLayout(new BoxLayout(p111, BoxLayout.X_AXIS));
        p111.add(Box.createRigidArea(new Dimension(7, 0)));
        p111.add(hungerFromTo);
        p111.add(Box.createRigidArea(new Dimension(7, 0)));
        p111.add(hungerTo);

        JPanel p10 = new JPanel();
        p10.setLayout(new BoxLayout(p10, BoxLayout.X_AXIS));
        p10.add(Box.createRigidArea(new Dimension(7, 0)));
        p10.add(timeFromTo);
        p10.add(Box.createRigidArea(new Dimension(7, 0)));
        p10.add(timeTo);

        JPanel p9 = new JPanel();
        p9.setLayout(new BoxLayout(p9, BoxLayout.X_AXIS));
        p9.add(Box.createRigidArea(new Dimension(7, 0)));
        p9.add(locationFromTo);
        p9.add(Box.createRigidArea(new Dimension(7, 0)));
        p9.add(locationTo);


        JPanel p8 = new JPanel();
        p8.setLayout(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(0, 10, 10, 10);
        c1.gridx = 0;
        c1.gridy = 0;
        p8.add(X, c1);
        c1.gridx++;
        p8.add(xFromSlider, c1);
        c1.gridx++;
        p8.add(xFrom, c1);

        JPanel p7 = new JPanel();
        p7.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(0, 10, 10, 10);
        c2.gridx = 0;
        c2.gridy = 0;
        p7.add(Y, c2);
        c2.gridx++;
        p7.add(yFromSlider, c2);
        c2.gridx++;
        p7.add(yFrom, c2);

        JPanel p6 = new JPanel();
        p6.setLayout(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        c3.insets = new Insets(0, 10, 10, 10);
        c3.gridx = 0;
        c3.gridy = 0;
        p6.add(size, c3);
        c3.gridx++;
        p6.add(sizeFromSlider, c3);
        c3.gridx++;
        p6.add(sizeFrom, c3);

        JPanel p5 = new JPanel();
        p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
        p5.add(startButton);
        p5.add(Box.createRigidArea(new Dimension(30, 0)));
        p5.add(stopButton);

        JPanel p4 = new JPanel();
        p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p17);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p16);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p15);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p14extended);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p13);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p12);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p11);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p111);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p10);
        p4.add(Box.createRigidArea(new Dimension(0, 5)));
        p4.add(p9);
        p4.add(Box.createRigidArea(new Dimension(0, 30)));

        p4.add(p8);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p7);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p6);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));
        p4.add(p5);
        p4.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel p4extended = new JPanel();
        p4extended.setLayout(new BoxLayout(p4extended, BoxLayout.X_AXIS));
        p4extended.add(Box.createRigidArea(new Dimension(15, 0)));
        p4extended.add(p4);
        p4extended.add(Box.createRigidArea(new Dimension(10, 0)));
        p4extended.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK));

        p3 = new JPanel();
        p3.setLayout(null);

        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        timeValue.setPreferredSize(new Dimension(20, 20));
        p2.add(infoObjectText);
        p2.add(nameText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(nameValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(familyText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(familyValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(hungerText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(hungerValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(timeText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(timeValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(locationText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(locationValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(xText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(xValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(yText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(yValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(sizeText);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));
        p2.add(sizeValue);
        p2.add(Box.createRigidArea(new Dimension(8, 0)));

        JPanel p2extended = new JPanel();
        p2extended.setLayout(new BoxLayout(p2extended, BoxLayout.Y_AXIS));
        JPanel topSpace = new JPanel();
        topSpace.setLayout(new BorderLayout());
        topSpace.add(Box.createRigidArea(new Dimension(0, 8)), BorderLayout.NORTH);
        JPanel botSpace = new JPanel();
        botSpace.setLayout(new BorderLayout());
        botSpace.add(Box.createRigidArea(new Dimension(0, 8)), BorderLayout.NORTH);
        p2extended.add(topSpace);
        p2extended.add(p2);
        p2extended.add(botSpace);
        botSpace.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        setLayout(new BorderLayout());
        add(p2extended, BorderLayout.NORTH);
        add(p3, BorderLayout.CENTER);
        add(p4extended, BorderLayout.EAST);

        setTitle(bundle.getString("title"));
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        firstTimeRefresh();
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

    void setTopPanelInfo(Creature creature) {
        chosenCreature = creature;
        nameValue.setText(creature.getName());
        familyValue.setText(creature.getFamily());
        hungerValue.setText(String.valueOf(creature.getHunger()));
        locationValue.setText(bundle.getString(creature.getLocation().name().toLowerCase()));
        timeValue.setText(displayDateTimeFormatter.format(creature.getCreationTime()));
    }

    private void checkFilters() {
        startButton.setEnabled(false);
        /*if ((notANumeric(sizeFrom.getText()) && !sizeFrom.getText().isEmpty()) || (notANumeric(sizeTo.getText()) && !sizeTo.getText().isEmpty())) {
            setFiltersText(bundle.getString("incorrectSize"), true);
            filtersTextNumber = 1;
            startButton.setEnabled(true);
        } else *//*if ((notANumeric(powerFrom.getText()) && !powerFrom.getText().isEmpty()) || (notANumeric(powerTo.getText()) && !powerTo.getText().isEmpty())) {
            setFiltersText(bundle.getString("incorrectPower"), true);
            filtersTextNumber = 2;
            startButton.setEnabled(true);
        } else {*/
        try {
            if (!timeFrom.getText().isEmpty()) {
                filterDateTimeFormatter.parse(timeFrom.getText());
            }
            if (!timeTo.getText().isEmpty()) {
                filterDateTimeFormatter.parse(timeTo.getText());
            }
            setFiltersText(bundle.getString("filtersCorrect"), false);
            filtersTextNumber = 4;
            applyFilters();
        } catch (DateTimeParseException e) {
            setFiltersText(bundle.getString("incorrectDate") + " dd.MM.yyyy HH:mm:ss!", true);
            filtersTextNumber = 3;
            startButton.setEnabled(true);
        }
        //  }
    }

    private boolean notANumeric(String str) {
        return !str.matches("-?\\d+(\\.\\d+)?");
    }

    private void setFiltersText(String message, boolean isError) {
        if (isError) infoText.setForeground(Color.RED);
        else infoText.setForeground(Color.GREEN);
        infoText.setText(" " + message);
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
            startButton.setEnabled(true);
            setFiltersText(bundle.getString("noObjectsFound"), false);
            filtersTextNumber = 5;
        } else {
            stopButton.setEnabled(true);
            setFiltersText(filteredCircles.size() + " " + bundle.getString("objectsFound"), false);
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
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void refreshCollection() {
        CopyOnWriteArrayList<Creature> tempCreatureList = connector.getCollection();
        if (tempCreatureList != null) {
            creatureList = tempCreatureList;
            //circleList = new Circle[creatureList.size()];
            p3.removeAll();
            /*for (int i = 0; i < creatureList.size(); i++) {
                Circle c = new Circle(creatureList.get(i), this);
                circleList[i] = c;
                p3.add(c);
            }*/
            setFiltersText(bundle.getString("acquired") + " " + creatureList.size() + " " + bundle.getString("elements"), false);
            filtersTextNumber = 7;
            p3.revalidate();
            p3.repaint();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        } else {
            setFiltersText(bundle.getString("tryAgain"), true);
            filtersTextNumber = 8;
        }
    }

    private void firstTimeRefresh() {
        while (true) {
            CopyOnWriteArrayList<Creature> tempCreatureList = connector.getCollection();
            if (tempCreatureList != null) {
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
    }

    private void changeLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("Bundle", locale, new UTF8Control());
        staticBundle = ResourceBundle.getBundle("Bundle", locale, new UTF8Control());
        displayDateTimeFormatter = displayDateTimeFormatter.withLocale(locale);
        connectionText.setText(" " + (isConnected ? staticBundle.getString("connected") : staticBundle.getString("disconnected")));
        setTitle(bundle.getString("title"));
        language.setText(bundle.getString("language"));
        infoObjectText.setText(bundle.getString("info") + " - ");
        nameText.setText(bundle.getString("name") + ":");
        familyText.setText(bundle.getString("family") + ":");
        hungerText.setText(bundle.getString("hunger") + ":");
        locationText.setText(bundle.getString("location") + ":");
        timeText.setText(bundle.getString("creationTime") + ":");
        refreshButton.setText(bundle.getString("refresh"));
        topFloorCheckBox.setText(bundle.getString("TopFloor"));
        groundFloorCheckBox.setText(bundle.getString("GroungFloor"));
        yardCheckBox.setText(bundle.getString("Yard"));
        hillCheckBox.setText(bundle.getString("Hill"));
        hangarCheckBox.setText(bundle.getString("Hangar"));
        footPathCheckBox.setText(bundle.getString("FootPath"));
        lightHouseCheckBox.setText(bundle.getString("LightHouse"));
        nanCheckBox.setText((bundle.getString("NaN")));
        nameStarts.setText(bundle.getString("nameFilter"));
        nameFromTo.setText(bundle.getString("name"));
        familyFromTo.setText(bundle.getString("family"));
        timeFromTo.setText(bundle.getString("time"));
        locationFromTo.setText(bundle.getString("location"));
        hungerFromTo = new JLabel(bundle.getString("hunger"));
        startButton.setText(bundle.getString("start"));
        stopButton.setText(bundle.getString("stop"));
        if (chosenCreature != null) {
            setTopPanelInfo(chosenCreature);
        }
        switch (filtersTextNumber) {
            case 0:
                setFiltersText(bundle.getString("greeting"), false);
                break;
            case 1:
                setFiltersText(bundle.getString("incorrectSize"), true);
                break;
            case 2:
                setFiltersText(bundle.getString("incorrectPower"), true);
                break;
            case 3:
                setFiltersText(bundle.getString("incorrectDate") + " dd-MM-yyyy!", true);
                break;
            case 4:
                setFiltersText(bundle.getString("filtersCorrect"), false);
                break;
            case 5:
                setFiltersText(bundle.getString("noObjectsFound"), false);
                break;
            case 6:
                //  setFiltersText(filteredCircles.size() + " " + bundle.getString("objectsFound"), false);
                break;
            case 7:
                setFiltersText(bundle.getString("acquired") + " " + creatureList.size() + " " + bundle.getString("elements"), false);
                break;
            case 8:
                setFiltersText(bundle.getString("tryAgain"), true);
                break;
        }
    }
}