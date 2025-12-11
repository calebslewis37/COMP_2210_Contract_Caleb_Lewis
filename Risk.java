import java.util.*;

public class Risk {

    private static final Scanner in = new Scanner(System.in);

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN   = "\u001B[36m";

    private static final Random RAND = new Random();


    public Risk(){}
    

    /**
     * Roll a given number of Risk dice (6-sided), return sorted descending.
     */
    private static int[] rollDice(int count) {
        int[] rolls = new int[count];
        for (int i = 0; i < count; i++) {
            rolls[i] = RAND.nextInt(6) + 1;
        }
        // sort ascending then reverse to descending
        Arrays.sort(rolls);
        for (int i = 0; i < rolls.length / 2; i++) {
            int tmp = rolls[i];
            rolls[i] = rolls[rolls.length - 1 - i];
            rolls[rolls.length - 1 - i] = tmp;
        }
        return rolls;
    }

    private static RiskGUI gui;

    public static void requestGuiUpdate() {
        if (gui != null) {
            gui.updateMap();
        }
    }

    public int playGame(){
        Risk_Map riskMap = new Risk_Map();
        gui = new RiskGUI(riskMap); // comment out to disable GUI

        // Test w/ two players
        // Human vs Computer
        Player p1 = new ComputerPlayer(1, 0);              // human
        Player p2 = new ComputerPlayer(2, 0);      // AI
        List<Player> players = Arrays.asList(p1, p2);

        System.out.println("Welcome to Terminal Risk-lite!\n");

        //0. Initial SETUP:        

        // 1. Assign countries alternately and give 1 starting troop to each
        initialCountryAssignment(riskMap, players);

        // 2. Give each player some extra troops to place
        initialTroopPlacement(riskMap, players, 10);




        //1. GAME LOOP:

        //      1. Main game loop
        int current = 0;
        while (true) {
            Player currentPlayer = players.get(current);


            if (hasWon(riskMap, currentPlayer)) {
                System.out.println("\n*** Player " + currentPlayer.getId() + " WINS! ***");
                printMap(riskMap);
                return currentPlayer.getId();
            }

            // Skip players who have lost all countries
            if (currentPlayer.hasLost()) {
                current = (current + 1) % players.size();
                continue;
            }

            

            printTurnBanner(currentPlayer);
            printPlayerStatus(currentPlayer);

            // Basic Risk rule: reinforcements = max(3, #countries / 3)
            // ie get at least 3, more if you control more countries
            
            int reinforcement = Math.max(3, currentPlayer.getControlledCountries().size() / 3);
            currentPlayer.giveTroops(reinforcement);

            if (currentPlayer instanceof ComputerPlayer) {
                System.out.println("Computer Player " + currentPlayer.getId() + " receives " + reinforcement + " reinforcement troops.");
                ((ComputerPlayer) currentPlayer).takeTurn(riskMap);
                //update gui
                requestGuiUpdate();
            } else {
                System.out.println("You receive " + reinforcement + " reinforcement troops.");
                reinforcementPhase(currentPlayer);
                attackPhase(riskMap, currentPlayer);
                fortifyPhase(riskMap, currentPlayer);
                requestGuiUpdate();
            }

            current = (current + 1) % players.size();
            //sleep(1000); // small delay to watch computer turns, commented out to allow for faster stats runs
        }
    }

    public void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Risk game = new Risk();
        System.out.println(game.playGame());
        game.closeWindow();
    }

    public void closeWindow(){
        if (gui != null) {
            gui.closeWindow();
        }
    }

    //Setup phases

    /**
    * Assign countries to players in round-robin fashion, 1 troop each
    * @param map The Risk_Map instance
    * @param players List of players
    * 
    */
    private static void initialCountryAssignment(Risk_Map map, List<Player> players) {
        List<Country> all = new ArrayList<>(map.getCountries());
        

        /* 
        int idx = 0;
        for (Country c : all) {
            Player owner = players.get(idx);
            owner.takeControl(c);
            c.addTroops(1); // 1 starting troop on each
            idx = (idx + 1) % players.size();
        }*/
        Random rand = new Random();
        players.get(0).takeControl(all.get(rand.nextInt(all.size())));
        all.get(0).addTroops(1);
        players.get(1).takeControl(all.get(rand.nextInt(all.size())));
        all.get(1).addTroops(1);

        System.out.println("Initial country assignment complete.\n");
        printMap(map);
    }


    /**
    * Initial troop placement phase: each player places extra troops on their countries
    * @param map The Risk_Map instance
    * @param players List of players
    * @param extraTroops Number of extra troops each player gets to place
    */
    private static void initialTroopPlacement(Risk_Map map, List<Player> players, int extraTroops) {
        for (Player p : players) {
            p.giveTroops(extraTroops);

            if (p instanceof ComputerPlayer) {
                System.out.println("\nComputer Player " + p.getId()
                        + " placing " + extraTroops + " initial troops...");
                ((ComputerPlayer) p).autoPlaceAllReinforcements(map);
            } else {
                System.out.println("\nPlayer " + p.getId() + " - place your " + extraTroops + " extra troops.");
                while (p.getDeployableTroops() > 0) {
                    printPlayerCountries(p);
                    System.out.println("You have " + p.getDeployableTroops() + " troops left to place.");
                    System.out.print("Enter country name to place 1 troop (or 'skip'): ");
                    String name = in.nextLine().trim();
                    if (name.equalsIgnoreCase("skip")) {
                        break;
                    }
                    Country c = findOwnedCountryByName(p, name);
                    if (c == null) {
                        System.out.println("Invalid country. Must be one you own.");
                        continue;
                    }
                    c.addTroops(1);
                    p.deployTroops(1);
                    requestGuiUpdate();
                }
            }
        }
    }

    // Turn phases


    /**
     * Reinforcement phase: player places their deployable troops
     * @param player
     */
    private static void reinforcementPhase(Player player) {
        while (player.getDeployableTroops() > 0) {
            printPlayerCountries(player);
            System.out.println("Reinforcements left: " + player.getDeployableTroops());
            System.out.print("Enter country name to place 1 troop (or 'done'): ");
            String name = in.nextLine().trim();
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            Country target = findOwnedCountryByName(player, name);
            if (target == null) {
                System.out.println("You don't own that country or it doesn't exist.");
                continue;
            }
            target.addTroops(1);
            player.deployTroops(1);
            requestGuiUpdate();
        }
    }


    /**
     * Fortification phase: player may move troops between owned adjacent countries
     * @param map The Risk_Map instance
     * @param player The current player
     */
    
    private static void fortifyPhase(Risk_Map map, Player player) {
    System.out.print("Do you want to fortify (move troops between your countries)? (y/n): ");
    String ans = in.nextLine().trim();
    if (!ans.equalsIgnoreCase("y")) {
        return;
    }

    // Choose FROM country
    printPlayerCountries(player);
    System.out.print("Choose a source country to move troops FROM: ");
    String fromName = in.nextLine().trim();
    Country from = findOwnedCountryByName(player, fromName);
    if (from == null) {
        System.out.println("Invalid source country.");
        return;
    }
    if (from.getTroops() < 2) {
        System.out.println("You must leave at least 1 troop behind; need at least 2 to move.");
        return;
    }

    // Show valid neighbors for fortify
    List<Country> eligibleTargets = new ArrayList<>();
    System.out.println("Adjacent countries you own:");
    for (Country n : from.getNeighbors()) {
        if (player.owns(n)) {
            eligibleTargets.add(n);
            System.out.println("  - " + n);
        }
    }
    if (eligibleTargets.isEmpty()) {
        System.out.println("No adjacent countries you own to move troops to.");
        return;
    }

    // Choose TO country
    System.out.print("Choose a destination country to move troops TO: ");
    String toName = in.nextLine().trim();
    Country to = null;
    for (Country c : eligibleTargets) {
        if (c.getName().equalsIgnoreCase(toName)) {
            to = c;
            break;
        }
    }
    if (to == null) {
        System.out.println("Invalid destination country.");
        return;
    }

    // Choose number of troops
    int maxMovable = from.getTroops() - 1; // must leave 1 behind
    System.out.print("How many troops do you want to move? (1-" + maxMovable + "): ");
    String numStr = in.nextLine().trim();
    int amount;
    try {
        amount = Integer.parseInt(numStr);
    } catch (NumberFormatException e) {
        System.out.println("Not a valid number.");
        return;
    }
    if (amount < 1 || amount > maxMovable) {
        System.out.println("Invalid amount.");
        return;
    }

    from.addTroops(-amount);
    to.addTroops(amount);
    System.out.println("Moved " + amount + " troops from " + from.getName() + " to " + to.getName());
    requestGuiUpdate();

    printMap(map);
}

    /**
     * Attack phase: player may choose to attack adjacent countries
     * @param map The Risk_Map instance
     * @param attacker The attacking player
     */
    private static void attackPhase(Risk_Map map, Player attacker) {
        while (true) {
            System.out.print("Do you want to declare an attack? (y/n): ");
            String ans = in.nextLine().trim();
            if (!ans.equalsIgnoreCase("y")) {
                return; // end attack phase
            }

            // Choose attacking country
            printPlayerCountries(attacker);
            System.out.print("Choose an attacking country: ");
            String fromName = in.nextLine().trim();
            Country from = findOwnedCountryByName(attacker, fromName);
            if (from == null) {
                System.out.println("Invalid attacking country.");
                continue;
            }
            if (from.getTroops() < 2) {
                System.out.println("You need at least 2 troops to attack from " + from.getName());
                continue;
            }

            // Choose target country
            System.out.println("Neighbors of " + from.getName() + ":");
            for (Country n : from.getNeighbors()) {
                System.out.println("  - " + n);
            }

            System.out.print("Choose a target country: ");
            String toName = in.nextLine().trim();
            Country to = map.getCountryByName(toName);
            if (to == null || !from.getNeighbors().contains(to)) {
                System.out.println("Invalid target (must be adjacent).");
                continue;
            }
            if (to.getOwner() != null && to.getOwner().getId() == attacker.getId()) {
                System.out.println("You already own that country.");
                continue;
            }

            // Risk-style repeated battle between from and to
            boolean conquered = false;
            while (true) {
                if (from.getTroops() < 2) {
                    System.out.println("You don't have enough troops left in " + from.getName() + " to continue this attack.");
                    break;
                }
                if (to.getTroops() == 0) {
                    conquered = true;
                    break;
                }

                int maxAttackDice = Math.min(3, from.getTroops() - 1);
                int maxDefendDice = Math.min(2, to.getTroops());

                // Ask attacker how many dice to roll (1..maxAttackDice). Defender rolls max allowed.
                int attackDiceCount = maxAttackDice;
                if (maxAttackDice > 1) {
                    System.out.print("How many dice do you want to roll (1-" + maxAttackDice + ")? ");
                    String diceStr = in.nextLine().trim();
                    try {
                        int choice = Integer.parseInt(diceStr);
                        if (choice >= 1 && choice <= maxAttackDice) {
                            attackDiceCount = choice;
                        }
                    } catch (NumberFormatException e) {
                        // fall back to maxAttackDice
                        
                    }
                }
                int defendDiceCount = maxDefendDice; // defender uses maximum allowed

                int[] attackRolls = rollDice(attackDiceCount);
                int[] defendRolls = rollDice(defendDiceCount);

                System.out.print("Attacker rolls: ");
                for (int r : attackRolls) {
                    System.out.print(r + " ");
                }
                System.out.println();

                System.out.print("Defender rolls: ");
                for (int r : defendRolls) {
                    System.out.print(r + " ");
                }
                System.out.println();

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

                from.addTroops(-attackerLosses);
                to.addTroops(-defenderLosses);
                requestGuiUpdate();

                String rollResult = "Result: " + from.getName() + " -> " + to.getName()
                        + " | attacker loses " + attackerLosses
                        + ", defender loses " + defenderLosses + ".";
                String troopStatus = from.getName() + " now has " + from.getTroops()
                        + " troops; " + to.getName() + " now has " + to.getTroops() + " troops.";

                System.out.println(rollResult);
                System.out.println(troopStatus);

                if (gui != null) {
                    gui.log(rollResult);
                    gui.log(troopStatus);
                }

                if (to.getTroops() == 0) {
                    conquered = true;
                    break;
                }
                if (from.getTroops() < 2) {
                    System.out.println("You no longer have enough troops in " + from.getName() + " to continue attacking.");
                    break;
                }

                System.out.print("Do you want to continue attacking " + to.getName() + "? (y/n): ");
                String cont = in.nextLine().trim();
                if (!cont.equalsIgnoreCase("y")) {
                    break;
                }
            }

            if (conquered) {
                System.out.println("Territory conquered!");
                if (gui != null) {
                    gui.log("Territory conquered: " + to.getName()
                            + " now belongs to Player " + attacker.getId());
                }

                Player oldOwner = to.getOwner();
                if (oldOwner != null) {
                    oldOwner.loseControl(to);
                }
                attacker.takeControl(to);

                // Move troops into conquered territory.
                int maxMove = from.getTroops() - 1;
                int minMove = 1;
                if (maxMove < 1) {
                    maxMove = 1;
                }
                System.out.print("How many troops do you want to move into " + to.getName() +
                                 " ? (" + minMove + "-" + maxMove + "): ");
                int move = minMove;
                String moveStr = in.nextLine().trim();
                try {
                    int choice = Integer.parseInt(moveStr);
                    if (choice >= minMove && choice <= maxMove) {
                        move = choice;
                    }
                } catch (NumberFormatException e) {
                    // use default
                }
                from.addTroops(-move);
                to.addTroops(move);
                requestGuiUpdate();
            }

            printMap(map);

            // quick win check inside attack phase
            if (hasWon(map, attacker)) {
                System.out.println("\n*** Player " + attacker.getId() + " WINS! ***");
                System.exit(0);
            }
        }
    }


    // Helper methods


    /**
     * Check if the given player has won by controlling all countries
     * @param map
     * @param player
     * @return true if player controls all countries
     */
    private static boolean hasWon(Risk_Map map, Player player) {
        for (Country c : map.getCountries()) {
            if (c.getOwner() != player) {
                return false;
            }
        }
        return true;
    }

    /**
     * Print the current map status
     * @param map
     */
    private static void printMap(Risk_Map map) {
        System.out.println();
        System.out.println(ANSI_BLUE + "=== CURRENT MAP STATE ===" + ANSI_RESET);

        // Determine dynamic column width for the country name
        int maxNameLen = getMaxCountryNameLength(map.getCountries());
        String countryHeader = "Country";
        int nameWidth = Math.max(countryHeader.length(), maxNameLen) + 2; // +2 for padding

        String ownerHeader = "Owner";
        String troopsHeader = "Troops";
        String neighHeader = "Neighbors";

        String headerFormat = "%-" + nameWidth + "s %-10s %-8s %-30s%n";
        String rowFormat = "%-" + nameWidth + "s %-10s %-8d %-30s%n";

        System.out.printf(headerFormat, countryHeader, ownerHeader, troopsHeader, neighHeader);
        
        int totalWidth = nameWidth + 1 + 10 + 1 + 8 + 1 + 30;
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("-");
        }
        System.out.println();

        // Print each country's info
        for (Country c : map.getCountries()) {
            String owner = (c.getOwner() == null) ? "-" : "P" + c.getOwner().getId();
            StringBuilder neigh = new StringBuilder();
            for (Country n : c.getNeighbors()) {
                if (neigh.length() > 0) {
                    neigh.append(", ");
                }
                neigh.append(n.getName());
            }
            System.out.printf(rowFormat, c.getName(), owner, c.getTroops(), neigh.toString());
        }
        requestGuiUpdate();
    }

    /**
     * Print the status of the given player
     * @param p
     */
    private static void printPlayerStatus(Player p) {
        System.out.println(ANSI_GREEN + p.toString() + ANSI_RESET);
        printPlayerCountries(p);
    }


    /**
     * Print the countries controlled by the given player
     * @param p The player
     */
    private static void printPlayerCountries(Player p) {
        System.out.println("Your countries:");

        int maxNameLen = getMaxCountryNameLength(p.getControlledCountries());
        String countryHeader = "Country";
        int nameWidth = Math.max(countryHeader.length(), maxNameLen) + 2;

        String troopsHeader = "Troops";
        String neighHeader = "Neighbors";

        String headerFormat = "%-" + nameWidth + "s %-8s %-30s%n";
        String rowFormat    = "%-" + nameWidth + "s %-8d %-30s%n";

        System.out.printf(headerFormat, countryHeader, troopsHeader, neighHeader);
        int totalWidth = nameWidth + 1 + 8 + 1 + 30;
        for (int i = 0; i < totalWidth; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (Country c : p.getControlledCountries()) {
            StringBuilder neigh = new StringBuilder();
            for (Country n : c.getNeighbors()) {
                if (neigh.length() > 0) {
                    neigh.append(", ");
                }
                neigh.append(n.getName());
            }
            System.out.printf(rowFormat,
                              c.getName(), c.getTroops(), neigh.toString());
        }
    }

    /**
     * Find a country owned by the player by name (case-insensitive)
     * @param p The player
     * @param name The country name
     * @return The Country if found, else null
     */
    private static Country findOwnedCountryByName(Player p, String name) {
        for (Country c : p.getControlledCountries()) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Compute the maximum length of any country name in a collection.
     * @param countries The collection of countries
     * @return The maximum name length
     */
    private static int getMaxCountryNameLength(Collection<Country> countries) {
        int max = 0;
        for (Country c : countries) {
            if (c != null && c.getName() != null) {
                max = Math.max(max, c.getName().length());
            }
        }
        return max;
    }

    /**
     * Print a centered banner for the current player's turn.
     * @param p the current player
     */
    private static void printTurnBanner(Player p) {
        String label = (p instanceof ComputerPlayer) ? "COMPUTER PLAYER " : "PLAYER ";
        String text = label + p.getId() + " TURN";
        String line = "==============================================";
        System.out.println();
        System.out.println(ANSI_CYAN + line + ANSI_RESET);
        System.out.println(ANSI_YELLOW + centerText(text, line.length()) + ANSI_RESET);
        System.out.println(ANSI_CYAN + line + ANSI_RESET);
    }

    /**
     * Center text within a given width using spaces.
     */
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(' ');
        }
        sb.append(text);
        return sb.toString();
    }
}
