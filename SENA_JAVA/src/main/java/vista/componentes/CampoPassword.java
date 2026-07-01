package vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Igual que CampoAnimado pero para contraseñas (JPasswordField)
 */
public class CampoPassword extends JPanel {

    private JPasswordField campo;
    private JLabel labelFloat;
    private String placeholder;
    private JLabel btnToggle;

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
    private boolean showPass  = false;

    public CampoPassword(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(300, 60));

        labelFloat = new JLabel(placeholder);
        labelFloat.setForeground(PLACEHOLDER_COLOR);
        labelFloat.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        campo = new JPasswordField();
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createEmptyBorder());
        campo.setForeground(TEXT_COLOR);
        campo.setCaretColor(BORDER_FOCUS);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setEchoChar('●');

        btnToggle = new JLabel("👁");
        btnToggle.setForeground(PLACEHOLDER_COLOR);
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnToggle.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                showPass = !showPass;
                campo.setEchoChar(showPass ? (char)0 : '●');
                btnToggle.setText(showPass ? "🙈" : "👁");
            }
        });

        add(labelFloat);
        add(campo);
        add(btnToggle);

        focusTimer = new Timer(10, null);

        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { hasFocus = true; animateFocus(true); }
            @Override public void focusLost(FocusEvent e)  {
                hasFocus = false;
                hasText = campo.getPassword().length > 0;
                if (!hasText) animateFocus(false);
                repaint();
            }
        });

        campo.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { hasText = campo.getPassword().length > 0; repaint(); }
        });
    }

    private void animateFocus(boolean show) {
        focusTimer.stop();
        focusTimer = new Timer(10, ev -> {
            focusProgress = show ? Math.min(1f, focusProgress + 0.08f) : Math.max(0f, focusProgress - 0.08f);
            repaint();
            if ((show && focusProgress >= 1f) || (!show && focusProgress <= 0f)) ((Timer)ev.getSource()).stop();
        });
        focusTimer.start();
    }

    @Override
    public void doLayout() {
        int w = getWidth(); int h = getHeight();
        boolean floating = hasFocus || hasText;
        if (floating) {
            labelFloat.setBounds(12, 6, w - 48, 18);
            labelFloat.setFont(new Font("Segoe UI", Font.BOLD, 11));
            labelFloat.setForeground(LABEL_FLOAT_COLOR);
        } else {
            labelFloat.setBounds(12, (h - 20) / 2, w - 48, 20);
            labelFloat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            labelFloat.setForeground(PLACEHOLDER_COLOR);
        }
        campo.setBounds(12, h - 30, w - 48, 24);
        btnToggle.setBounds(w - 36, (h - 20) / 2, 28, 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(BG_COLOR);
        g2.fillRoundRect(0, 0, getWidth(), getHeight() - 2, 10, 10);
        g2.setColor(BORDER_NORMAL);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 10, 10);
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

    @Override public void invalidate() { super.invalidate(); doLayout(); }

    public JPasswordField getCampo() { return campo; }
    public char[] getPassword() { return campo.getPassword(); }
    public String getRealText() { return new String(campo.getPassword()).trim(); }
}
