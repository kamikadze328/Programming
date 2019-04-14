public class MoominFamily extends Creature{
    private static final long serialVersionUID = 2175155311815998353L;
    protected int waste;
    protected int care;
    protected boolean hat;

    MoominFamily(String n, Location l, int w, int c, boolean h){
        super(n,l);
        waste = w;
        care = c;
        hat = h;
    }
}
