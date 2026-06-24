package vista;

import vista.componentes.BotonPlano;
import vista.componentes.CampoTextoModerno;
import vista.componentes.SenaColores;
import vista.componentes.PanelParticulasAnimadas;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Login extends JFrame {

    private PanelParticulasAnimadas fondoAnimado;

    public Login() {
        setTitle("BIBLIOTECA & ALMACÉN SENA - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));

        fondoAnimado = new PanelParticulasAnimadas();
        fondoAnimado.setLayout(null); 

        CardLayout cardLayout = new CardLayout();
        JPanel cardsContainer = new JPanel(cardLayout);
        cardsContainer.setOpaque(false);

        PanelCristal tarjetaLogin = crearTarjetaLogin(cardLayout, cardsContainer);
        JPanel pnlLoginWrapper = new JPanel(new GridBagLayout());
        pnlLoginWrapper.setOpaque(false);
        pnlLoginWrapper.add(tarjetaLogin);

        PanelInicio pnlInicio = new PanelInicio(cardLayout, cardsContainer);

        cardsContainer.add(pnlLoginWrapper, "Login");
        cardsContainer.add(pnlInicio, "Inicio");

        fondoAnimado.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cardsContainer.setBounds(0, 0, fondoAnimado.getWidth(), fondoAnimado.getHeight());
            }
        });

        fondoAnimado.add(cardsContainer);

        setContentPane(fondoAnimado);
        
        // Show Inicio by default
        cardLayout.show(cardsContainer, "Inicio");
    }

    private PanelCristal crearTarjetaLogin(CardLayout cardLayout, JPanel cardsContainer) {
        PanelCristal tarjeta = new PanelCristal();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setPreferredSize(new Dimension(420, 580));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        int gridy = 0;

        // Spacer
        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Logo
        try {
            URL urlLogo = getClass().getResource("/imagenes/logo_sena_trans.png");
            if (urlLogo != null) {
                BufferedImage img = ImageIO.read(urlLogo);
                Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel lblLogo = new JLabel(new ImageIcon(scaled));
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridy = gridy++;
                tarjeta.add(lblLogo, gbc);
            }
        } catch (Exception e) {}

        // Title
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(SenaColores.VERDE_SENA);
        gbc.gridy = gridy++;
        tarjeta.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Ingresa tus credenciales de acceso", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(SenaColores.TEXTO_SECUNDARIO);
        gbc.gridy = gridy++;
        tarjeta.add(lblSub, gbc);

        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

        // Document Field
        JLabel lblDoc = new JLabel("Número de Documento");
        lblDoc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDoc.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 0, 40);
        tarjeta.add(lblDoc, gbc);

        CampoTextoModerno txtDoc = new CampoTextoModerno("Ingresa tu documento", 1); // 1 = User Icon
        txtDoc.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 15, 40);
        tarjeta.add(txtDoc, gbc);

        // Password Field
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 0, 40);
        tarjeta.add(lblPass, gbc);

        vista.componentes.CampoPasswordModerno txtPass = new vista.componentes.CampoPasswordModerno("Tu contraseña");
        txtPass.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 5, 40);
        tarjeta.add(txtPass, gbc);

        // Forgot Password
        JLabel lblForgot = new JLabel("¿Olvidaste tu contraseña?");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblForgot.setForeground(SenaColores.VERDE_SENA);
        lblForgot.setHorizontalAlignment(SwingConstants.RIGHT);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        tarjeta.add(lblForgot, gbc);

        // Login Button
        BotonPlano btnLogin = new BotonPlano("Ingresar a la Plataforma");
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.addActionListener(e -> {
            Dashboard d = new Dashboard();
            d.setVisible(true);
            this.dispose();
        });
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 20, 40);
        tarjeta.add(btnLogin, gbc);

        // Bottom links
        JPanel pnlLinks = new JPanel();
        pnlLinks.setLayout(new BoxLayout(pnlLinks, BoxLayout.Y_AXIS));
        pnlLinks.setOpaque(false);

        JLabel lblReg = new JLabel("¿No tienes cuenta? Regístrate aquí");
        lblReg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblReg.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblReg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblHome = new JLabel("Volver al Inicio");
        lblHome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHome.setForeground(Color.WHITE);
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Inicio");
            }
        });

        pnlLinks.add(lblReg);
        pnlLinks.add(Box.createRigidArea(new Dimension(0, 8)));
        pnlLinks.add(lblHome);

        gbc.gridy = gridy++;
        tarjeta.add(pnlLinks, gbc);
        
        // Spacer bottom
        gbc.gridy = gridy++;
        gbc.weighty = 1.0;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        return tarjeta;
    }

    private Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                }
                return rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    class PanelCristal extends JPanel {
        public PanelCristal() {
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(15, 23, 42, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.setColor(new Color(255, 255, 255, 30));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class BotonAccesibilidad extends JPanel {
        private boolean arrastrado = false;
        private int mouseX, mouseY;
        private float scale = 1.0f;
        private boolean growing = true;

        public BotonAccesibilidad() {
            setSize(50, 50);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            Timer pulseTimer = new Timer(50, e -> {
                if (growing) {
                    scale += 0.02f;
                    if (scale > 1.15f) growing = false;
                } else {
                    scale -= 0.02f;
                    if (scale < 0.95f) growing = true;
                }
                repaint();
            });
            pulseTimer.start();

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    arrastrado = true;
                    int pX = e.getXOnScreen();
                    int pY = e.getYOnScreen();
                    Point panelLoc = getParent().getLocationOnScreen();
                    setLocation(pX - panelLoc.x - mouseX, pY - panelLoc.y - mouseY);
                }
            });
        }

        public boolean haSidoArrastrado() { return arrastrado; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int baseSize = 46;
            int currentSize = (int) (baseSize * scale);
            int offset = (getWidth() - currentSize) / 2;

            g2.setColor(SenaColores.VERDE_SENA);
            g2.fillOval(offset, offset, currentSize, currentSize);
            
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            
            g2.drawOval(cx - 3, cy - 10, 6, 6);
            g2.drawLine(cx, cy - 4, cx, cy + 4);
            g2.drawLine(cx - 7, cy - 1, cx + 7, cy - 1);
            g2.drawLine(cx, cy + 4, cx - 5, cy + 12);
            g2.drawLine(cx, cy + 4, cx + 5, cy + 12);
            
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
