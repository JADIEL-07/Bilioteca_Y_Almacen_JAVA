package vista.componentes;

import javax.swing.*;
import java.awt.*;

public class PanelRedondeado extends JPanel {
    private int radio = 20;
    private Color colorFondo;

    public PanelRedondeado(Color colorFondo, int radio) {
        this.colorFondo = colorFondo;
        this.radio = radio;
        setOpaque(false);
    }

    public PanelRedondeado(Color colorFondo) {
        this(colorFondo, 20);
    }

    public PanelRedondeado() {
        this(new Color(30, 41, 59), 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorFondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();
        super.paintComponent(g);
    }

    public void setColorFondo(Color c) { this.colorFondo = c; repaint(); }
}
