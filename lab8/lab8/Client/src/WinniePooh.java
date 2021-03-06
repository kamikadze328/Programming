import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class WinniePooh extends JButton {
    private int s;
    Creature creature;
    private GUI frame;
    RightHand rightHand;
    Balloon balloon;
    private boolean isBalloon = true;
    private boolean isHand = false;


    WinniePooh(Creature creature, GUI frame) {
        this.creature = creature;
        this.frame = frame;
        this.rightHand = new RightHand(creature, frame);
        this.balloon = new Balloon(creature, frame);
        s = creature.getSize();
        setBorder(null);
        setEnabled(false);
        rebound();
        this.setToolTipText(creature.getName());
        this.addMouseListener(new setInfoOnClick());

    }

    @Override
    public void paintComponent(Graphics g1) {
        rebound();
        Graphics2D g = (Graphics2D) g1;
        Color brown = new Color(107, 63, 13);
        Color darkBrown = new Color(43, 26, 7);
        AffineTransform old = g.getTransform();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //ears
        g.setColor(darkBrown);
        g.fillOval(s * 62 / 100, s * 17 / 100, s * 60 / 100, s * 60 / 100);
        g.setColor(brown);
        g.fillOval(s * 745 / 1000, s * 30 / 100, s * 34 / 100, s * 34 / 100);
        g.setColor(darkBrown);
        g.fillOval(s * 177 / 100, s * 17 / 100, s * 60 / 100, s * 60 / 100);
        g.setColor(brown);
        g.fillOval(s * 1890 / 1000, s * 30 / 100, s * 34 / 100, s * 34 / 100);

        //legs
        g.setColor(darkBrown);
        g.rotate(Math.toRadians(80));
        g.fillOval(s * 25 / 10, -s, s, s * 70 / 100);
        g.setTransform(old);
        g.setColor(darkBrown);
        g.rotate(Math.toRadians(-80));
        g.fillOval(-s * 297 / 100, s * 20 / 10, s, s * 70 / 100);
        g.setTransform(old);

        //body
        g.setColor(brown);
        g.fillRoundRect(s * 820 / 1000, s * 40 / 100, s * 135 / 100, s, s * 60 / 100, s * 65 / 100);
        g.setColor(brown);
        g.fillOval(s / 2, s * 90 / 100, s * 2, s * 2);

        //left Hand
        g.setColor(darkBrown);
        g.rotate(Math.toRadians(-50));
        g.fillOval(-s * 160 / 100, s * 130 / 100, s, s * 60 / 100);
        g.setTransform(old);

        //face
        g.setColor(darkBrown);
        g.fillOval(s, s * 5 / 10, s, s * 8 / 10);
        g.setColor(brown);
        g.fillOval(s, s * 9 / 10, s, s * 8 / 10);
        g.setColor(Color.BLACK);
        g.fillOval(s * 126 / 100, s * 93 / 100, s * 50 / 100, s * 40 / 100);
        g.setColor(Color.BLACK);
        g.fillOval(s * 1445 / 1000, s * 140 / 100, s * 13 / 100, s * 9 / 100);
        g.setColor(Color.WHITE);
        g.fillOval(s * 117 / 100, s * 65 / 100, s * 17 / 100, s * 23 / 100);
        g.setColor(Color.WHITE);
        g.fillOval(s * 166 / 100, s * 65 / 100, s * 17 / 100, s * 23 / 100);
        g.setColor(Color.BLACK);
        g.fillOval(s * 1707 / 1000, s * 72 / 100, s * 8 / 100, s * 8 / 100);
        g.setColor(Color.BLACK);
        g.fillOval(s * 1212 / 1000, s * 72 / 100, s * 8 / 100, s * 8 / 100);
    }

    private void rebound() {
        int x = (frame.graphicsPanel.getWidth() - 150) * creature.getX() / 1000;
        int y = (frame.graphicsPanel.getHeight() - 150) * creature.getY() / 1000;
        this.setBounds(x, y, s * 25 / 10, s * 337 / 100);
    }

    void transition() {
        while (true) {
             balloon.y--;
            if (!isHand) {
                rightHand.degree--;
                if (rightHand.degree < -50)
                    isHand = true;
            } else {
                rightHand.degree++;
                if (rightHand.degree > 50)
                    isHand = false;
            }
            if (rightHand.degree == 50 && !isBalloon) {
                balloon.setNormal();
                rightHand.setNormal();
                break;
            }
            try {
                frame.graphicsPanel.revalidate();
                frame.graphicsPanel.repaint();
                Thread.sleep(7);
            } catch (Exception ignored) {
            }
            if (balloon.y + s * 6 / 2 < 0) {
                isBalloon = false;
            }
        }
    }


    /*void setNormalColor() {
        diff = 40;
        range = 40;
        isGoingWhite = true;
        color = sea.getColor().getRgbColor();
    }*/

    class setInfoOnClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            frame.cancel();
            frame.setCreatureInfo(creature);
        }
    }
}
class RightHand extends JButton {
    private int s;
    private Creature creature;
    private GUI frame;
    int degree = 50;


    RightHand(Creature creature, GUI frame) {
        this.creature = creature;
        this.frame = frame;
        s = creature.getSize();
        setBorder(null);
        setEnabled(false);
        rebound();
        this.setToolTipText(creature.getName());
        this.addMouseListener(new setInfoOnClick());
    }

    @Override
    public void paintComponent(Graphics g1) {
        rebound();
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        Color darkBrown = new Color(43, 26, 7);
        g.setColor(darkBrown);
        g.rotate(Math.toRadians(degree), s/3+s*28/100, s/4+s*4/10);
        g.fillOval(s/3, s/4, s, s * 60 / 100);
    }

    private void rebound() {
        int x = (frame.graphicsPanel.getWidth() - 150) * creature.getX() / 1000+ s*170/100;
        int y = (frame.graphicsPanel.getHeight() - 150) * creature.getY() / 1000 + s*110/100;
        this.setBounds(x, y, s*135/100, s*12/10);
    }
    void setNormal(){
        degree = 50;
    }

    class setInfoOnClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            frame.cancel();
            frame.setCreatureInfo(creature);
        }
    }
}
class Balloon extends JButton {
    private Color color;
    private int s;
    private Creature creature;
    private GUI frame;
    int y;
    private int x;

    Balloon(Creature creature, GUI frame) {
        this.creature = creature;
        this.frame = frame;
        s = creature.getSize();
        y = (frame.graphicsPanel.getHeight()-150) * creature.getY() / 1000 - s;
        x = s/10;
        color = creature.getColor().getRgbColor();
        setBorder(null);
        setEnabled(false);
        rebound();
    }

    @Override
    public void paintComponent(Graphics g1) {
        rebound();
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(x, y, s*12/10, s*15/10);
        g.setColor(Color.BLACK);
        g.drawLine(x + s*6/10, y+s*3/2, x + s*6/10, y+s*6/2);
        g.setColor(color);
        g.fillPolygon(new int[]{x + s * 6 / 10, x + s * 6 / 10 - x, x + s * 6 / 10 + x}, new int[]{y + s * 298 / 200, y + s * 3 / 2 + s / 9, y + s * 3 / 2 + s / 9},3);

    }

    private void rebound() {
        this.setBounds((frame.graphicsPanel.getWidth() - 150) * creature.getX() / 1000 + s * 22/10, 0, s*13/10, frame.graphicsPanel.getHeight());
    }
    void setNormal(){
        y = (frame.graphicsPanel.getHeight()-150) * creature.getY() / 1000- s;
    }
}