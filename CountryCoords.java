import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds approximate screen coordinates for each Risk territory
 * for the 736x439 Risk_Territories.jpeg background image.
 *
 * Keys MUST match Country.getName() strings
 */
public class CountryCoords {

    private static final Map<String, Point> COORDS = new HashMap<>();

    static {
        // ---- North America ----
        COORDS.put("Alaska",              new Point(59, 108));
        COORDS.put("Northwest Territory", new Point(122, 100));
        COORDS.put("Greenland",           new Point(263, 76));
        COORDS.put("Alberta",             new Point(119, 146));
        COORDS.put("Ontario",             new Point(168, 158));
        COORDS.put("Quebec",              new Point(219, 157));
        COORDS.put("Western United States", new Point(120, 201));
        COORDS.put("Eastern United States", new Point(181, 218));
        COORDS.put("Central America",     new Point(128, 266));

        // ---- South America ----
        COORDS.put("Venezuela",           new Point(183, 307));
        COORDS.put("Peru",                new Point(157, 357));
        COORDS.put("Brazil",              new Point(244, 350));
        COORDS.put("Argentina",           new Point(199, 425));

        // ---- Europe ----
        COORDS.put("Iceland",             new Point(320, 134));
        COORDS.put("Scandinavia",         new Point(384, 120));
        COORDS.put("Great Britain",       new Point(316, 200));
        COORDS.put("Northern Europe",     new Point(374, 200));
        COORDS.put("Western Europe",      new Point(322, 258));
        COORDS.put("Southern Europe",     new Point(384, 243));
        // On the board this is labeled as Russia; in code it's Ukraine
        COORDS.put("Ukraine",             new Point(440, 172));

        // ---- Africa ----
        COORDS.put("North Africa",        new Point(352, 341));
        COORDS.put("Egypt",               new Point(405, 313));
        COORDS.put("East Africa",         new Point(448, 371));
        COORDS.put("Congo",               new Point(400, 399));
        COORDS.put("South Africa",        new Point(407, 459));
        COORDS.put("Madagascar",          new Point(472, 459));

        // ---- Asia ----
        COORDS.put("Ural",                new Point(520, 154));
        COORDS.put("Siberia",             new Point(560, 111));
        COORDS.put("Yakutsk",             new Point(613, 91));
        COORDS.put("Kamchatka",           new Point(672, 94));
        COORDS.put("Japan",               new Point(680, 204));
        COORDS.put("Mongolia",            new Point(602, 204));
        COORDS.put("Irkutsk",             new Point(601, 156));
        COORDS.put("China",               new Point(594, 251));
        COORDS.put("India",               new Point(539, 288));
        COORDS.put("Afghanistan",         new Point(503, 216));
        COORDS.put("Middle East",         new Point(463, 281));
        COORDS.put("Southeast Asia",      new Point(599, 311));

        // ---- Australia / Oceania ----
        COORDS.put("Indonesia",           new Point(609, 390));
        COORDS.put("New Guinea",          new Point(674, 366));
        COORDS.put("Western Australia",   new Point(632, 463));
        COORDS.put("Eastern Australia",   new Point(698, 440));
    }

    public static Point get(String countryName) {
        return COORDS.get(countryName);
    }
}
