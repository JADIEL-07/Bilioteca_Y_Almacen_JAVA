package vista.componentes;

import javax.swing.*;
import java.awt.*;

public class PanelCristal extends JPanel {
    private boolean hovered = false;
    private boolean active = false;

    public PanelCristal() {
        setOpaque(false);
    }

    public void setHovered(boolean h) {
        this.hovered = h;
        repaint();
    }

    public void setActive(boolean a) {
        this.active = a;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo semi-transparente
        g2.setColor(new Color(15, 23, 42, 220));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        
        // Borde
        if (active || hovered) {
            g2.setColor(SenaColores.VERDE_SENA);
            g2.setStroke(new BasicStroke(2f));
        } else {
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1f));
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        
        g2.dispose();
        // NO llamamos a super.paintComponent() para no limpiar nuestro fondo pintado
    }
}
