public class Morra extends Creature {
    private static final long serialVersionUID = -148109982058042834L;
    protected int temperature;
    Morra(String n, Location l, int t){
        super(n,l);
        temperature = t;
    }
}
