import javax.swing.*;
import java.awt.*;

/**
 * Panel that draws the Risk map background and troop count
 */
public class MapPanel extends JPanel {

    private final Risk_Map riskMap;
    private final Image mapImage;

    public MapPanel(Risk_Map riskMap) {
        this.riskMap = riskMap;
        this.mapImage = new ImageIcon("Risk_Territories.jpeg").getImage();

        int w = mapImage.getWidth(null);
        int h = mapImage.getHeight(null);
        setPreferredSize(new Dimension(w, h));

        /*Debug helper: print coordinates on click
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Clicked at: " + e.getX() + ", " + e.getY());
            }
        });
        */
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, this);
        }

        if (riskMap == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Country c : riskMap.getCountries()) {
            Point p = CountryCoords.get(c.getName());
            if (p == null) {
                // If this printed, add/update a coord in CountryCoords
                System.out.println("No coords for " + c.getName());
                continue;
            }
            drawTroopBubble(g2, c, p.x, p.y);
        }
    }

    private void drawTroopBubble(Graphics2D g2, Country c, int x, int y) {
        int r = 22; // bubble diameter
        Player owner = c.getOwner();
        Color color = Color.LIGHT_GRAY;

        if (owner != null) {
            switch (owner.getId()) {
                case 1: color = new Color(200, 40, 40); break;  // P1 red
                case 2: color = new Color(40, 80, 200); break;  // P2 blue
                case 3: color = new Color(30, 150, 30); break;  // extra players
                default: color = Color.DARK_GRAY;
            }
        }

        // Shadow / outline
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillOval(x - 2, y - 2, r + 4, r + 4);

        // Fill
        g2.setColor(color);
        g2.fillOval(x, y, r, r);

        // Troop number
        String text = Integer.toString(c.getTroops());
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (r - fm.stringWidth(text)) / 2;
        int ty = y + (r + fm.getAscent()) / 2 - 3;
        g2.drawString(text, tx, ty);
    }
}
