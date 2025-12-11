import java.util.*;

public class Country implements Comparable<Country> {
    private int troops;
    private Set<Country> neighbors;
    private String name;
    private Player owner; // which player controls this country (may be null)

    public Country(String name) {
        this.troops = 0;
        this.neighbors = new TreeSet<>();
        this.name = name;
        this.owner = null;
    }

    public int getTroops() {
        return troops;
    }

    // add a single troop
    public boolean addTroops() {
        this.troops += 1;
        return true;
    }

    // add a number of troops (positive to add, negative to remove)
    public boolean addTroops(int troops) {
        this.troops += troops;
        if (this.troops < 0) {
            this.troops = 0;
        }
        return true;
    }

    public Set<Country> getNeighbors() {
        return neighbors;
    }

    public boolean addNeighbor(Country neighbor) {
        if (neighbor == null || neighbor == this) {
            return false;
        }
        neighbors.add(neighbor);
        return true;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(Country other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country other = (Country) o;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        String ownerStr = (owner == null ? "-" : "P" + owner.getId());
        return name + " [troops=" + troops + ", owner=" + ownerStr + "]";
    }
}
