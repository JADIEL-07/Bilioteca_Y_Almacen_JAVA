package vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

public class PanelParticulasAnimadas extends JPanel {
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
        } catch (Exception e) {}
        
        particulas = new ArrayList<>();
        iconos = new ArrayList<>();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (particulas.isEmpty()) {
                    for (int i = 0; i < 50; i++) {
                        particulas.add(new Particula(getWidth(), getHeight()));
                    }
                    for (int i = 0; i < 6; i++) {
                        iconos.add(new IconoAnimado(getWidth(), getHeight(), i));
                    }
                }
            }
        });

        timer = new Timer(16, e -> {
            int w = getWidth();
            int h = getHeight();
            for (Particula p : particulas) p.update(w, h);
            for (IconoAnimado ic : iconos) ic.update(w, h);
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(SenaColores.FONDO_OSCURO);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Iconos
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(255, 255, 255, 60)); // White semi-transparent
        for (IconoAnimado ic : iconos) {
            ic.draw(g2);
        }

        // Draw Particulas
        for (Particula p : particulas) {
            p.draw(g2);
        }

        g2.dispose();
    }

    private class Particula {
        float x, y, speedY, size, alpha;
        boolean glowing;

        public Particula(int width, int height) {
            reset(width, height, true);
        }

        public void reset(int width, int height, boolean randomY) {
            Random r = new Random();
            x = r.nextInt(Math.max(1, width));
            y = randomY ? r.nextInt(Math.max(1, height)) : height + 10;
            speedY = 0.5f + r.nextFloat() * 1.5f;
            size = 2 + r.nextFloat() * 4;
            alpha = 0.2f + r.nextFloat() * 0.8f;
            glowing = r.nextFloat() > 0.8f; 
        }

        public void update(int width, int height) {
            y -= speedY;
            if (y < -10) reset(width, height, false);
        }

        public void draw(Graphics2D g2) {
            if (glowing) {
                g2.setColor(new Color(57, 169, 0, (int)(alpha * 50)));
                g2.fillOval((int)x - (int)size, (int)y - (int)size, (int)size*3, (int)size*3);
            }
            g2.setColor(new Color(57, 169, 0, (int)(alpha * 255)));
            g2.fillOval((int)x, (int)y, (int)size, (int)size);
        }
    }

    private class IconoAnimado {
        float x, y, rot, speedY, speedX, speedRot, scale;
        int type;

        public IconoAnimado(int width, int height, int type) {
            this.type = type;
            reset(width, height, true);
        }

        public void reset(int width, int height, boolean randomY) {
            Random r = new Random();
            x = r.nextInt(Math.max(1, width));
            y = randomY ? r.nextInt(Math.max(1, height)) : height + 50;
            speedY = -0.2f - r.nextFloat() * 0.5f;
            speedX = -0.3f + r.nextFloat() * 0.6f;
            speedRot = -0.02f + r.nextFloat() * 0.04f;
            scale = 0.5f + r.nextFloat() * 1.5f;
            rot = r.nextFloat() * (float)Math.PI * 2;
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
}
