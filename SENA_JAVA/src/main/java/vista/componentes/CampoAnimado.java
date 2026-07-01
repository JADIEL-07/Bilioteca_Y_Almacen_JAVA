package vista.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Campo de texto premium con animación de foco (borde verde que aparece suavemente),
 * label flotante que sube al escribir (efecto Material Design) y placeholder.
 */
public class CampoAnimado extends JPanel {

    private JTextField campo;
    private JLabel labelFloat;
    private String placeholder;

    // Color states
    private static final Color BORDER_NORMAL = new Color(55, 65, 81);
    private static final Color BORDER_FOCUS  = new Color(0, 180, 60);
    private static final Color BG_COLOR      = new Color(17, 24, 39);
    private static final Color TEXT_COLOR    = Color.WHITE;
    private static final Color PLACEHOLDER_COLOR = new Color(107, 114, 128);
    private static final Color LABEL_FLOAT_COLOR = new Color(0, 200, 70);

    private float focusProgress = 0f;
    private Timer focusTimer;
    private boolean hasFocus = false;
    private boolean hasText  = false;

    public CampoAnimado(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(300, 60));

        labelFloat = new JLabel(placeholder);
        labelFloat.setForeground(PLACEHOLDER_COLOR);
        labelFloat.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        campo = new JTextField();
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        campo.setForeground(TEXT_COLOR);
        campo.setCaretColor(BORDER_FOCUS);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        add(labelFloat);
        add(campo);

        focusTimer = new Timer(10, null);

        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                hasFocus = true;
                animateFocus(true);
            }
            @Override public void focusLost(FocusEvent e) {
                hasFocus = false;
                hasText = !campo.getText().isEmpty();
                if (!hasText) animateFocus(false);
                repaint();
            }
        });

        campo.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                hasText = !campo.getText().isEmpty();
                repaint();
            }
        });
    }

    private void animateFocus(boolean show) {
        focusTimer.stop();
        focusTimer = new Timer(10, ev -> {
            if (show) {
                focusProgress = Math.min(1f, focusProgress + 0.08f);
            } else {
                focusProgress = Math.max(0f, focusProgress - 0.08f);
            }
            repaint();
            if ((show && focusProgress >= 1f) || (!show && focusProgress <= 0f)) {
                ((Timer)ev.getSource()).stop();
            }
        });
        focusTimer.start();
    }

    @Override
    public void doLayout() {
        int w = getWidth(); int h = getHeight();
        // Label flotante
        boolean floating = hasFocus || hasText;
        if (floating) {
            labelFloat.setBounds(12, 6, w - 24, 18);
            labelFloat.setFont(new Font("Segoe UI", Font.BOLD, 11));
            labelFloat.setForeground(LABEL_FLOAT_COLOR);
        } else {
            labelFloat.setBounds(12, (h - 20) / 2, w - 24, 20);
            labelFloat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            labelFloat.setForeground(PLACEHOLDER_COLOR);
        }
        // Campo
        campo.setBounds(12, h - 30, w - 24, 24);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(BG_COLOR);
        g2.fillRoundRect(0, 0, getWidth(), getHeight() - 2, 10, 10);

        // Border normal
        g2.setColor(BORDER_NORMAL);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 10, 10);

        // Border animated focus (bottom line)
        if (focusProgress > 0) {
            int lineW = (int)(getWidth() * focusProgress);
            int startX = (getWidth() - lineW) / 2;
            g2.setColor(BORDER_FOCUS);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(startX, getHeight() - 2, startX + lineW, getHeight() - 2);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void invalidate() { super.invalidate(); doLayout(); }

    public JTextField getCampo() { return campo; }

    /** Método de compatibilidad: devuelve el texto limpio */
    public String getRealText() {
        return campo.getText().trim();
    }

    public void setText(String text) { campo.setText(text); hasText = !text.isEmpty(); repaint(); }
    public String getText() { return campo.getText(); }

    public void addKeyListener(KeyListener kl) { campo.addKeyListener(kl); }
}
