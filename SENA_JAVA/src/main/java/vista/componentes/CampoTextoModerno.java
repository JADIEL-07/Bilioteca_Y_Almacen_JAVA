package vista.componentes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Campo de texto Material Design con etiqueta flotante animada.
 * - La etiqueta flota hacia arriba al hacer click / cuando hay texto.
 * - El borde inferior se resalta en verde al enfocar.
 * - Reemplaza el viejo placeholder que desaparecía al hacer click.
 */
public class CampoTextoModerno extends JPanel {

    private JTextField campo;
    private String etiqueta;
    private int iconType;

    // Estado de animación de la etiqueta flotante
    private float labelY;          // posición Y actual de la etiqueta
    private float labelSize;       // tamaño actual de fuente
    private float labelAlpha;      // transparencia
    private boolean focused = false;
    private boolean hasText = false;

    // Animación
    private Timer animTimer;
    private static final int LABEL_Y_TOP  = 4;   // posición cuando flota arriba
    private static final int LABEL_Y_BASE = 16;  // posición base (dentro del campo)
    private static final float LABEL_SIZE_SMALL = 10f;
    private static final float LABEL_SIZE_LARGE = 14f;

    // Color del borde inferior
    private float borderAlpha = 0f;

    public CampoTextoModerno(String etiqueta, int iconType) {
        this.etiqueta = etiqueta;
        this.iconType = iconType;

        // Estado inicial
        labelY    = LABEL_Y_BASE;
        labelSize = LABEL_SIZE_LARGE;
        labelAlpha = 1f;

        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(200, 52));

        // Campo interno sin decoración propia
        campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                // Solo pintamos el texto, sin fondo propio
                g.setColor(new Color(248, 250, 252));
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(new Color(248, 250, 252));
        campo.setCaretColor(new Color(0, 230, 118));
        campo.setBorder(new EmptyBorder(22, iconType > 0 ? 38 : 12, 4, 12));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focused = true;
                animarHaciaArriba();
            }
            @Override
            public void focusLost(FocusEvent e) {
                focused = false;
                if (campo.getText().isEmpty()) {
                    animarHaciaAbajo();
                }
            }
        });

        campo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { hasText = !campo.getText().isEmpty(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { hasText = !campo.getText().isEmpty(); if (!hasText && !focused) animarHaciaAbajo(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        add(campo, BorderLayout.CENTER);
    }

    public CampoTextoModerno(String etiqueta) {
        this(etiqueta, 0);
    }

    // ── Animación ──────────────────────────────────────────────
    private void animarHaciaArriba() {
        detenerTimer();
        animTimer = new Timer(8, e -> {
            labelY    = lerp(labelY,    LABEL_Y_TOP,       0.18f);
            labelSize = lerp(labelSize, LABEL_SIZE_SMALL,  0.18f);
            borderAlpha = lerp(borderAlpha, 1f, 0.18f);
            repaint();
            if (Math.abs(labelY - LABEL_Y_TOP) < 0.5f) {
                labelY = LABEL_Y_TOP; labelSize = LABEL_SIZE_SMALL; borderAlpha = 1f;
                detenerTimer(); repaint();
            }
        });
        animTimer.start();
    }

    private void animarHaciaAbajo() {
        detenerTimer();
        animTimer = new Timer(8, e -> {
            labelY    = lerp(labelY,    LABEL_Y_BASE,      0.18f);
            labelSize = lerp(labelSize, LABEL_SIZE_LARGE,  0.18f);
            borderAlpha = lerp(borderAlpha, 0f, 0.18f);
            repaint();
            if (Math.abs(labelY - LABEL_Y_BASE) < 0.5f) {
                labelY = LABEL_Y_BASE; labelSize = LABEL_SIZE_LARGE; borderAlpha = 0f;
                detenerTimer(); repaint();
            }
        });
        animTimer.start();
    }

    private void detenerTimer() {
        if (animTimer != null) { animTimer.stop(); animTimer = null; }
    }

    private float lerp(float a, float b, float t) { return a + (b - a) * t; }

    // ── Pintura ────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // Fondo
        g2.setColor(new Color(15, 23, 42));
        g2.fillRoundRect(0, 0, w, h, 10, 10);

        // Borde exterior tenue
        g2.setColor(new Color(255, 255, 255, 30));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

        // Borde inferior animado (verde al enfocar)
        if (borderAlpha > 0.01f) {
            Color verde = new Color(0, 230, 118, (int)(255 * borderAlpha));
            g2.setColor(verde);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(3, h - 2, w - 3, h - 2);
        }

        // Ícono
        if (iconType > 0) {
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(0, 230, 118, (int)(180 + 75 * borderAlpha)));
            int ix = 12, iy = h / 2;
            if (iconType == 1) {
                g2.drawOval(ix + 3, iy - 8, 8, 8);
                g2.drawArc(ix, iy + 2, 14, 10, 0, 180);
            } else if (iconType == 2) {
                g2.drawRoundRect(ix, iy - 7, 14, 10, 3, 3);
                g2.drawLine(ix, iy - 2, ix + 14, iy - 2);
                g2.drawRect(ix + 2, iy + 1, 3, 2);
            } else if (iconType == 3) {
                g2.drawRoundRect(ix, iy - 6, 14, 10, 2, 2);
                g2.drawLine(ix, iy - 6, ix + 7, iy);
                g2.drawLine(ix + 14, iy - 6, ix + 7, iy);
            } else if (iconType == 4) {
                g2.drawRoundRect(ix + 3, iy - 8, 8, 14, 4, 4);
                g2.drawLine(ix + 5, iy + 3, ix + 9, iy + 3);
            }
        }

        // Etiqueta flotante
        boolean arriba = focused || hasText;
        Color lblColor = arriba
            ? new Color(0, 230, 118, (int)(150 + 105 * borderAlpha))   // verde cuando activo
            : new Color(148, 163, 184);                                   // gris cuando inactivo

        g2.setFont(new Font("Segoe UI", Font.PLAIN, (int) labelSize));
        g2.setColor(lblColor);
        int textX = (iconType > 0 ? 38 : 12);
        g2.drawString(etiqueta, textX, (int) labelY + (int) labelSize);

        g2.dispose();
    }

    // ── API pública ────────────────────────────────────────────
    public String getText() {
        return campo.getText();
    }

    public void setText(String text) {
        campo.setText(text);
        hasText = !text.isEmpty();
        if (hasText) { labelY = LABEL_Y_TOP; labelSize = LABEL_SIZE_SMALL; borderAlpha = 1f; }
        else         { labelY = LABEL_Y_BASE; labelSize = LABEL_SIZE_LARGE; borderAlpha = 0f; }
        repaint();
    }

    /** Equivalente a getRealText() del componente anterior. */
    public String getRealText() {
        return campo.getText();
    }

    public void addKeyListener(KeyListener kl) {
        campo.addKeyListener(kl);
    }

    public void setControlador(java.awt.event.ActionListener al) {
        campo.addActionListener(al);
    }

    /** Permite que el campo reciba el foco al hacer Tab. */
    @Override
    public boolean isFocusable() { return false; }

    public JTextField getCampo() { return campo; }
}
