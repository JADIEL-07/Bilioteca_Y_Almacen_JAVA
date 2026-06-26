package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;

public class PanelInicio extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardsContainer;

    public PanelInicio(CardLayout cardLayout, JPanel cardsContainer) {
        this.cardLayout = cardLayout;
        this.cardsContainer = cardsContainer;
        
        setOpaque(false);
        setLayout(new BorderLayout());

        // Navbar
        add(crearNavbar(), BorderLayout.NORTH);

        // Center Content
        JPanel pnlCenterWrapper = new JPanel(new GridBagLayout());
        pnlCenterWrapper.setOpaque(false);
        
        // Cards Grid
        JPanel pnlGrid = new JPanel();
        pnlGrid.setLayout(new BoxLayout(pnlGrid, BoxLayout.Y_AXIS));
        pnlGrid.setOpaque(false);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        row1.setOpaque(false);
        row1.add(crearCard(0, "Todos los elementos", "Vista completa de todos los recursos institucionales.", false));
        row1.add(crearCard(1, "Libros", "Gestión y reserva de material bibliográfico institucional.", true));
        row1.add(crearCard(2, "Equipos", "Préstamo y retorno de activos técnicos especializados.", false));

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        row2.setOpaque(false);
        row2.add(crearCard(3, "Insumos", "Suministros consumibles para programas técnicos.", false));
        row2.add(crearCard(4, "Herramientas", "Seguimiento en tiempo real de inventarios y existencias.", false));

        pnlGrid.add(row1);
        pnlGrid.add(row2);
        
        pnlCenterWrapper.add(pnlGrid);
        add(pnlCenterWrapper, BorderLayout.CENTER);
    }

    private JPanel crearNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Left side: Logo + Title
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeft.setOpaque(false);
        
        try {
            URL urlLogo = getClass().getResource("/imagenes/logo_sena_trans.png");
            if (urlLogo != null) {
                BufferedImage img = ImageIO.read(urlLogo);
                Image scaled = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                pnlLeft.add(new JLabel(new ImageIcon(scaled)));
            }
        } catch (Exception e) {}

        JLabel lblNavTitle = new JLabel("BIBLIOTECA & ALMACÉN SENA");
        lblNavTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNavTitle.setForeground(Color.WHITE);
        pnlLeft.add(lblNavTitle);

        // Right side: Links + Button
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        pnlRight.setOpaque(false);
        
        pnlRight.add(crearLink("INICIO", false));
        pnlRight.add(crearLink("CONTACTO", false));
        
        JLabel btnLogin = crearLink("INICIAR SESIÓN", true);
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Login");
            }
        });
        pnlRight.add(btnLogin);
        
        BotonPlano btnCrear = new BotonPlano("CREAR CUENTA");
        btnCrear.setPreferredSize(new Dimension(140, 35));
        btnCrear.addActionListener(e -> {
            cardLayout.show(cardsContainer, "Registro");
        });
        pnlRight.add(btnCrear);

        nav.add(pnlLeft, BorderLayout.WEST);
        nav.add(pnlRight, BorderLayout.EAST);
        return nav;
    }

    private JLabel crearLink(String text, boolean isAction) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.WHITE);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                lbl.setForeground(SenaColores.VERDE_SENA);
            }
            public void mouseExited(MouseEvent e) {
                lbl.setForeground(Color.WHITE);
            }
        });
        return lbl;
    }

    private PanelCristal crearCard(int type, String titulo, String desc, boolean active) {
        PanelCristal card = new PanelCristal();
        card.setActive(active);
        
        // Usamos BoxLayout vertical para centrado infalible
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(280, 210));
        card.setBorder(new EmptyBorder(25, 20, 25, 20));
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setHovered(true); }
            public void mouseExited(MouseEvent e) { card.setHovered(false); }
        });

        // Contenedor que empuja hacia el centro
        card.add(Box.createVerticalGlue());

        // Icon in circle
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int s = 50;
                int x = (getWidth() - s) / 2;
                int y = (getHeight() - s) / 2;
                
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(x, y, s, s);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                
                switch (type) {
                    case 0: // Stack (Todos)
                        g2.drawLine(cx - 10, cy - 3, cx, cy - 8);
                        g2.drawLine(cx, cy - 8, cx + 10, cy - 3);
                        g2.drawLine(cx - 10, cy - 3, cx, cy + 2);
                        g2.drawLine(cx, cy + 2, cx + 10, cy - 3);
                        
                        g2.drawLine(cx - 10, cy + 3, cx, cy + 8);
                        g2.drawLine(cx, cy + 8, cx + 10, cy + 3);
                        break;
                    case 1: // Libros
                        g2.drawRect(cx - 10, cy - 8, 10, 14);
                        g2.drawRect(cx, cy - 8, 10, 14);
                        break;
                    case 2: // Equipos (Monitor)
                        g2.drawRect(cx - 12, cy - 10, 24, 14);
                        g2.drawLine(cx - 6, cy + 10, cx + 6, cy + 10);
                        g2.drawLine(cx, cy + 4, cx, cy + 10);
                        break;
                    case 3: // Insumos (Caja)
                        g2.drawRect(cx - 10, cy - 8, 20, 14);
                        g2.drawLine(cx - 10, cy, cx + 10, cy);
                        break;
                    case 4: // Herramientas (Llave)
                        g2.drawOval(cx + 3, cy - 10, 8, 8);
                        g2.drawLine(cx + 4, cy - 4, cx - 8, cy + 8);
                        g2.drawLine(cx + 2, cy - 2, cx - 6, cy + 10);
                        break;
                }
                g2.dispose();
            }
        };
        pnlIcon.setOpaque(false);
        pnlIcon.setPreferredSize(new Dimension(60, 60));
        pnlIcon.setMinimumSize(new Dimension(60, 60));
        pnlIcon.setMaximumSize(new Dimension(60, 60));
        pnlIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pnlIcon);
        
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTit.setForeground(Color.WHITE);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblTit);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblDesc = new JLabel("<html><div style='text-align: center;'>"+desc+"</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(203, 213, 225)); // Slate 300
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblDesc);

        card.add(Box.createVerticalGlue());

        return card;
    }
}
