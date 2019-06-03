import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

public class Creature implements Comparable<Creature>, Serializable {

    private String name;

    private String family;

    private int hunger;

    private Location location;

    private OffsetDateTime creationTime;

    private Integer x;

    private Integer y;

    private Integer size;

    private Colors color;

    transient private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    private static final long serialVersionUID = -2308846153109753988L;
    Inventory inventory = new Inventory();

    Creature(String name, int hunger, Location location, OffsetDateTime creationTime, String family, Integer x, Integer y, Integer size, Colors color) {
        this.name = name;
        this.hunger = hunger;
        this.location = location;
        this.creationTime = creationTime;
        this.family = family;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    Creature(String name, int hunger, Location location, String family, Integer x, Integer y, Integer size, Colors color) {
        this.name = name;
        this.hunger = hunger;
        this.location = location;
        this.creationTime = OffsetDateTime.now();
        this.family = family;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
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

    int getX(){return x;}
    int getY(){return y;}
    int getSize(){return size;}

    Location getLocation() {
        if (location == null) location = Location.NaN;
        return location;
    }

    OffsetDateTime getCreationTime() {
        if (creationTime == null) creationTime = OffsetDateTime.now();
        return creationTime;
    }

    Colors getColor() {
        return color;
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (getClass() != otherObject.getClass()) return false;
        Creature other = (Creature) otherObject;
        return getName().equals(other.getName()) &&
                getFamily().equals((other.getFamily()));

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