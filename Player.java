import java.util.Set;
import java.util.HashSet;

public class Player {
    private Set<Country> controlledCountries;
    private int deployableTroops;
    private final int id;

    public Player(int id, int initialTroops) {
        this.id = id;
        this.deployableTroops = initialTroops;
        this.controlledCountries = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public int getDeployableTroops() {
        return deployableTroops;
    }

    // Add troops to this player's reinforcement pool
    public void giveTroops(int troops) {
        if (troops <= 0) return;
        this.deployableTroops += troops;
    }

    // Spend troops from reinforcement pool
    public void deployTroops(int troops) {
        if (troops <= deployableTroops && troops > 0) {
            deployableTroops -= troops;
        }
    }

    public Set<Country> getControlledCountries() {
        return controlledCountries;
    }

    // Player takes control of the given country
    public boolean takeControl(Country country) {
        if (country == null) return false;
        if (controlledCountries.add(country)) {
            country.setOwner(this);
            return true;
        }
        return false;
    }

    // Player loses control of the given country
    public void loseControl(Country country) {
        if (country == null) return;
        if (controlledCountries.remove(country)) {
            country.setOwner(null);
        }
    }

    public boolean owns(Country country) {
        return country != null && controlledCountries.contains(country);
    }

    public boolean hasLost() {
        return controlledCountries.isEmpty();
    }

    @Override
    public String toString() {
        return "Player " + id + " [countries=" + controlledCountries.size() + ", reserve=" + deployableTroops + "]";
    }
}
