import javax.swing.*;
import java.awt.*;

/**
 * Shows map and log of events
 * Control of the game still happens via the terminal.
 */
public class RiskGUI {

    private final Risk_Map riskMap;
    private JFrame frame;
    private MapPanel mapPanel;
    private JTextArea logArea;

    public RiskGUI(Risk_Map riskMap) {
        this.riskMap = riskMap;
        SwingUtilities.invokeLater(this::createAndShow);
    }

    private void createAndShow() {
        frame = new JFrame("Risk - GUI View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mapPanel = new MapPanel(riskMap);
        logArea = new JTextArea(6, 60);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane logScroll = new JScrollPane(logArea);

        frame.setLayout(new BorderLayout());
        frame.add(mapPanel, BorderLayout.CENTER);
        frame.add(logScroll, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /** Request a repaint of the map. */
    public void updateMap() {
        if (mapPanel != null) {
            SwingUtilities.invokeLater(mapPanel::repaint);
        }
    }

    /** Append a message to the log area. */
    public void log(String msg) {
        if (logArea != null) {
            SwingUtilities.invokeLater(() -> {
                logArea.append(msg + "\n");
                logArea.setCaretPosition(logArea.getDocument().getLength());
            });
        }
    }

    public void closeWindow(){
        if(frame != null){
            SwingUtilities.invokeLater(() -> {
                frame.dispose();
            });
        }
    }
}
