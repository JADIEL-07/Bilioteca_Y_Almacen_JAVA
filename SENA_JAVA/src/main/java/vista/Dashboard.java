package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel sidebar;
    private PanelParticulasAnimadas fondoAnimado;
    private String seccionActiva = "Inicio";

    public Dashboard() {
        setTitle("BIBLIOTECA & ALMACÉN SENA (ADMIN)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        fondoAnimado = new PanelParticulasAnimadas();
        fondoAnimado.setLayout(new BorderLayout());

        // Sidebar on West
        fondoAnimado.add(crearSidebar(), BorderLayout.WEST);

        // Main area on Center (Navbar + Content)
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setOpaque(false);
        mainArea.add(crearNavbarSuperior(), BorderLayout.NORTH);
        mainArea.add(crearContenidoPrincipal(), BorderLayout.CENTER);

        fondoAnimado.add(mainArea, BorderLayout.CENTER);

        setContentPane(fondoAnimado);
    }

    private JPanel crearSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 23, 42)); // Solid dark blue
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 20)));

        // Top Sidebar (Logo and Hamburger)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Top Row: Logo text + Icon
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLogo.setOpaque(false);
        
        JLabel logoSena = new JLabel("SENA") { // Simple placeholder for logo
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(0, 5, 20, 20);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.drawString("S", 7, 19);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logoSena.setPreferredSize(new Dimension(20, 30));
        
        JLabel lblTitle = new JLabel("<html><div style='width:150px; font-weight:bold; font-size:13px;'>BIBLIOTECA & ALMACÉN SENA (ADMIN)</div></html>");
        lblTitle.setForeground(Color.WHITE);
        
        pnlLogo.add(logoSena);
        pnlLogo.add(lblTitle);
        
        // Hamburger button
        JPanel pnlHamburger = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        pnlHamburger.setOpaque(false);
        JPanel btnHam = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.drawLine(8, 12, 22, 12);
                g2.drawLine(8, 17, 22, 17);
                g2.drawLine(8, 22, 22, 22);
                g2.dispose();
            }
        };
        btnHam.setPreferredSize(new Dimension(30, 34));
        btnHam.setOpaque(false);
        pnlHamburger.add(btnHam);

        topPanel.add(pnlLogo);
        topPanel.add(pnlHamburger);

        panel.add(topPanel, BorderLayout.NORTH);

        // Center Sidebar (Menu Links)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] links = {"Inicio", "Inventario", "Préstamos", "Reservas", "Mantenimiento", "Salidas", "Reportes", "Usuarios", "Solicitudes", "Configuración", "Auditoría"};
        
        for (String link : links) {
            boolean isActive = link.equals("Inicio");
            JPanel pnlLink = crearBotonMenu(link, isActive);
            menuPanel.add(pnlLink);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JScrollPane scrollMenu = new JScrollPane(menuPanel);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.setBorder(null);
        panel.add(scrollMenu, BorderLayout.CENTER);

        // Bottom Sidebar (User)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        userInfo.setOpaque(false);
        
        JLabel avatar = new JLabel("A", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatar.setPreferredSize(new Dimension(36, 36));
        
        JPanel pnlUserText = new JPanel();
        pnlUserText.setLayout(new BoxLayout(pnlUserText, BoxLayout.Y_AXIS));
        pnlUserText.setOpaque(false);
        
        JLabel lblAdminName = new JLabel("Administrador");
        lblAdminName.setForeground(new Color(203, 213, 225));
        lblAdminName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlUserText.add(lblAdminName);
        
        userInfo.add(avatar);
        userInfo.add(pnlUserText);
        
        JLabel lblLogout = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblLogout.setForeground(new Color(239, 68, 68)); // Red 500
        lblLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogout.setBorder(new EmptyBorder(15, 0, 0, 0));
        lblLogout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });
        
        bottomPanel.add(userInfo, BorderLayout.CENTER);
        bottomPanel.add(lblLogout, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearBotonMenu(String texto, boolean active) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        pnl.setOpaque(false);
        pnl.setMaximumSize(new Dimension(240, 40));
        pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (active) {
            pnl.setOpaque(true);
            pnl.setBackground(new Color(57, 169, 0, 20)); // Green tint
        }
        
        // Icon placeholder
        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(active ? SenaColores.VERDE_SENA : new Color(148, 163, 184));
                g2.setStroke(new BasicStroke(1.5f));
                if (texto.equals("Inicio")) {
                    g2.drawRect(2, 4, 12, 10);
                    g2.drawLine(8, 0, 2, 4);
                    g2.drawLine(8, 0, 14, 4);
                } else if (texto.equals("Inventario")) {
                    g2.drawRect(2, 2, 12, 12);
                    g2.drawLine(2, 6, 14, 6);
                } else {
                    g2.drawOval(4, 4, 8, 8); // Generic
                }
                g2.dispose();
            }
        };
        lblIcon.setPreferredSize(new Dimension(16, 16));
        
        JLabel lblText = new JLabel(texto);
        lblText.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 13));
        lblText.setForeground(active ? SenaColores.VERDE_SENA : new Color(203, 213, 225));
        
        pnl.add(lblIcon);
        pnl.add(lblText);
        
        return pnl;
    }

    private JPanel crearNavbarSuperior() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        rightPanel.setOpaque(false);

        // Contacto Link
        JLabel lblContacto = new JLabel("CONTACTO");
        lblContacto.setForeground(Color.WHITE);
        lblContacto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContacto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(lblContacto);

        // Bell Icon
        JLabel lblBell = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawArc(4, 6, 12, 10, 0, 180);
                g2.drawLine(4, 11, 4, 16);
                g2.drawLine(16, 11, 16, 16);
                g2.drawLine(2, 16, 18, 16);
                g2.drawArc(8, 16, 4, 4, 180, 180);
                
                // Red badge
                g2.setColor(new Color(239, 68, 68));
                g2.fillOval(12, 2, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 8));
                g2.drawString("2", 14, 9);
                
                g2.dispose();
            }
        };
        lblBell.setPreferredSize(new Dimension(24, 24));
        rightPanel.add(lblBell);

        // Avatar
        JLabel avatar = new JLabel("A", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatar.setPreferredSize(new Dimension(28, 28));
        rightPanel.add(avatar);

        nav.add(rightPanel, BorderLayout.EAST);
        
        // Add subtle bottom border
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(nav, BorderLayout.CENTER);
        JPanel border = new JPanel();
        border.setBackground(new Color(255, 255, 255, 20));
        border.setPreferredSize(new Dimension(0, 1));
        container.add(border, BorderLayout.SOUTH);
        
        return container;
    }

    private JScrollPane crearContenidoPrincipal() {
        JPanel pnlContent = new JPanel(new GridBagLayout());
        pnlContent.setOpaque(false);
        pnlContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // ---- ROW 0: 5 Top KPI Cards ----
        gbc.weighty = 0.0; // Fixed height for top cards
        gbc.weightx = 0.2; // 5 cards = 1.0 total
        gbc.gridy = 0;
        
        gbc.gridx = 0; pnlContent.add(crearKpiCard("Total de elementos", "0", 0, null), gbc);
        gbc.gridx = 1; pnlContent.add(crearKpiCard("Disponibles", "0", 1, "0% del total"), gbc);
        gbc.gridx = 2; pnlContent.add(crearKpiCard("Prestados", "0", 2, "0% del total"), gbc);
        gbc.gridx = 3; pnlContent.add(crearKpiCard("Mantenimiento", "0", 3, "0% del total"), gbc);
        gbc.gridx = 4; pnlContent.add(crearKpiCard("Reservas activas", "0", 4, "Pendientes por aprobar"), gbc);

        // ---- ROW 1: Middle Charts + Right Activity ----
        gbc.gridy = 1;
        gbc.weighty = 0.6; // Take up more vertical space
        
        // Préstamos por mes (Takes 2 columns)
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.4;
        pnlContent.add(crearChartPanel("Préstamos por mes", true), gbc);
        
        // Elementos por estado (Takes 2 columns)
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.4;
        pnlContent.add(crearChartPanel("Elementos por estado", false), gbc);
        
        // Actividad Reciente (Takes 1 column, spans 2 rows)
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        pnlContent.add(crearActivityPanel(), gbc);

        // ---- ROW 2: Bottom Small Charts ----
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weighty = 0.4; // Slightly less vertical space than main charts
        
        // Próximas devoluciones (Takes 2 columns)
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0.4;
        pnlContent.add(crearChartPanel("Próximas devoluciones", false), gbc);
        
        // Categorías principales (Takes 2 columns)
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0.4;
        pnlContent.add(crearChartPanel("Categorías principales", false), gbc);

        JScrollPane scroll = new JScrollPane(pnlContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        return scroll;
    }

    private PanelCristal crearKpiCard(String title, String value, int type, String subtitle) {
        PanelCristal card = new PanelCristal();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(0, 100)); // Dynamic width, fixed height preference
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 15, 0, 15);
        c.gridy = 0;
        
        // Icon
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = new Color(57, 169, 0, 30);
                Color iconColor = SenaColores.VERDE_SENA;
                
                if (type == 2) { bgColor = new Color(234, 179, 8, 30); iconColor = new Color(234, 179, 8); } // Yellow
                if (type == 3) { bgColor = new Color(249, 115, 22, 30); iconColor = new Color(249, 115, 22); } // Orange
                if (type == 4) { bgColor = new Color(168, 85, 247, 30); iconColor = new Color(168, 85, 247); } // Purple
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(iconColor);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth()/2;
                int cy = getHeight()/2;
                
                switch (type) {
                    case 0: // Box
                        g2.drawRect(cx-8, cy-6, 16, 12);
                        g2.drawLine(cx-8, cy, cx+8, cy);
                        break;
                    case 1: // Check
                        g2.drawOval(cx-10, cy-10, 20, 20);
                        g2.drawLine(cx-4, cy+2, cx-1, cy+5);
                        g2.drawLine(cx-1, cy+5, cx+6, cy-4);
                        break;
                    case 2: // Book
                        g2.drawRect(cx-9, cy-7, 8, 14);
                        g2.drawRect(cx-1, cy-7, 8, 14);
                        break;
                    case 3: // Wrench
                        g2.drawOval(cx+1, cy-8, 6, 6);
                        g2.drawLine(cx+2, cy-3, cx-6, cy+8);
                        g2.drawLine(cx, cy-1, cx-4, cy+10);
                        break;
                    case 4: // Calendar
                        g2.drawRect(cx-8, cy-7, 16, 14);
                        g2.drawLine(cx-8, cy-2, cx+8, cy-2);
                        g2.drawLine(cx-4, cy-9, cx-4, cy-5);
                        g2.drawLine(cx+4, cy-9, cx+4, cy-5);
                        break;
                }
                g2.dispose();
            }
        };
        pnlIcon.setOpaque(false);
        pnlIcon.setPreferredSize(new Dimension(44, 44));
        c.gridx = 0;
        c.gridheight = 2;
        c.weightx = 0.0;
        card.add(pnlIcon, c);
        
        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(203, 213, 225));
        c.gridx = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.insets = new Insets(0, 15, 2, 15);
        card.add(lblTitle, c);
        
        // Value
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblVal.setForeground(Color.WHITE);
        c.gridy = 1;
        c.insets = new Insets(0, 15, 0, 15);
        card.add(lblVal, c);
        
        // Subtitle
        if (subtitle != null) {
            JLabel lblSub = new JLabel(subtitle);
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            Color cSub = SenaColores.VERDE_SENA;
            if (type == 2) cSub = new Color(234, 179, 8);
            if (type == 3) cSub = new Color(249, 115, 22);
            lblSub.setForeground(cSub);
            c.gridy = 2;
            c.insets = new Insets(2, 15, 0, 15);
            card.add(lblSub, c);
        }
        
        return card;
    }

    private PanelCristal crearChartPanel(String title, boolean hasDropdown) {
        PanelCristal pnl = new PanelCristal();
        pnl.setLayout(new BorderLayout());
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);
        
        if (hasDropdown) {
            JPanel combo = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
            };
            combo.setOpaque(false);
            combo.setBorder(new EmptyBorder(4, 10, 4, 10));
            JLabel lblCombo = new JLabel("Este año v");
            lblCombo.setForeground(Color.WHITE);
            lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            combo.add(lblCombo);
            header.add(combo, BorderLayout.EAST);
        }
        
        pnl.add(header, BorderLayout.NORTH);
        
        // Placeholder text
        JLabel lblContent = new JLabel(title.equals("Elementos por estado") ? "0 Total" : 
                (title.equals("Préstamos por mes") ? "" : "No hay datos disponibles"));
        lblContent.setHorizontalAlignment(SwingConstants.CENTER);
        lblContent.setForeground(new Color(148, 163, 184));
        lblContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (title.equals("Elementos por estado")) {
            lblContent.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblContent.setText("<html><div style='text-align:center'>0<br><span style='font-size:10px; font-weight:normal;'>Total</span></div></html>");
            lblContent.setForeground(Color.WHITE);
        } else if (title.equals("Próximas devoluciones")) {
            lblContent.setText("No hay devoluciones pendientes");
        } else if (title.equals("Categorías principales")) {
            lblContent.setText("Sin datos registrados");
        }
        
        pnl.add(lblContent, BorderLayout.CENTER);
        
        return pnl;
    }

    private PanelCristal crearActivityPanel() {
        PanelCristal pnl = new PanelCristal();
        pnl.setLayout(new BorderLayout());
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Actividad reciente");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        pnl.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblEmpty = new JLabel("No hay actividad reciente", SwingConstants.CENTER);
        lblEmpty.setForeground(new Color(148, 163, 184));
        lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnl.add(lblEmpty, BorderLayout.CENTER);
        
        JLabel lblLink = new JLabel("Ver todas las actividades", SwingConstants.CENTER);
        lblLink.setForeground(SenaColores.VERDE_SENA);
        lblLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pnl.add(lblLink, BorderLayout.SOUTH);
        
        return pnl;
    }
}
