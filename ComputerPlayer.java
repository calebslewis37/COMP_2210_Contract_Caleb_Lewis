import java.util.*;

public class ComputerPlayer extends Player {

    private Random rand = new Random();

    public ComputerPlayer(int id, int initialTroops) {
        super(id, initialTroops);
    }

    // Automatically place all reinforcement troops (used in setup + turns)
    public void autoPlaceAllReinforcements(Risk_Map map) {
        int pool = getDeployableTroops();
        if (pool <= 0) return;

        Country target = chooseWeakestBorder();
        if (target == null) {
            // No border countries? Pick any owned country.
            Set<Country> mine = getControlledCountries();
            if (mine.isEmpty()) return;
            target = mine.iterator().next();
        }

        target.addTroops(pool);
        deployTroops(pool);
        System.out.println("Computer placed " + pool + " troops on " + target.getName());
    }

    // Full computer-controlled turn
    public void takeTurn(Risk_Map map) {
        System.out.println("\n--- Computer Player " + getId() + " turn ---");


        autoPlaceAllReinforcements(map);


        performAttacks(map);


        fortify(map);

        System.out.println("Computer Player " + getId() + " ends turn.\n");
    }

    // ---------- AI HELPERS ----------

    // Pick the weakest border country (one that touches an enemy), breaking ties randomly
    private Country chooseWeakestBorder() {
        Country best = null;
        int bestTroops = Integer.MAX_VALUE;

        for (Country c : getControlledCountries()) {
            boolean isBorder = false;
            for (Country n : c.getNeighbors()) {
                if (!owns(n)) {
                    isBorder = true;
                    break;
                }
            }
            if (!isBorder) {
                continue;
            }

            // Heuristic: prefer the border with the fewest troops; if tied, pick randomly
            if (c.getTroops() < bestTroops) {
                bestTroops = c.getTroops();
                best = c;
            } else if (c.getTroops() == bestTroops && best != null && rand.nextBoolean()) {
                best = c;
            }
        }
        return best;
    }

    // Perform attacks using dice, choosing "good" attacks based on troop advantage
    private void performAttacks(Risk_Map map) {
        boolean attackedThisRound;

        do {
            attackedThisRound = false;
            Country bestFrom = null;
            Country bestTo = null;
            int bestAdvantage = 0;

            // Look for a favorable attack: attackers troops - defenders troops is large
            for (Country from : getControlledCountries()) {
                if (from.getTroops() < 2) continue;
                for (Country n : from.getNeighbors()) {
                    if (owns(n)) continue;
                    int advantage = from.getTroops() - n.getTroops();
                    if (advantage >= 2 && advantage > bestAdvantage) {
                        bestAdvantage = advantage;
                        bestFrom = from;
                        bestTo = n;
                    }
                }
            }

            if (bestFrom != null && bestTo != null) {
                attackedThisRound = true;
                System.out.println("Computer attacks " + bestTo.getName() + " from " + bestFrom.getName());

                // Repeat Risk-style battle until advantage is gone or territory is conquered
                boolean conquered = false;
                while (true) {
                    if (bestFrom.getTroops() < 2 || bestTo.getTroops() == 0) {
                        if (bestTo.getTroops() == 0) {
                            conquered = true;
                        }
                        break;
                    }

                    int maxAttackDice = Math.min(3, bestFrom.getTroops() - 1);
                    int maxDefendDice = Math.min(2, bestTo.getTroops());

                    int attackDiceCount = maxAttackDice; // AI uses maximum dice
                    int defendDiceCount = maxDefendDice; // defender uses maximum dice

                    int[] attackRolls = new int[attackDiceCount];
                    int[] defendRolls = new int[defendDiceCount];
                    for (int i = 0; i < attackDiceCount; i++) {
                        attackRolls[i] = rand.nextInt(6) + 1;
                    }
                    for (int i = 0; i < defendDiceCount; i++) {
                        defendRolls[i] = rand.nextInt(6) + 1;
                    }

                    Arrays.sort(attackRolls);
                    Arrays.sort(defendRolls);

                    // reverse to descending
                    for (int i = 0; i < attackRolls.length / 2; i++) {
                        int tmp = attackRolls[i];
                        attackRolls[i] = attackRolls[attackRolls.length - 1 - i];
                        attackRolls[attackRolls.length - 1 - i] = tmp;
                    }
                    for (int i = 0; i < defendRolls.length / 2; i++) {
                        int tmp = defendRolls[i];
                        defendRolls[i] = defendRolls[defendRolls.length - 1 - i];
                        defendRolls[defendRolls.length - 1 - i] = tmp;
                    }

                    int comparisons = Math.min(attackDiceCount, defendDiceCount);
                    int attackerLosses = 0;
                    int defenderLosses = 0;
                    for (int i = 0; i < comparisons; i++) {
                        if (attackRolls[i] > defendRolls[i]) {
                            defenderLosses++;
                        } else {
                            attackerLosses++;
                        }
                    }

                    bestFrom.addTroops(-attackerLosses);
                    bestTo.addTroops(-defenderLosses);

                    System.out.println("Roll result: attacker loses " + attackerLosses + ", defender loses " + defenderLosses + ".");
                    System.out.println(bestFrom.getName() + " now has " + bestFrom.getTroops() + " troops; " + bestTo.getName() + " now has " + bestTo.getTroops() + " troops.");

                    // Simple stopping heuristic: stop if advantage disappears
                    if (bestFrom.getTroops() <= bestTo.getTroops()) {
                        break;
                    }
                }

                if (conquered) {
                    System.out.println("Computer conquers " + bestTo.getName() + "!");
                    Player oldOwner = bestTo.getOwner();
                    if (oldOwner != null) {
                        oldOwner.loseControl(bestTo);
                    }
                    this.takeControl(bestTo);

                    int maxMove = bestFrom.getTroops() - 1;
                    int move = Math.max(1, maxMove); // move everything but one
                    bestFrom.addTroops(-move);
                    bestTo.addTroops(move);
                }
            }

        } while (attackedThisRound);
    }

    // Fortify the weakest border country using troops from a stronger adjacent ally
    private void fortify(Risk_Map map) {
        // Fortify the weakest border country using troops from a stronger neighbor that is owned
        Country target = chooseWeakestBorder();
        if (target == null) {
            return; 
        }

        Country source = null;
        int bestSourceTroops = 0;

        // Look at neighbors of the target that is owned and find the one with the most troops
        for (Country n : target.getNeighbors()) {
            if (!owns(n)) {
                continue;
            }
            int troops = n.getTroops();
            if (troops > 1 && troops > bestSourceTroops) { // must be able to move at least 1
                bestSourceTroops = troops;
                source = n;
            }
        }

        if (source == null) {
            // No suitable source country
            return;
        }

        int movable = source.getTroops() - 1; // must leave 1 behind
        if (movable <= 0) {
            return;
        }

        // Heuristic: move about half of the movable troops (at least 1)
        int move = Math.max(1, movable / 2);
        source.addTroops(-move); // remove from source
        target.addTroops(move); // add to target

        System.out.println("Computer fortifies " + target.getName() + " from " + source.getName() + " with " + move + " troops.");
    }
}
