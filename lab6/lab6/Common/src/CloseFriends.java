public class CloseFriends  extends Creature {
    private static final long serialVersionUID = -1228332872132319197L;
    protected int intelligence;
    protected int strength;
    protected boolean tail;
    CloseFriends(String n, Location l, int i, int s, boolean t){
        super(n,l);
        intelligence = i;
        strength = s;
        tail = t;
    }

}
