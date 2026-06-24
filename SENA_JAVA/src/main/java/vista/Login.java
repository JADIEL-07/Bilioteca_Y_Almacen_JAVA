package vista;

import vista.componentes.BotonPlano;
import vista.componentes.CampoTextoModerno;
import vista.componentes.SenaColores;
<<<<<<< Updated upstream
=======
import vista.componentes.PanelParticulasAnimadas;
import vista.componentes.PanelCristal;
>>>>>>> Stashed changes

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
    private BotonAccesibilidad btnAccesibilidad;

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
                if (!btnAccesibilidad.haSidoArrastrado()) {
                    int x = fondoAnimado.getWidth() - 100;
                    int y = fondoAnimado.getHeight() - 100;
                    btnAccesibilidad.setLocation(x, y);
                }
            }
        });

        fondoAnimado.add(cardsContainer);

        btnAccesibilidad = new BotonAccesibilidad();
        fondoAnimado.add(btnAccesibilidad);

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


<<<<<<< Updated upstream
    class PanelParticulasAnimadas extends JPanel {
        private BufferedImage bgImage;
        private List<Particula> particulas;
        private List<IconoAnimado> iconos;
        private Timer timer;

        public PanelParticulasAnimadas() {
            try {
                URL bgUrl = getClass().getResource("/imagenes/bg_oscuro.png");
                if (bgUrl != null) {
                    BufferedImage temp = ImageIO.read(bgUrl);
                    
                    // High-quality Blur (No pixelation)
                    int scaleFactor = 4;
                    int w = temp.getWidth() / scaleFactor;
                    int h = temp.getHeight() / scaleFactor;
                    
                    // 1. Scale down smoothly
                    Image scaledDown = temp.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    BufferedImage smallImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2small = smallImg.createGraphics();
                    g2small.drawImage(scaledDown, 0, 0, null);
                    g2small.dispose();
                    
                    // 2. Apply true Gaussian Blur to the small image
                    float[] matrix = new float[25];
                    for (int i = 0; i < 25; i++) matrix[i] = 1.0f / 25.0f;
                    java.awt.image.Kernel kernel = new java.awt.image.Kernel(5, 5, matrix);
                    java.awt.image.ConvolveOp op = new java.awt.image.ConvolveOp(kernel, java.awt.image.ConvolveOp.EDGE_NO_OP, null);
                    BufferedImage blurredSmall = op.filter(smallImg, null);
                    
                    // 3. Upscale using high-quality BICUBIC interpolation
                    bgImage = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2bg = bgImage.createGraphics();
                    g2bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2bg.drawImage(blurredSmall, 0, 0, temp.getWidth(), temp.getHeight(), null);
                    g2bg.dispose();
                }
            } catch (IOException e) {}
            
            particulas = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                particulas.add(new Particula());
            }
            
            iconos = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                iconos.add(new IconoAnimado());
            }
            
            timer = new Timer(16, e -> {
                for (Particula p : particulas) p.update(getWidth(), getHeight());
                for (IconoAnimado ic : iconos) ic.update(getWidth(), getHeight());
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (bgImage != null) {
                double scale = Math.max((double) getWidth() / bgImage.getWidth(), (double) getHeight() / bgImage.getHeight());
                int imgW = (int) (bgImage.getWidth() * scale);
                int imgH = (int) (bgImage.getHeight() * scale);
                int x = (getWidth() - imgW) / 2;
                int y = (getHeight() - imgH) / 2;
                g2.drawImage(bgImage, x, y, imgW, imgH, null);
            } else {
                g2.setColor(SenaColores.FONDO_OSCURO);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setColor(new Color(15, 23, 42, 160));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw floating outline icons
            for (IconoAnimado ic : iconos) {
                g2.setStroke(new BasicStroke(ic.strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 255, 255, ic.alpha));
                ic.draw(g2);
            }

            // Draw glowing green dots
            for (Particula p : particulas) {
                g2.setColor(new Color(57, 169, 0, p.alpha / 3)); // Glow
                g2.fillOval((int) p.x - 2, (int) p.y - 2, p.size + 4, p.size + 4);
                
                g2.setColor(new Color(57, 169, 0, p.alpha)); // Core
                g2.fillOval((int) p.x, (int) p.y, p.size, p.size);
            }
            
            g2.dispose();
        }
    }

    class Particula {
        double x, y;
        double speedX, speedY;
        int size;
        int alpha;
        Random rnd = new Random();

        public Particula() { reset(1920, 1080, true); }

        public void reset(int width, int height, boolean randomY) {
            width = width == 0 ? 1920 : width;
            height = height == 0 ? 1080 : height;
            x = rnd.nextInt(width);
            y = randomY ? rnd.nextInt(height) : height + 10;
            speedY = -0.3 - rnd.nextDouble() * 0.5;
            speedX = -0.2 + rnd.nextDouble() * 0.4;
            size = rnd.nextInt(4) + 3;
            alpha = rnd.nextInt(150) + 100;
        }

        public void update(int width, int height) {
            x += speedX;
            y += speedY;
            if (y < -10) reset(width, height, false);
            if (x < -10) x = width + 10;
            if (x > width + 10) x = -10;
        }
    }
    
    class IconoAnimado {
        double x, y, rot;
        double speedX, speedY, speedRot;
        double scale;
        int type; 
        int alpha;
        float strokeWeight;
        Random rnd = new Random();

        public IconoAnimado() { reset(1920, 1080, true); }

        public void reset(int width, int height, boolean randomY) {
            width = width == 0 ? 1920 : width;
            height = height == 0 ? 1080 : height;
            x = rnd.nextInt(width);
            y = randomY ? rnd.nextInt(height) : height + 100;
            speedY = -0.1 - rnd.nextDouble() * 0.4;
            speedX = -0.2 + rnd.nextDouble() * 0.4;
            speedRot = -0.01 + rnd.nextDouble() * 0.02;
            type = rnd.nextInt(6); // 0 to 5
            scale = 0.5 + rnd.nextDouble() * 1.5; // Very large to medium sizes
            alpha = rnd.nextInt(40) + 15; // More visible
            rot = rnd.nextDouble() * Math.PI * 2;
            strokeWeight = (float) (1.5 / scale); // Keep stroke thin even when scaled
        }

        public void update(int width, int height) {
            x += speedX;
            y += speedY;
            rot += speedRot;
            if (y < -100) reset(width, height, false);
            if (x < -100) x = width + 100;
            if (x > width + 100) x = -100;
        }
        
        public void draw(Graphics2D g2) {
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(rot);
            g2.scale(scale, scale);
            int s = 30; // base size
            
            switch (type) {
                case 0: // Lupa (Magnifying Glass)
                    g2.drawOval(-s/2, -s/2, s, s);
                    g2.drawLine((int)(s*0.35), (int)(s*0.35), s, s);
                    break;
                case 1: // Documento (Document)
                    g2.drawRect(-s/2, -s, s, s*2);
                    g2.drawLine(-s/4, -s/2, s/4, -s/2);
                    g2.drawLine(-s/4, -s/4, s/4, -s/4);
                    g2.drawLine(-s/4, 0, s/4, 0);
                    break;
                case 2: // Reloj (Clock)
                    g2.drawOval(-s, -s, s*2, s*2);
                    g2.drawLine(0, 0, 0, -s/2); // hour hand
                    g2.drawLine(0, 0, s/2 + 5, 0); // minute hand
                    g2.drawOval(-2, -2, 4, 4); // center pivot
                    break;
                case 3: // Libro Abierto (Open Book)
                    g2.drawRect(-s, -s/2, s, s);
                    g2.drawRect(0, -s/2, s, s);
                    g2.drawLine(0, -s/2, 0, s/2); // spine
                    g2.drawLine(-s + 5, -s/4, -5, -s/4);
                    g2.drawLine(5, -s/4, s - 5, -s/4);
                    break;
                case 4: // Bombillo (Lightbulb)
                    g2.drawOval(-s/2, -s, s, s);
                    g2.drawRect(-s/4, 0, s/2, s/3); // base
                    g2.drawLine(-s/4 + 2, s/3, s/4 - 2, s/3); // threads
                    g2.drawLine(-s/4 + 2, s/4, s/4 - 2, s/4);
                    g2.drawLine(0, s/3, 0, s/2); // pin
                    break;
                case 5: // Engranaje (Gear simplified)
                    g2.drawOval(-s/2, -s/2, s, s);
                    g2.drawOval(-s/4, -s/4, s/2, s/2);
                    g2.drawLine(0, -s/2, 0, -s/2 - 8);
                    g2.drawLine(0, s/2, 0, s/2 + 8);
                    g2.drawLine(-s/2, 0, -s/2 - 8, 0);
                    g2.drawLine(s/2, 0, s/2 + 8, 0);
                    break;
            }
            g2.setTransform(old);
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
=======
>>>>>>> Stashed changes

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
