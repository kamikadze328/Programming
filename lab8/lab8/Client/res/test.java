import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;


public class test extends JButton {

    protected double angle = 0D;

    public test() {
        this( null, null );
    }

    public test( Action a ) {
        this();
        setAction( a );
    }

    public test( Icon icon ) {
        this( null, icon );
    }

    public test( String text ) {
        this( text, null );
    }

    public test( String text, Icon icon ) {
        super( text, icon );
    }

    public void paint( Graphics g ) {
        Point oldCenter = new Point( super.getPreferredSize().width /
                2, super.getPreferredSize().height / 2 );
        Point bigCenter = new Point( getLargest() / 2, getLargest() /
                2 );
        Point newCenter = new Point( bigCenter.x - oldCenter.x,
                bigCenter.y - oldCenter.y );

        Image oldImage = createImage( getLargest(), getLargest() );
        Graphics oldG = oldImage.getGraphics();
        oldG.setClip( newCenter.x, newCenter.y,
                super.getPreferredSize().width,
                super.getPreferredSize().height );

        Image newImage = createImage( getLargest(), getLargest() );
        Graphics newG = newImage.getGraphics();

        super.paint( oldG );

        newG.drawImage( oldImage, 0, 0, this );

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor( getParent().getBackground() );
        g2.fillRect( 0, 0, getLargest(), getLargest() );
        g2.rotate( angle, getPreferredSize().width / 2,
                getPreferredSize().height / 2 );
        g2.drawImage( newImage, 0, 0, this );
    }

    protected void paintBorder( Graphics g ) {
        if( isBorderPainted() ) {
            Border border = getBorder();
            if( border != null ) {
                Point oldCenter = new Point(
                        super.getPreferredSize().width / 2,
                        super.getPreferredSize().height / 2 );
                Point bigCenter = new Point( getLargest() / 2,
                        getLargest() / 2 );
                Point newCenter = new Point(
                        bigCenter.x - oldCenter.x,
                        bigCenter.y - oldCenter.y );
                border.paintBorder( this, g, newCenter.x,
                        newCenter.y, super.getPreferredSize().width,
                        super.getPreferredSize().height );
            }
        }
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle( double angle ) {
        this.angle = angle;
        repaint();
    }

    public Dimension getPreferredSize() {
        Dimension d = new Dimension( getLargest(), getLargest() );
        return d;
    }

    protected int getLargest() {
        int w = super.getPreferredSize().width;
        int h = super.getPreferredSize().height;

        if( w > h ) {
            return w;
        }
        else {
            return h;
        }
    }

    public static void main( String[] arg ) {
        new AngledButtonTester();
    }
}

class AngledButtonTester extends JFrame {
    public AngledButtonTester() {
        super( "AngledButton Tester" );
        test b = new test( "Angled" );
        b.addActionListener( new SpinTheButton() );
        JPanel p = new JPanel();
        JButton old = new JButton( "Normal" );
        p.add( b );
        p.add( old );
        p.setOpaque( false );
        getContentPane().add( p );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 300, 300 );
        setVisible( true );
    }

    private class SpinTheButton implements ActionListener, Runnable {
        private test b;

        public void actionPerformed( ActionEvent e ) {
            if( e.getSource() instanceof test ) {
                b = (test)e.getSource();
                Thread t = new Thread( this );
                t.start();
            }
        }

        public void run() {
            double d = b.getAngle();
            while( d < 2 * Math.PI ) {
                b.setAngle( d );
                d += 0.1;
                try {
                    Thread.sleep( 80 );
                }
                catch( InterruptedException x ) {
                    System.err.println( "Spinning interrupted!" );
                }
            }
            b.setAngle( 0.0 );
        }
    }
}