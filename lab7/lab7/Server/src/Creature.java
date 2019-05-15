import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.Objects;

public class Creature implements Comparable<Creature>, Serializable {
    private static final long serialVersionUID = -2308846153109753988L;
    Inventory inventory = new Inventory();
    private String name;
    private int hunger;
    private Location location;
    private OffsetDateTime creationTime;
    private String family;

    Creature(String name, int hunger, Location location, OffsetDateTime creationTime, String family) {
        this.name = name;
        this.hunger = hunger;
        this.location = location;
        this.creationTime = creationTime;
        this.family = family;
    }

    Creature(String n, Location l) {
        name = n;
        location = l;
        creationTime = OffsetDateTime.now();
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

    class Inventory implements Serializable {
        private static final long serialVersionUID = -1484983106197509695L;
        LinkedList<String> inventory = new LinkedList<>();

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