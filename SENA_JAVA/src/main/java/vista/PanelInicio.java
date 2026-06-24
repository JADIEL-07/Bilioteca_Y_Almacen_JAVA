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
        
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setOpaque(false);
        pnlContent.setBorder(new EmptyBorder(40, 0, 40, 0));
        
        // Titulo
        JLabel lblTitulo = new JLabel("SISTEMA INTEGRAL: BIBLIOTECA, ALMACÉN E INVENTARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36)); // Bigger title
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("<html><div style='text-align: center; color: #cbd5e1; font-style: italic; width: 600px;'>Gestión unificada de material bibliográfico, equipos, herramientas e insumos para el centro de formación.</div></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlContent.add(lblTitulo);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlContent.add(lblSub);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 60)));
        
        // Cards Grid
        JPanel pnlGrid = new JPanel(new GridLayout(2, 3, 30, 30));
        pnlGrid.setOpaque(false);
        pnlGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlGrid.add(crearCard(0, "Todos los elementos", "Vista completa de todos los recursos institucionales.", false));
        pnlGrid.add(crearCard(1, "Libros", "Gestion y reserva de material bibliografico institucional.", true));
        pnlGrid.add(crearCard(2, "Equipos", "Prestamo y retorno de activos tecnicos especializados.", false));
        pnlGrid.add(crearCard(3, "Insumos", "Suministros consumibles para programas tecnicos.", false));
        pnlGrid.add(crearCard(4, "Herramientas", "Seguimiento en tiempo real de inventarios y existencias.", false));
        
        pnlContent.add(pnlGrid);
        
        pnlContent.add(Box.createRigidArea(new Dimension(0, 60)));
        
        // Terms
        JLabel lblTerms = new JLabel("<html><div style='text-align: center; color: #94a3b8;'>Al hacer clic en Comienza ahora, aceptas nuestros <span style='color: #39A900;'>terminos</span> y <span style='color: #39A900;'>politica de privacidad</span></div></html>");
        lblTerms.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTerms.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlContent.add(lblTerms);
        
        pnlContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Big Green Button
        BotonPlano btnComienza = new BotonPlano("COMIENZA AHORA");
        btnComienza.setPreferredSize(new Dimension(280, 50));
        btnComienza.setMaximumSize(new Dimension(280, 50));
        btnComienza.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComienza.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlContent.add(btnComienza);
        
        pnlContent.add(Box.createRigidArea(new Dimension(0, 80)));
        
        // Footer
        JLabel lblFooter = new JLabel("<html><div style='text-align: center; color: #64748b;'>© 2026 SENA - Sistema de Biblioteca y Almacén. Todos los derechos reservados.</div></html>");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlContent.add(lblFooter);
        
        pnlCenterWrapper.add(pnlContent);
        
        // ScrollPane for scrolling
        JScrollPane scroll = new JScrollPane(pnlCenterWrapper);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide scrollbar visually but allow scrolling
        
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setOpaque(false);
        nav.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Left side: Logo + Title
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlLeft.setOpaque(false);
        
        try {
            URL urlLogo = getClass().getResource("/imagenes/logo_sena_trans.png");
            if (urlLogo != null) {
                BufferedImage img = ImageIO.read(urlLogo);
                Image scaled = img.getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                pnlLeft.add(new JLabel(new ImageIcon(scaled)));
            }
        } catch (Exception e) {}

        JLabel lblNavTitle = new JLabel("BIBLIOTECA & ALMACÉN SENA");
        lblNavTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNavTitle.setForeground(Color.WHITE);
        pnlLeft.add(lblNavTitle);

        // Right side: Links + Button
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        pnlRight.setOpaque(false);
        
        pnlRight.add(crearLinkConIcono("INICIO", 0, false));
        pnlRight.add(crearLinkConIcono("CONTACTO", 1, false));
        
        JPanel btnLogin = crearLinkConIcono("INICIAR SESIÓN", 3, true);
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Login");
            }
        });
        pnlRight.add(btnLogin);
        
        BotonPlano btnCrear = new BotonPlano("CREAR CUENTA");
        btnCrear.setPreferredSize(new Dimension(150, 40));
        
        // Add user icon inside the button if possible, but BotonPlano is simple. Let's leave text.
        // Or we can create a wrapper. 
        JPanel pnlBtn = new JPanel(new BorderLayout());
        pnlBtn.setOpaque(false);
        pnlBtn.add(btnCrear);
        pnlRight.add(pnlBtn);

        nav.add(pnlLeft, BorderLayout.WEST);
        nav.add(pnlRight, BorderLayout.EAST);
        
        // Add a subtle bottom border to the navbar
        JPanel navContainer = new JPanel(new BorderLayout());
        navContainer.setOpaque(false);
        navContainer.add(nav, BorderLayout.CENTER);
        
        JPanel border = new JPanel();
        border.setBackground(new Color(255,255,255,20));
        border.setPreferredSize(new Dimension(0, 1));
        navContainer.add(border, BorderLayout.SOUTH);
        
        return navContainer;
    }

    private JPanel crearLinkConIcono(String text, int iconType, boolean isAction) {
        JPanel pnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Colors
                boolean hover = (Boolean) getClientProperty("hover");
                g2.setColor(hover ? SenaColores.VERDE_SENA : Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int y = getHeight() / 2;
                int x = 2;
                
                switch(iconType) {
                    case 0: // House
                        g2.drawLine(x+2, y+4, x+2, y-1);
                        g2.drawLine(x+10, y+4, x+10, y-1);
                        g2.drawLine(x+2, y+4, x+10, y+4);
                        g2.drawLine(x, y, x+6, y-5);
                        g2.drawLine(x+12, y, x+6, y-5);
                        break;
                    case 1: // Envelope
                        g2.drawRect(x, y-4, 12, 8);
                        g2.drawLine(x, y-4, x+6, y);
                        g2.drawLine(x+12, y-4, x+6, y);
                        break;
                    case 2: // Robot/User tie
                        g2.drawOval(x+3, y-5, 6, 6);
                        g2.drawLine(x, y+5, x+12, y+5);
                        g2.drawArc(x, y-2, 12, 14, 0, 180);
                        break;
                    case 3: // User
                        g2.drawOval(x+3, y-5, 6, 6);
                        g2.drawArc(x, y-2, 12, 14, 0, 180);
                        break;
                }
                g2.dispose();
            }
        };
        pnl.putClientProperty("hover", false);
        pnl.setOpaque(false);
        pnl.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0)); // space for icon
        pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        pnl.add(lbl);
        
        pnl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                pnl.putClientProperty("hover", true);
                lbl.setForeground(SenaColores.VERDE_SENA);
                pnl.repaint();
            }
            public void mouseExited(MouseEvent e) {
                pnl.putClientProperty("hover", false);
                lbl.setForeground(Color.WHITE);
                pnl.repaint();
            }
        });
        
        return pnl;
    }

    private PanelCristal crearCard(int type, String titulo, String desc, boolean active) {
        PanelCristal card = new PanelCristal();
        card.setActive(active);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(320, 240));
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setHovered(true); }
            public void mouseExited(MouseEvent e) { card.setHovered(false); }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        int y = 0;
        
        // Icon in circle
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int s = 64; // Bigger circle
                int cx = 40; // Hardcoded center
                int cy = 40;
                int x = cx - s/2;
                
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(x, cy - s/2, s, s);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Thicker lines
                
                switch (type) {
                    case 0: // Stack (Todos)
                        g2.drawLine(cx - 12, cy - 4, cx, cy - 10);
                        g2.drawLine(cx, cy - 10, cx + 12, cy - 4);
                        g2.drawLine(cx - 12, cy - 4, cx, cy + 2);
                        g2.drawLine(cx, cy + 2, cx + 12, cy - 4);
                        
                        g2.drawLine(cx - 12, cy + 2, cx, cy + 8);
                        g2.drawLine(cx, cy + 8, cx + 12, cy + 2);
                        
                        g2.drawLine(cx - 12, cy + 8, cx, cy + 14);
                        g2.drawLine(cx, cy + 14, cx + 12, cy + 8);
                        break;
                    case 1: // Libros
                        g2.drawRect(cx - 12, cy - 8, 12, 16);
                        g2.drawRect(cx, cy - 8, 12, 16);
                        g2.drawLine(cx, cy - 8, cx, cy + 8);
                        break;
                    case 2: // Equipos (Monitor)
                        g2.drawRoundRect(cx - 14, cy - 10, 28, 16, 4, 4);
                        g2.drawLine(cx - 6, cy + 12, cx + 6, cy + 12);
                        g2.drawLine(cx, cy + 6, cx, cy + 12);
                        break;
                    case 3: // Insumos (Box / Cube)
                        g2.drawLine(cx, cy - 12, cx - 12, cy - 6);
                        g2.drawLine(cx - 12, cy - 6, cx, cy);
                        g2.drawLine(cx, cy, cx + 12, cy - 6);
                        g2.drawLine(cx + 12, cy - 6, cx, cy - 12);
                        g2.drawLine(cx - 12, cy - 6, cx - 12, cy + 6);
                        g2.drawLine(cx + 12, cy - 6, cx + 12, cy + 6);
                        g2.drawLine(cx - 12, cy + 6, cx, cy + 12);
                        g2.drawLine(cx + 12, cy + 6, cx, cy + 12);
                        g2.drawLine(cx, cy, cx, cy + 12);
                        break;
                    case 4: // Herramientas (Wrench)
                        g2.drawOval(cx + 3, cy - 12, 8, 8);
                        g2.drawLine(cx + 4, cy - 5, cx - 8, cy + 8);
                        g2.drawLine(cx + 2, cy - 3, cx - 6, cy + 10);
                        g2.drawOval(cx - 10, cy + 8, 4, 4);
                        break;
                }
                g2.dispose();
            }
        };
        pnlIcon.setOpaque(false);
        pnlIcon.setPreferredSize(new Dimension(80, 80));
        pnlIcon.setMinimumSize(new Dimension(80, 80)); // Ensure it's not compressed
        pnlIcon.setMaximumSize(new Dimension(80, 80)); // Ensure it's not stretched
        gbc.gridy = y++;
        card.add(pnlIcon, gbc);
        
        gbc.gridy = y++;
        card.add(Box.createRigidArea(new Dimension(0, 5)), gbc);
        
        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setForeground(Color.WHITE);
        gbc.gridy = y++;
        card.add(lblTit, gbc);
        
        gbc.gridy = y++;
        card.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        
        JLabel lblDesc = new JLabel("<html><div style='text-align: center; width: 220px; color: #cbd5e1; line-height: 1.4;'>"+desc+"</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridy = y++;
        card.add(lblDesc, gbc);

        return card;
    }
}
