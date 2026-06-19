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
        
        // Titulo
        JLabel lblTitulo = new JLabel("SISTEMA INTEGRAL: BIBLIOTECA, ALMACÉN E INVENTARIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("<html><div style='text-align: center; color: #94a3b8;'>Gestión unificada de material bibliográfico, equipos, herramientas e insumos para el centro de<br>formación.</div></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlContent.add(lblTitulo);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlContent.add(lblSub);
        pnlContent.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Cards Grid
        JPanel pnlGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        pnlGrid.setOpaque(false);
        pnlGrid.setMaximumSize(new Dimension(900, 450));
        pnlGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlGrid.add(crearCard(0, "Todos los elementos", "Vista completa de todos los recursos institucionales.", false));
        pnlGrid.add(crearCard(1, "Libros", "Gestion y reserva de material bibliografico institucional.", true));
        pnlGrid.add(crearCard(2, "Equipos", "Prestamo y retorno de activos tecnicos especializados.", false));
        pnlGrid.add(crearCard(3, "Insumos", "Suministros consumibles para programas tecnicos.", false));
        pnlGrid.add(crearCard(4, "Herramientas", "Seguimiento en tiempo real de inventarios y existencias.", false));
        pnlGrid.add(crearCard(5, "Asistente Personal", "Un asistente inteligente diseñado para guiarte y facilitar el flujo por toda la web.", false));
        
        pnlContent.add(pnlGrid);
        
        pnlCenterWrapper.add(pnlContent);
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
        pnlRight.add(crearLink("ASISTENTE PERSONAL", false));
        
        JLabel btnLogin = crearLink("INICIAR SESIÓN", true);
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Login");
            }
        });
        pnlRight.add(btnLogin);
        
        BotonPlano btnCrear = new BotonPlano("CREAR CUENTA");
        btnCrear.setPreferredSize(new Dimension(140, 35));
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
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(280, 200));
        
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setHovered(true); }
            public void mouseExited(MouseEvent e) { card.setHovered(false); }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 20, 5, 20);
        
        int y = 0;
        
        // Icon in circle
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int s = 46;
                int x = (getWidth() - s) / 2;
                int y = (getHeight() - s) / 2;
                
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(x, y, s, s);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int is = 16; // icon size
                
                switch (type) {
                    case 0: // Stack (Todos)
                        g2.drawLine(cx - 8, cy - 2, cx, cy - 6);
                        g2.drawLine(cx, cy - 6, cx + 8, cy - 2);
                        g2.drawLine(cx - 8, cy - 2, cx, cy + 2);
                        g2.drawLine(cx, cy + 2, cx + 8, cy - 2);
                        
                        g2.drawLine(cx - 8, cy + 2, cx, cy + 6);
                        g2.drawLine(cx, cy + 6, cx + 8, cy + 2);
                        break;
                    case 1: // Libros
                        g2.drawRect(cx - 8, cy - 6, 8, 12);
                        g2.drawRect(cx, cy - 6, 8, 12);
                        break;
                    case 2: // Equipos (Monitor)
                        g2.drawRect(cx - 10, cy - 8, 20, 12);
                        g2.drawLine(cx - 4, cy + 8, cx + 4, cy + 8);
                        g2.drawLine(cx, cy + 4, cx, cy + 8);
                        break;
                    case 3: // Insumos (Caja)
                        g2.drawRect(cx - 8, cy - 6, 16, 12);
                        g2.drawLine(cx - 8, cy, cx + 8, cy);
                        break;
                    case 4: // Herramientas (Llave)
                        g2.drawOval(cx + 2, cy - 8, 6, 6);
                        g2.drawLine(cx + 3, cy - 3, cx - 6, cy + 6);
                        g2.drawLine(cx + 1, cy - 1, cx - 4, cy + 8);
                        break;
                    case 5: // Chip (Asistente)
                        g2.drawRect(cx - 6, cy - 6, 12, 12);
                        g2.drawRect(cx - 2, cy - 2, 4, 4);
                        g2.drawLine(cx - 8, cy - 2, cx - 6, cy - 2);
                        g2.drawLine(cx - 8, cy + 2, cx - 6, cy + 2);
                        g2.drawLine(cx + 6, cy - 2, cx + 8, cy - 2);
                        g2.drawLine(cx + 6, cy + 2, cx + 8, cy + 2);
                        g2.drawLine(cx - 2, cy - 8, cx - 2, cy - 6);
                        g2.drawLine(cx + 2, cy - 8, cx + 2, cy - 6);
                        g2.drawLine(cx - 2, cy + 6, cx - 2, cy + 8);
                        g2.drawLine(cx + 2, cy + 6, cx + 2, cy + 8);
                        break;
                }
                g2.dispose();
            }
        };
        pnlIcon.setOpaque(false);
        pnlIcon.setPreferredSize(new Dimension(50, 50));
        gbc.gridy = y++;
        card.add(pnlIcon, gbc);
        
        gbc.gridy = y++;
        card.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        
        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTit.setForeground(Color.WHITE);
        gbc.gridy = y++;
        card.add(lblTit, gbc);
        
        gbc.gridy = y++;
        card.add(Box.createRigidArea(new Dimension(0, 8)), gbc);
        
        JLabel lblDesc = new JLabel("<html><div style='text-align: center;'>"+desc+"</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(203, 213, 225)); // Slate 300
        gbc.gridy = y++;
        card.add(lblDesc, gbc);

        return card;
    }
}
