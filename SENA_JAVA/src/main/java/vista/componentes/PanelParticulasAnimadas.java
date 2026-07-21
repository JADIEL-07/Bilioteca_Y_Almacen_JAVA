package vista.componentes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Panel animado con fondo oscuro, partículas flotantes e íconos animados.
 * Se usa como fondo del Login y del Dashboard.
 */
public class PanelParticulasAnimadas extends JPanel {

    private BufferedImage bgImage;
    private List<Particula> particulas;
    private List<IconoAnimado> iconos;
    private Timer timer;

    public PanelParticulasAnimadas() {
        setOpaque(true); // Nosotros mismos pintamos el fondo completamente
        try {
            URL bgUrl = getClass().getResource("/imagenes/bg_oscuro.png");
            if (bgUrl != null) {
                BufferedImage temp = ImageIO.read(bgUrl);
                
                // Usamos la imagen en su resolución original para evitar pixelado,
                // aplicando un filtro de suavizado de alta calidad.
                bgImage = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2bg = bgImage.createGraphics();
                g2bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2bg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2bg.drawImage(temp, 0, 0, temp.getWidth(), temp.getHeight(), null);
                g2bg.dispose();
            }
        } catch (IOException e) {}

        particulas = new ArrayList<>();
        for (int i = 0; i < 20; i++) particulas.add(new Particula());

        iconos = new ArrayList<>();
        for (int i = 0; i < 10; i++) iconos.add(new IconoAnimado());

        // Optimización: 33ms (~30 FPS) en lugar de 16ms (60 FPS) para reducir uso de CPU
        timer = new Timer(33, e -> {
            if (!isShowing()) return; // No calcular ni repintar si el panel no es visible
            for (Particula p : particulas) p.update(getWidth(), getHeight());
            for (IconoAnimado ic : iconos) ic.update(getWidth(), getHeight());
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo oscuro base siempre (evitamos llamar super que lo borraría)
        g2.setColor(new Color(10, 15, 28));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Imagen de fondo con blur (si existe)
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        }

        // Dark overlay semi-transparente
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Iconos animados flotantes
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (IconoAnimado ic : iconos) ic.draw(g2);

        // Partículas verdes
        for (Particula p : particulas) {
            g2.setColor(new Color(57, 169, 0, p.alpha));
            g2.fillOval((int) p.x, (int) p.y, p.size, p.size);
        }

        g2.dispose();
    }

    // =========================================================
    //  Partícula flotante
    // =========================================================
    class Particula {
        float x, y, speedX, speedY;
        int size, alpha;
        Random rnd = new Random();

        Particula() { reset(800, 600, true); }

        void reset(int width, int height, boolean randomY) {
            x = rnd.nextInt(Math.max(width, 1));
            y = randomY ? rnd.nextInt(Math.max(height, 1)) : height + 10;
            speedX = (rnd.nextFloat() - 0.5f) * 0.5f;
            speedY = -(rnd.nextFloat() * 0.5f + 0.1f);
            size = rnd.nextInt(4) + 2;
            alpha = rnd.nextInt(80) + 20;
        }

        void update(int width, int height) {
            x += speedX;
            y += speedY;
            if (y < -10) reset(width, height, false);
            if (x < -10) x = width + 10;
            if (x > width + 10) x = -10;
        }
    }

    // =========================================================
    //  Ícono animado flotante
    // =========================================================
    class IconoAnimado {
        float x, y, speedX, speedY;
        double rot, speedRot;
        float scale;
        int alpha, type;
        float strokeWeight;
        Random rnd = new Random();

        IconoAnimado() { reset(800, 600, true); }

        void reset(int width, int height, boolean randomY) {
            x = rnd.nextInt(Math.max(width, 1));
            y = randomY ? rnd.nextInt(Math.max(height, 1)) : height + 100;
            speedX = (rnd.nextFloat() - 0.5f) * 0.3f;
            speedY = -(rnd.nextFloat() * 0.3f + 0.05f);
            speedRot = (rnd.nextDouble() - 0.5) * 0.01;
            type = rnd.nextInt(6);
            scale = 0.5f + rnd.nextFloat() * 1.5f;
            alpha = rnd.nextInt(40) + 15;
            rot = rnd.nextDouble() * Math.PI * 2;
            strokeWeight = (float) (1.5 / scale);
        }

        void update(int width, int height) {
            x += speedX;
            y += speedY;
            rot += speedRot;
            if (y < -100) reset(width, height, false);
            if (x < -100) x = width + 100;
            if (x > width + 100) x = -100;
        }

        void draw(Graphics2D g2) {
            AffineTransform old = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(rot);
            g2.scale(scale, scale);
            g2.setColor(new Color(57, 169, 0, alpha));
            g2.setStroke(new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int s = 30;
            switch (type) {
                case 0: g2.drawOval(-s/2,-s/2,s,s); g2.drawLine((int)(s*0.35),(int)(s*0.35),s,s); break;
                case 1: g2.drawRect(-s/2,-s,s,s*2); g2.drawLine(-s/4,-s/2,s/4,-s/2); g2.drawLine(-s/4,-s/4,s/4,-s/4); g2.drawLine(-s/4,0,s/4,0); break;
                case 2: g2.drawOval(-s,-s,s*2,s*2); g2.drawLine(0,0,0,-s/2); g2.drawLine(0,0,s/2+5,0); g2.drawOval(-2,-2,4,4); break;
                case 3: g2.drawRect(-s,-s/2,s,s); g2.drawRect(0,-s/2,s,s); g2.drawLine(0,-s/2,0,s/2); g2.drawLine(-s+5,-s/4,-5,-s/4); g2.drawLine(5,-s/4,s-5,-s/4); break;
                case 4: g2.drawOval(-s/2,-s,s,s); g2.drawRect(-s/4,0,s/2,s/3); g2.drawLine(-s/4+2,s/3,s/4-2,s/3); g2.drawLine(-s/4+2,s/4,s/4-2,s/4); g2.drawLine(0,s/3,0,s/2); break;
                case 5: g2.drawOval(-s/2,-s/2,s,s); g2.drawOval(-s/4,-s/4,s/2,s/2); g2.drawLine(0,-s/2,0,-s/2-8); g2.drawLine(0,s/2,0,s/2+8); g2.drawLine(-s/2,0,-s/2-8,0); g2.drawLine(s/2,0,s/2+8,0); break;
            }
            g2.setTransform(old);
        }
    }
}
