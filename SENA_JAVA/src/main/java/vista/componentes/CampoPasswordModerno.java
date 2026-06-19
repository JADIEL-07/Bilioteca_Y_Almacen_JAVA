package vista.componentes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class CampoPasswordModerno extends JPasswordField {
    private String placeholder;
    private boolean showingPlaceholder = true;
    private boolean passwordVisible = false;

    public CampoPasswordModerno(String placeholder) {
        this.placeholder = placeholder;
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(new Color(15, 23, 42));
        setForeground(new Color(148, 163, 184));
        setCaretColor(new Color(248, 250, 252));
        
        setEchoChar((char) 0); // Initially show placeholder as normal text
        setText(placeholder);

        // Padding: Left for lock icon (40), Right for eye icon (40)
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 25), 1, true),
            new EmptyBorder(10, 40, 10, 40)
        ));

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.WHITE);
                    setEchoChar(passwordVisible ? (char) 0 : '•');
                    showingPlaceholder = false;
                }
            }
            public void focusLost(FocusEvent e) {
                if (getPassword().length == 0) {
                    setText(placeholder);
                    setForeground(new Color(148, 163, 184));
                    setEchoChar((char) 0);
                    showingPlaceholder = true;
                }
            }
        });

        // Mouse listener for the eye icon (toggle visibility)
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                if (x >= getWidth() - 35 && x <= getWidth() - 10) {
                    if (!showingPlaceholder) {
                        passwordVisible = !passwordVisible;
                        setEchoChar(passwordVisible ? (char) 0 : '•');
                        repaint();
                    }
                }
            }
        });
        
        // Cursor change when hovering the eye icon
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                if (x >= getWidth() - 35 && x <= getWidth() - 10) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int y = getHeight() / 2;

        // Draw left lock icon (Green)
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(SenaColores.VERDE_SENA);
        int lx = 15;
        g2.drawRoundRect(lx + 2, y - 2, 10, 8, 2, 2); // Body
        g2.drawArc(lx + 4, y - 6, 6, 8, 0, 180); // Shackle
        g2.drawLine(lx + 7, y + 1, lx + 7, y + 3); // Keyhole

        // Draw right eye icon (Gray/White)
        g2.setColor(passwordVisible ? Color.WHITE : new Color(148, 163, 184));
        int rx = getWidth() - 28;
        
        // Eye shape (Oval)
        g2.drawOval(rx - 8, y - 5, 16, 10);
        
        // Pupil
        if (passwordVisible) {
            g2.fillOval(rx - 3, y - 2, 6, 6);
        } else {
            // Slash if not visible
            g2.drawLine(rx - 8, y + 5, rx + 8, y - 5);
        }

        g2.dispose();
    }
}
