import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

@Table(name = "Creatures")
public class Creature implements Comparable<Creature>, Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "family")
    private String family;

    @Column(name = "hunger")
    private int hunger;

    @Column(name = "location")
    private Location location;

    @Column(name = "creation_time")
    private OffsetDateTime creationTime;

    transient private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    private static final long serialVersionUID = -2308846153109753988L;
    Inventory inventory = new Inventory();

    Creature(String name, int hunger, Location location, OffsetDateTime creationTime, String family) {
        this.name = name;
        this.hunger = hunger;
        this.location = location;
        this.creationTime = creationTime;
        this.family = family;
    }

    String getFamily() {
        return family;
    }

    String getName() {
        return name;
    }

    int getHunger() {
        return hunger;
    }

    Location getLocation() {
        if (location == null) location = Location.NaN;
        return location;
    }

    OffsetDateTime getCreationTime() {
        if (creationTime == null) creationTime = OffsetDateTime.now();
        return creationTime;
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        Creature other = (Creature) otherObject;
        return getName().equals(other.getName());

    }

    public int hashCode() {
        return Objects.hash(name, family);
    }

    @Override
    public int compareTo(Creature compared) {
        return this.getName().length() - compared.getName().length();
    }

    public String toString() {
        return "\n" + getFamily() + " " + getName();
    }

    @Table(name ="Inventory" )
    class Inventory implements Serializable {
        @Column(name = "inventory")
        LinkedList<String> inventory = new LinkedList<>();

        private static final long serialVersionUID = -1484983106197509695L;

        void add(String a) {
            inventory.add(a);
        }

        void remove(String a) {
            inventory.remove(a);
        }

        LinkedList<String> getInventory() {
            return inventory;
        }
    }

    LinkedList<String> getInventory() {
        if (inventory == null) inventory = new Inventory();
        return inventory.getInventory();
    }
}