import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Timer;
import java.util.*;

class GUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private boolean clearSelection;
    private ArrayList<Creature> creatureList = new ArrayList<>();
    private ArrayList<Creature> filteredList = new ArrayList<>();
    private ResourceBundle bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
    static private ResourceBundle staticBundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
    private Locale rulocale = new Locale("ru");
    private Locale sllocale = new Locale("sl");
    private Locale enlocale = new Locale("en", "AU");
    private Locale svlocale = new Locale("sv");
    private Locale zhlocale = new Locale("zh", "CN");
    private JMenu language = new JMenu(bundle.getString("language"));

    private static JLabel connectionText = new JLabel();
    private static boolean isConnected = false;
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
    private JTextField timeValue = new JTextField();
    private JTextField inventoryValue = new JTextField();

    Sender sender;
    private Colors color;
    private boolean isFiltered = true;

    private JLabel infoText = new JLabel(" " + bundle.getString("greeting"));
    private String topFloorComboBox = bundle.getString("TopFloor");
    private String all = bundle.getString("all");
    private String groundFloorComboBox = bundle.getString("GroundFloor");
    private String yardComboBox = bundle.getString("Yard");
    private String hillComboBox = bundle.getString("Hill");
    private String hangarComboBox = bundle.getString("Hangar");
    private String footPathComboBox = bundle.getString("FootPath");
    private String lightHouseComboBox = bundle.getString("LightHouse");
    private String nanComboBox = bundle.getString("NaN");
    private String FernGreenComboBox = bundle.getString("fernGreen");
    private String BlackComboBox = bundle.getString("black");
    private String WhiteComboBox = bundle.getString("white");
    private String PareGoldComboBox = bundle.getString("pareGold");
    private String DeepRedComboBox = bundle.getString("deepRed");
    private String PurpleComboBox = bundle.getString("purple");
    private String OtherComboBox = bundle.getString("other");

    private JLabel nameFromTo = new JLabel(bundle.getString("name"));
    private JTextField nameTo = new JTextField();
    private JLabel familyFromTo = new JLabel(bundle.getString("family"));
    private JTextField familyTo = new JTextField();
    private JLabel timeFromTo = new JLabel(bundle.getString("time"));
    private JTextField timeTo = new JTextField();
    private JTextField xTo = new JTextField();
    private JTextField yTo = new JTextField();
    private JTextField sizeTo = new JTextField();


    private JLabel locationFromTo = new JLabel(bundle.getString("location"));
    private JLabel infoConnectionText = new JLabel("<html><h1 align=\"center\">" + bundle.getString("kek") + "</h1></html>");
    private JLabel tableP = new JLabel(bundle.getString("table"));
    private JLabel graphics = new JLabel(bundle.getString("graphics"));

    private JLabel hungerFromTo = new JLabel(bundle.getString("hunger"));
    private JTextField hungerTo = new JTextField();
    private JLabel size = new JLabel(bundle.getString("size") + ":");
    private JLabel sizeText = new JLabel(bundle.getString("size") + ":");

    private JSlider xFromSlider = new JSlider();
    private JSlider yFromSlider = new JSlider();
    private JSlider sizeFromSlider = new JSlider();

    private JButton changeButton = new JButton(bundle.getString("change"));
    private JButton newCreatureButton = new JButton(bundle.getString("newCreature"));
    private JButton addButton = new JButton(bundle.getString("add"));
    private JButton clearButton = new JButton(bundle.getString("clear"));
    private JButton addIfMaxButton = new JButton(bundle.getString("add_if_max"));
    private JButton removeButton = new JButton(bundle.getString("remove"));
    private JButton exitButton = new JButton(bundle.getString("exit"));
    private JButton cancelButton = new JButton(bundle.getString("cancel"));
    private JButton refreshButton = new JButton(bundle.getString("refresh"));

    private JComboBox<String> locationBox;
    private JComboBox<String> locationComboBox;
    private JComboBox<String> colorComboBox;
    private DateTimeFormatter displayDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM).withLocale(Locale.getDefault());
    private Creature chosenCreature;
    private DateTimeFormatter filterDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");

    GUI(Locale locale, Color color, String login) {
        JLabel loginInfo = new JLabel(bundle.getString("user") + ": " + login);
        switch (Integer.toString(color.getRGB())) {
            case "-15039176":
                this.color = Colors.FernGreen;
                break;
            case "-1":
                this.color = Colors.White;
                break;
            case "-6189544":
                this.color = Colors.PareGold;
                break;
            case "-10085091":
                this.color = Colors.DeepRed;
                break;
            case "-9479517":
                this.color = Colors.Purple;
                break;
            case "-16777216":
                this.color = Colors.Black;
            default:
                this.color = Colors.Purple;
        }

        nameValue.setEditable(false);
        familyValue.setEditable(false);
        hungerValue.setEditable(false);
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
        en_item.addActionListener(arg0 -> changeLanguage(enlocale, false));
        ru_item.addActionListener(arg0 -> changeLanguage(rulocale, false));
        sv_item.addActionListener(arg0 -> changeLanguage(svlocale, false));
        sl_item.addActionListener(arg0 -> changeLanguage(sllocale, false));
        zh_item.addActionListener(arg0 -> changeLanguage(zhlocale, false));
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
        refreshButton.addActionListener(arg0 -> {
            printText("", false);
            new Thread(sender::getCollection).start();
        });

        timeTo.setToolTipText(bundle.getString("format") + ": dd.MM.yy HH:mm:ss");
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
        sizeFromSlider.addChangeListener(e -> {
            sizeFrom.setText(Integer.toString(sizeFromSlider.getValue()));
            printText("", false);
        });

        xFromSlider.setEnabled(false);
        yFromSlider.setEnabled(false);
        sizeFromSlider.setEnabled(false);
        newCreatureButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(this::newCreature).start();
        });
        addButton.setEnabled(false);
        addButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(() -> checkInput("add")).start();
        });
        clearButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(sender::clearCreatures).start();
        });
        addIfMaxButton.setEnabled(false);
        addIfMaxButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(() -> checkInput("add_if_max")).start();
        });
        removeButton.setEnabled(false);
        removeButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(() -> {
                int index = table.getSelectedRow();
                String name = (String) table.getModel().getValueAt(index, 0);
                String family = (String) table.getModel().getValueAt(index, 1);
                for (Creature cr : creatureList) {
                    if (cr.getName().equals(name) && cr.getFamily().equals(family)) {
                        cancel();
                        sender.removeCreature(cr);
                        break;
                    }
                }
            }).start();
        });
        changeButton.setEnabled(false);
        changeButton.addActionListener(args0 -> {
            printText("", false);
            new Thread(() -> checkInput("change")).start();


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

        String[] locationsArray = {
                all,
                topFloorComboBox,
                groundFloorComboBox,
                yardComboBox,
                hillComboBox,
                hangarComboBox,
                footPathComboBox,
                lightHouseComboBox,
                nanComboBox
        };
        String[] locationsArray1 = {
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
        String[] colorsArray = {
                all,
                FernGreenComboBox,
                BlackComboBox,
                WhiteComboBox,
                PareGoldComboBox,
                DeepRedComboBox,
                PurpleComboBox,
                OtherComboBox
        };

        locationBox = new JComboBox<>(locationsArray1);
        locationBox.setEditable(false);
        locationBox.setEnabled(false);
        locationComboBox = new JComboBox<>(locationsArray);
        colorComboBox = new JComboBox<>(colorsArray);

        nameTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        familyTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        hungerTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        timeTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        xTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        yTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        sizeTo.getDocument().addDocumentListener((FiltersListener) this::checkFilters);
        final ActionListener actionListener = e -> new Thread(this::checkFilters).start();
        colorComboBox.addActionListener(actionListener);
        locationComboBox.addActionListener(actionListener);

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
        infoConnectionText.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        JPanel p8 = new JPanel();
        p8.setLayout(new BoxLayout(p8, BoxLayout.X_AXIS));
        xFromSlider.setMaximumSize(new Dimension(150, 17));
        JLabel x = new JLabel("X:");
        p8.add(x, Component.LEFT_ALIGNMENT);
        p8.add(Box.createRigidArea(new Dimension(100, 10)));
        p8.add(xFromSlider);
        p8.add(Box.createHorizontalGlue());
        p8.add(xFrom, Component.RIGHT_ALIGNMENT);

        JPanel p7 = new JPanel();
        p7.setLayout(new BoxLayout(p7, BoxLayout.X_AXIS));
        yFromSlider.setMaximumSize(new Dimension(150, 17));
        JLabel y = new JLabel("Y:");
        p7.add(y, Component.LEFT_ALIGNMENT);
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
        p5.add(Box.createRigidArea(new Dimension(5, 0)));
        p5.add(newCreatureButton);
        p5.add(Box.createRigidArea(new Dimension(5, 0)));
        p5.add(clearButton);
        p5.add(Box.createRigidArea(new Dimension(5, 0)));
        p5.add(cancelButton);

        JPanel p55 = new JPanel();
        p55.setLayout(new BoxLayout(p55, BoxLayout.X_AXIS));
        p55.add(addButton);
        p55.add(Box.createRigidArea(new Dimension(10, 0)));
        p55.add(addIfMaxButton);
        p55.add(Box.createRigidArea(new Dimension(10, 0)));
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
        p15.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        //Table
        String[] columns = {"Name", "Family", "Hunger", "Creation Time", "Location", "X", "Y", "Size", "Color"};
        tableModel = new MyDefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        table = new JTable(tableModel);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        table.getColumnModel().getColumn(8).setPreferredWidth(120);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!clearSelection) {
                new Thread(() -> {
                    cancel();
                    int index = table.getSelectedRow();
                    String name = (String) table.getModel().getValueAt(index, 0);
                    String family = (String) table.getModel().getValueAt(index, 1);
                    for (Creature cr : creatureList) {
                        if (cr.getName().equals(name) && cr.getFamily().equals(family)) {
                            nameValue.setText(name);
                            familyValue.setText(family);
                            hungerValue.setText((Integer.toString(cr.getHunger())));
                            locationBox.setSelectedItem(cr.getLocation().toString());
                            timeValue.setText((String) table.getModel().getValueAt(index, 3));
                            inventoryValue.setText(cr.getInventory().toString().substring(1, cr.getInventory().toString().length() - 1));
                            xFromSlider.setValue(cr.getX());
                            yFromSlider.setValue(cr.getY());
                            sizeFromSlider.setValue(cr.getSize());
                            break;
                        }
                    }
                    nameValue.setEditable(true);
                    familyValue.setEditable(true);
                    hungerValue.setEditable(true);
                    inventoryValue.setEditable(true);
                    xFromSlider.setEnabled(true);
                    yFromSlider.setEnabled(true);
                    sizeFromSlider.setEnabled(true);
                    newCreatureButton.setEnabled(false);
                    changeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    addButton.setEnabled(false);
                    locationBox.setEnabled(true);
                    addIfMaxButton.setEnabled(false);
                }).start();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        JPanel graphicsPanel = new JPanel();

        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        Font font3 = new Font("Verdana", Font.PLAIN, 12);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(font3);
        tabbedPane.addTab(tableP.getText(), scrollPane);
        tabbedPane.addTab(graphics.getText(), graphicsPanel);
        p3.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addChangeListener(e->
            new Thread(()->{
            JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            if (index == 0) {
                nameTo.setEnabled(true);
                familyTo.setEnabled(true);
                hungerTo.setEnabled(true);
                timeTo.setEnabled(true);
                locationComboBox.setEnabled(true);
                xTo.setEnabled(true);
                yTo.setEnabled(true);
                sizeTo.setEnabled(true);
                colorComboBox.setEnabled(true);
            } else {
                nameTo.setEnabled(false);
                familyTo.setEnabled(false);
                hungerTo.setEnabled(false);
                timeTo.setEnabled(false);
                locationComboBox.setEnabled(false);
                xTo.setEnabled(false);
                yTo.setEnabled(false);
                sizeTo.setEnabled(false);
                colorComboBox.setEnabled(false);
            }
        }).start());

        //Panels
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
        nameTo.setMaximumSize(new Dimension(119, 50));
        nameTo.setPreferredSize(new Dimension(119, 30));
        p2.add(nameTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        familyTo.setMaximumSize(new Dimension(119, 50));
        familyTo.setPreferredSize(new Dimension(119, 30));
        p2.add(familyTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        hungerTo.setMaximumSize(new Dimension(118, 50));
        hungerTo.setPreferredSize(new Dimension(118, 30));
        p2.add(hungerTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        timeTo.setMaximumSize(new Dimension(118, 50));
        timeTo.setPreferredSize(new Dimension(118, 30));
        p2.add(timeTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        locationComboBox.setMaximumSize(new Dimension(118, 50));
        locationComboBox.setPreferredSize(new Dimension(118, 30));
        p2.add(locationComboBox);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        xTo.setMaximumSize(new Dimension(118, 50));
        xTo.setPreferredSize(new Dimension(118, 30));
        p2.add(xTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        yTo.setMaximumSize(new Dimension(118, 50));
        yTo.setPreferredSize(new Dimension(118, 30));
        p2.add(yTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        sizeTo.setMaximumSize(new Dimension(118, 50));
        sizeTo.setPreferredSize(new Dimension(118, 30));
        p2.add(sizeTo);
        p2.add(Box.createRigidArea(new Dimension(5, 0)));
        colorComboBox.setMaximumSize(new Dimension(118, 50));
        colorComboBox.setPreferredSize(new Dimension(118, 30));
        p2.add(colorComboBox);
        p2.add(Box.createHorizontalGlue());
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
        userInfo.add(Box.createRigidArea(new Dimension(8, 0)));
        userInfo.add(exitButton);
        userInfo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JPanel pInfoExtended = new JPanel();
        pInfoExtended.setLayout(new BorderLayout());

        setLayout(new BorderLayout());
        add(userInfo, BorderLayout.NORTH);
        add(p3extended, BorderLayout.CENTER);
        add(p4extended, BorderLayout.EAST);
        changeLanguage(locale, true);
        setTitle(bundle.getString("title"));
        setSize(1500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    void exit() {
        this.setVisible(false);
        sender.exit();
        sender = null;
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

    private void checkFilters() {
        if (!xTo.getText().isEmpty() && (notANumeric(xTo.getText()) || (Integer.valueOf(xTo.getText()) > 1000 || Integer.valueOf(xTo.getText()) < -1000)))
            printText(bundle.getString("incorrectX"), true);
        else if (!yTo.getText().isEmpty() && (notANumeric(yTo.getText()) || (Integer.valueOf(yTo.getText()) > 1000 || Integer.valueOf(yTo.getText()) < -1000)))
            printText(bundle.getString("incorrectY"), true);
        else if (!sizeTo.getText().isEmpty() && (notANumeric(sizeTo.getText()) || (Integer.valueOf(sizeTo.getText()) < 10 || Integer.valueOf(sizeTo.getText()) > 99)))
            printText(bundle.getString("incorrectSize"), true);
        else if (!hungerTo.getText().isEmpty() && (notANumeric(hungerTo.getText()) || Integer.valueOf(hungerTo.getText()) < 1))
            printText(bundle.getString("incorrectHunger"), true);
        else if (!timeTo.getText().isEmpty()) {
            try {
                filterDateTimeFormatter.parse(timeTo.getText());
                printText("", false);
                applyFilters();
            } catch (DateTimeParseException e) {
                printText(bundle.getString("incorrectDate") + ": dd.MM.yy HH:mm:ss!", true);
            }

        } else {
            if (isFiltered)
                printText("", false);
            applyFilters();
        }
    }

    private boolean notANumeric(String str) {
        return !str.matches("[-+]?\\d+");
    }

    void printTextToConsole(String message, boolean isError) {
        if (isError) infoText.setForeground(Color.RED);
        else infoText.setForeground(Color.GREEN);
        infoText.setText(bundle.getString(message));
    }

    private void printText(String message, boolean isError) {
        if (isError) infoText.setForeground(Color.RED);
        else infoText.setForeground(Color.GREEN);
        infoText.setText(message);
    }

    void printTextToConsole(String message, int i) {
        infoText.setForeground(Color.GREEN);
        infoText.setText(bundle.getString(message) + " " + bundle.getString("creatures") + ": " + i);
    }

    private void applyFilters() {
        String colorFl = colorComboBox.getSelectedItem().toString().replace(" ", "");
        if(colorComboBox.getSelectedIndex()==0) colorFl = "";
        String locationFl = locationComboBox.getSelectedItem().toString().replace(" ", "");
        if(locationComboBox.getSelectedIndex()==0) locationFl = "";

        filteredList.clear();
        String finalColorFl = colorFl;
        String finalLocationFl = locationFl;
        creatureList.stream()
                .filter(o -> hungerTo.getText().isEmpty() || o.getHunger() == Integer.valueOf(hungerTo.getText()))
                .filter(o -> xTo.getText().isEmpty() || o.getX() == Integer.valueOf(xTo.getText()))
                .filter(o -> yTo.getText().isEmpty() || o.getY() == Integer.valueOf(yTo.getText()))
                .filter(o -> sizeTo.getText().isEmpty() || o.getSize() == Integer.valueOf(sizeTo.getText()))
                .filter(o -> timeTo.getText().isEmpty() || o.getCreationTime()
                        .format(filterDateTimeFormatter
                                .withZone(ZoneId.systemDefault()))
                        .equals(timeTo.getText()))
                .filter(o -> o.getName().startsWith(nameTo.getText()))
                .filter(o -> o.getFamily().startsWith(familyTo.getText()))
                .filter(o -> finalColorFl.equals("") || finalColorFl.equals(o.getColor().toString()))
                .filter(o -> finalLocationFl.equals("") || finalLocationFl.equals(o.getLocation().toString()))
                .forEach(filteredList::add);

        if (filteredList.size() == 0)
            printText(bundle.getString("NoCreaturesFound"), false);
        else {
            cancel();
            refreshTable(filteredList);
        }
    }

    /*private void stopAnimation() {
        for (Timer timer : timers) {
            timer.cancel();
        }
        for (Circle c : filteredCircles) {
            c.setNormalColor();
        }
        p3.revalidate();
        p3.repaint();
        changeButton.setEnabled(true);
        cancelButton.setEnabled(false);
    }*/

    synchronized void refreshCollection(List<Creature> tempCr) {
        ArrayList<Creature> newCreature = new ArrayList<>(tempCr);
        ArrayList<Creature> changedCr;
        if (creatureList.size() > 0) {
            changedCr = new ArrayList<>();
            Iterator<Creature> iter = tempCr.iterator();
            while (iter.hasNext()) {
                Creature crtemp = iter.next();
                Iterator<Creature> iter1 = creatureList.iterator();
                while (iter1.hasNext()) {
                    Creature cr = iter1.next();
                    if (cr.equals(crtemp)) {
                        iter1.remove();
                        iter.remove();
                        if (!cr.equalsAll(crtemp)) {
                            changedCr.add(crtemp);
                        }
                        break;
                    }
                }
            }
            changedCr.addAll(tempCr);
        }
        creatureList.clear();
        creatureList = newCreature;
        refreshTable(creatureList);
        isFiltered = false;
        checkFilters();
        isFiltered = true;
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

    private void refreshTable(ArrayList<Creature> list) {
        clearSelection = true;
        table.clearSelection();
        tableModel.setRowCount(0);
        for (Creature creature : list)
            addToTable(creature);
        clearSelection = false;
        table.repaint();
        table.revalidate();
    }

    private void changeLanguage(Locale locale, boolean isFirst) {
        printText("", false);
        bundle = ResourceBundle.getBundle("bundle", locale);
        staticBundle = ResourceBundle.getBundle("bundle", locale);
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
        all = bundle.getString("all");
        groundFloorComboBox = bundle.getString("GroundFloor");
        yardComboBox = bundle.getString("Yard");
        hillComboBox = bundle.getString("Hill");
        hangarComboBox = bundle.getString("Hangar");
        footPathComboBox = bundle.getString("FootPath");
        lightHouseComboBox = bundle.getString("LightHouse");
        nanComboBox = bundle.getString("NaN");
        FernGreenComboBox = bundle.getString("fernGreen");
        BlackComboBox = bundle.getString("black");
        WhiteComboBox = bundle.getString("white");
        PareGoldComboBox = bundle.getString("pareGold");
        DeepRedComboBox = bundle.getString("deepRed");
        PurpleComboBox = bundle.getString("purple");
        OtherComboBox = bundle.getString("other");
        nameFromTo.setText(bundle.getString("name"));
        familyFromTo.setText(bundle.getString("family"));
        timeFromTo.setText(bundle.getString("time"));
        locationFromTo.setText(bundle.getString("location"));
        infoConnectionText.setText("<html><h1 align=\"center\">" + bundle.getString("kek") + "</h1></html>");
        hungerFromTo.setText(bundle.getString("hunger"));
        changeButton.setText(bundle.getString("change"));
        newCreatureButton.setText(bundle.getString("newCreature"));
        addButton.setText(bundle.getString("add"));
        clearButton.setText(bundle.getString("clear"));
        addIfMaxButton.setText(bundle.getString("add_if_max"));
        removeButton.setText(bundle.getString("remove"));
        cancelButton.setText(bundle.getString("cancel"));
        exitButton.setText(bundle.getString("exit"));
        size.setText(bundle.getString("size") + ":");
        sizeText.setText(bundle.getString("size") + ":");
        timeFromTo.setText(bundle.getString("time"));
        tableP.setText(bundle.getString("table"));
        graphics.setText(bundle.getString("graphics"));
        table.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("name"));
        table.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("family"));
        table.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("hunger"));
        table.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("creationTime"));
        table.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("location"));
        table.getColumnModel().getColumn(7).setHeaderValue(bundle.getString("size"));
        table.getColumnModel().getColumn(8).setHeaderValue(bundle.getString("color"));
        if (!isFirst) refreshTable(filteredList);
    }

    private void newCreature() {
        addButton.setEnabled(true);
        addIfMaxButton.setEnabled(true);
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

    private void cancel() {
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
        xValue.setText("");
        yValue.setText("");
        timeValue.setText("");
        sizeValue.setText("");
    }

    private void checkInput(String command) {
        if (nameValue.getText().isEmpty()) {
            printText(bundle.getString("name") + " " + bundle.getString("isEmpty"), true);
        } else if (familyValue.getText().isEmpty()) {
            printText(bundle.getString("family") + " " + bundle.getString("isEmpty"), true);
        } else if (hungerValue.getText().isEmpty()) {
            printText(bundle.getString("hunger") + " " + bundle.getString("isEmpty"), true);
        } else if (notANumeric(hungerValue.getText())) {
            printText(bundle.getString("hunger") + " " + bundle.getString("isnNumber"), true);
        } else if (Integer.parseInt(hungerValue.getText()) <= 0) {
            printText(bundle.getString("hunger") + " " + bundle.getString("isnPositive"), true);
        } else if (locationBox.getSelectedItem().equals("")) {
            printText(bundle.getString("location") + " " + bundle.getString("isnChosen"), true);
        } else {
            Creature creature = new Creature(nameValue.getText(), Integer.parseInt(hungerValue.getText()), Location.valueOf(((String) locationBox.getSelectedItem()).replace(" ", "")), OffsetDateTime.now(), familyValue.getText(), xFromSlider.getValue(), yFromSlider.getValue(), sizeFromSlider.getValue(), color);
            if (!inventoryValue.getText().isEmpty()) {
                String inv = inventoryValue.getText();
                String[] result = inv.replace(" ", "").split(",");
                for(String str : result)
                    creature.getInventory().add(str);
            }
            switch (command) {
                case "add":
                    cancel();
                    sender.addCreature(creature);
                    break;
                case "change":
                    int index = table.getSelectedRow();
                    String name = (String) table.getModel().getValueAt(index, 0);
                    String family = (String) table.getModel().getValueAt(index, 1);
                    for (Creature cr : creatureList) {
                        if (cr.getName().equals(name) && cr.getFamily().equals(family)) {
                            creature.setId(cr.getId());
                            break;
                        }
                    }
                    cancel();
                    sender.changeCreature(creature);
                    break;
                case "add_if_max":
                    cancel();
                    sender.addIfMaxCreature(creature);
                    break;
            }
        }
    }

    private void addToTable(Creature cr) {
        String time = cr.getCreationTime().format(displayDateTimeFormatter);
        tableModel.addRow(new Object[]{
                cr.getName(), cr.getFamily(), cr.getHunger(), time, cr.getLocation().toString(), cr.getX(), cr.getY(), cr.getSize(), cr.getColor().name()});
    }

    /*private void load(){
        Creature creature = new Creature(nameValue.getText(), )
    }*/
}
