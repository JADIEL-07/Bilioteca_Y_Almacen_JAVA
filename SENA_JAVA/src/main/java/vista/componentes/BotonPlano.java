package vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BotonPlano extends JButton {
    private Color colorNormal;
    private Color colorHover;
    private Color colorActual;
    private int radio = 12;

    public BotonPlano(String texto, Color colorNormal, Color colorHover) {
        super(texto);
        this.colorNormal = colorNormal;
        this.colorHover = colorHover;
        this.colorActual = colorNormal;
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { colorActual = colorHover; repaint(); }
            public void mouseExited(MouseEvent e) { colorActual = colorNormal; repaint(); }
        });
    }

    public BotonPlano(String texto) {
        this(texto, new Color(57, 169, 0), new Color(45, 134, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorActual);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width + 30, Math.max(d.height, 40));
    }
}
