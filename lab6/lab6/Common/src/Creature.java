import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.Objects;

public class Creature implements Comparable<Creature>, Serializable {
    private static final long serialVersionUID = 4520157701042133812L;
    private String name;
    private int hunger;
    private Location location;
    private OffsetDateTime CurrentTime;
    private String Class;

    class Inventory implements Serializable{
        private static final long serialVersionUID = -1484983106197509695L;
        LinkedList<Creature> inventory = new LinkedList<>();
        protected void add(Creature a) {
            inventory.add(a);
            System.out.println(Creature.this.getName() + " поднимает " + a.getName());
        }
        protected void remove(Creature a) {
            inventory.remove(a);
            System.out.println(Creature.this.getName() + " опускает " + a.getName());
        }
    }

    Creature.Inventory inventory = new Creature.Inventory();

    Creature(String n) {
        name = n;
        CurrentTime = OffsetDateTime.now();
    }

    Creature(String n, Location l){
        name = n;
        location = l;
        CurrentTime = OffsetDateTime.now();
    }

    public String getName() {
        return name;
    }
    public int getHunger() {
        return hunger;
    }
    public Location getLocation() {
        return location;
    }
    public OffsetDateTime getCurrentTime() {
        return CurrentTime;
    }
    public void setLocation(Location l) {
        location = l;
    }
    public void setCurrentTime() {
        CurrentTime = OffsetDateTime.now();
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        Creature other = (Creature) otherObject;
        return getName().equals(other.getName()) ;
                /*&& getHunger() == other.getHunger()
                && getLocation() == other.getLocation();*/
    }

    public int hashCode() {
        return Objects.hash(name, Class);
    }

    @Override
    public int compareTo(Creature compared) {
            return this.getName().length() - compared.getName().length();
    }

    public String toString() {
        return "\n\t" + Class +" "+ getName();
    }
}