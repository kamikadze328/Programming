import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Objects;

public class Creature implements Comparable<Creature>, Serializable {
    private static final long serialVersionUID = -2308846153109753988L;
    Creature.Inventory inventory = new Creature.Inventory();
    private String name;
    private int hunger;
    private Location location;
    private OffsetDateTime creationTime;
    private String family;

    Creature(String n) {
        name = n;
        creationTime = OffsetDateTime.now();
    }

    Creature(String n, Location l) {
        name = n;
        location = l;
        creationTime = OffsetDateTime.now();
    }

    public String getFamily() {
        return family;
    }

    public String getName() {
        return name;
    }

    public int getHunger() {
        return hunger;
    }

    public Location getLocation() {
        if (location == null) location = Location.NaN;
        return location;
    }

    public void setLocation(Location l) {
        location = l;
    }

    public String getCreationTime() {
        if (creationTime == null) creationTime = OffsetDateTime.now();
        String dateFormat = creationTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ssX"));
        return dateFormat;
    }

    public void setCurrentTime() {
        creationTime = OffsetDateTime.now();
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        Creature other = (Creature) otherObject;
        return getName().equals(other.getName());
                /*&& getHunger() == other.getHunger()
                && getLocation() == other.getLocation();*/
    }

    public int hashCode() {
        return Objects.hash(name, family);
    }

    @Override
    public int compareTo(Creature compared) {
        return this.getName().length() - compared.getName().length();
    }

    public String toString() {
        return "" + getFamily() + " " + getName();
    }

    class Inventory implements Serializable {
        private static final long serialVersionUID = -1484983106197509695L;
        LinkedList<String> inventory = new LinkedList<>();

        protected void add(String a) {
            inventory.add(a);
            System.out.println(Creature.this.getName() + " поднимает " + a);
        }

        protected void remove(String a) {
            inventory.remove(a);
            System.out.println(Creature.this.getName() + " опускает " + a);
        }
    }
}