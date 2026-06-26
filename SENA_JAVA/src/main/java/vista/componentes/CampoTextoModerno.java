package vista.componentes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CampoTextoModerno extends JTextField {
    private String placeholder;
    private boolean showingPlaceholder = true;
    private int iconType = 0; // 0 = none, 1 = user

    public CampoTextoModerno(String placeholder, int iconType) {
        this.placeholder = placeholder;
        this.iconType = iconType;
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(new Color(15, 23, 42));
        setForeground(new Color(148, 163, 184));
        setCaretColor(new Color(248, 250, 252));
        
        // Add extra left padding for the icon
        int leftPad = iconType > 0 ? 40 : 15;
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 25), 1, true),
            new EmptyBorder(10, leftPad, 10, 15)
        ));
        setText(placeholder);

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(new Color(248, 250, 252));
                    showingPlaceholder = false;
                }
            }
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(new Color(148, 163, 184));
                    showingPlaceholder = true;
                }
            }
        });
    }

    public CampoTextoModerno(String placeholder) {
        this(placeholder, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (iconType > 0) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(SenaColores.VERDE_SENA); // Green icon

            int y = getHeight() / 2;
            int x = 15; // Left margin

            if (iconType == 1) { // User icon
                g2.drawOval(x + 4, y - 8, 8, 8); // Head
                g2.drawArc(x, y + 2, 16, 12, 0, 180); // Shoulders
            } else if (iconType == 2) { // Document/ID Card icon
                g2.drawRoundRect(x, y - 7, 16, 12, 4, 4); // Card outline
                g2.drawLine(x, y - 2, x + 16, y - 2); // Magnetic stripe/separator
                g2.drawRect(x + 3, y + 1, 3, 2); // Small photo box
            } else if (iconType == 3) { // Email icon
                g2.drawRoundRect(x, y - 6, 16, 12, 2, 2); // Envelope outline
                g2.drawLine(x, y - 6, x + 8, y); // Flap left
                g2.drawLine(x + 16, y - 6, x + 8, y); // Flap right
            } else if (iconType == 4) { // Phone icon
                g2.drawRoundRect(x + 4, y - 8, 8, 14, 4, 4); // Phone body
                g2.drawLine(x + 6, y + 3, x + 10, y + 3); // Home button line
            }

            g2.dispose();
        }
    }

    public String getRealText() {
        return showingPlaceholder ? "" : getText();
    }
}
