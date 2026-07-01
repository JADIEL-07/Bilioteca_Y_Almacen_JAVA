package vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Botón animado premium con efecto ripple al hacer clic,
 * gradiente verde SENA y hover suave con javax.swing.Timer.
 */
public class BotonAnimado extends JButton {

    private Color c1 = new Color(0, 180, 60);
    private Color c2 = new Color(0, 130, 30);
    private Color c1Hover = new Color(0, 210, 80);
    private Color c2Hover = new Color(0, 160, 50);
    private float hoverProgress = 0f;
    private Timer hoverTimer;

    // Ripple
    private int rippleX = -1, rippleY = -1;
    private float rippleRadius = 0f;
    private float rippleAlpha = 0f;
    private Timer rippleTimer;

    public BotonAnimado(String texto) {
        super(texto);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        hoverTimer = new Timer(12, null);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hoverTimer.stop();
                hoverTimer = new Timer(12, ev -> {
                    hoverProgress = Math.min(1f, hoverProgress + 0.08f);
                    repaint();
                    if (hoverProgress >= 1f) ((Timer)ev.getSource()).stop();
                });
                hoverTimer.start();
            }
            @Override public void mouseExited(MouseEvent e) {
                hoverTimer.stop();
                hoverTimer = new Timer(12, ev -> {
                    hoverProgress = Math.max(0f, hoverProgress - 0.08f);
                    repaint();
                    if (hoverProgress <= 0f) ((Timer)ev.getSource()).stop();
                });
                hoverTimer.start();
            }
            @Override public void mousePressed(MouseEvent e) {
                rippleX = e.getX(); rippleY = e.getY();
                rippleRadius = 0f; rippleAlpha = 0.4f;
                if (rippleTimer != null) rippleTimer.stop();
                rippleTimer = new Timer(10, ev -> {
                    rippleRadius += getWidth() * 0.06f;
                    rippleAlpha = Math.max(0f, rippleAlpha - 0.015f);
                    repaint();
                    if (rippleAlpha <= 0f) ((Timer)ev.getSource()).stop();
                });
                rippleTimer.start();
            }
        });
    }

    public BotonAnimado(String texto, Color c1, Color c2, Color c1Hover, Color c2Hover) {
        this(texto);
        this.c1 = c1; this.c2 = c2;
        this.c1Hover = c1Hover; this.c2Hover = c2Hover;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient interpolado según hover
        Color startColor = blend(c1, c1Hover, hoverProgress);
        Color endColor   = blend(c2, c2Hover, hoverProgress);

        GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

        // Sombra interior sutil
        g2.setColor(new Color(0,0,0,30));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);

        // Ripple
        if (rippleRadius > 0 && rippleAlpha > 0) {
            g2.setColor(new Color(1f, 1f, 1f, rippleAlpha));
            g2.fillOval((int)(rippleX - rippleRadius), (int)(rippleY - rippleRadius),
                        (int)(rippleRadius * 2), (int)(rippleRadius * 2));
        }

        g2.dispose();
        super.paintComponent(g);
    }

    private Color blend(Color a, Color b, float t) {
        int r = (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t);
        int gr= (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl= (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
        return new Color(Math.max(0,Math.min(255,r)), Math.max(0,Math.min(255,gr)), Math.max(0,Math.min(255,bl)));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width + 36, Math.max(d.height + 8, 42));
    }
}
