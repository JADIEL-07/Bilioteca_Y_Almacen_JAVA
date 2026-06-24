package vista;

import modelo.ConexionBD;
import vista.componentes.*;
import modelo.DashboardDAO;
import modelo.EstadisticasDashboard;
import controlador.ControladorDashboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends JFrame {

<<<<<<< Updated upstream
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private List<JButton> botonesMenu = new ArrayList<>();
    private List<String> idsMenu = new ArrayList<>();
    private String seccionActiva = "dashboard";
=======
    private CardLayout cardLayoutContenido;
    private JPanel contenedorCentral;
    private PanelParticulasAnimadas fondoAnimado;
    private JPanel menuPanel;
    private final String[] links = {"Inicio", "Inventario", "Préstamos", "Reservas", "Mantenimiento", "Salidas", "Reportes", "Usuarios", "Solicitudes", "Configuración", "Auditoría"};
    private String seccionActiva = "Inicio";
>>>>>>> Stashed changes

    // Sidebar Toggle Components
    private boolean isSidebarExpanded = true;
    private JPanel sidebarPanel;
    private JLabel lblAppTitle;
    private JPanel pnlUserText;
    private JLabel lblLogout;

    // JLabels para las métricas
    private JLabel lblTotalItems = new JLabel("0");
    private JLabel lblDisponibles = new JLabel("0");
    private JLabel lblPrestados = new JLabel("0");
    private JLabel lblMantenimiento = new JLabel("0");
    private JLabel lblReservas = new JLabel("0");
    private JLabel lblElementosEstado = new JLabel("<html><div style='text-align:center'>0<br><span style='font-size:10px; font-weight:normal;'>Total</span></div></html>");

    private ControladorDashboard controlador;

    public Dashboard() {
        ConexionBD.getInstance();
        setTitle("BIBLIOTECA & ALMACÉN SENA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));
        getContentPane().setBackground(SenaColores.FONDO_OSCURO);
        setLayout(new BorderLayout(0, 0));

        add(crearNavbarSuperior(), BorderLayout.NORTH);
        add(crearSidebar(), BorderLayout.WEST);
        add(crearContenidoPrincipal(), BorderLayout.CENTER);

<<<<<<< Updated upstream
        seleccionarMenu("dashboard");
=======
        // Sidebar on West
        sidebarPanel = crearSidebar();
        fondoAnimado.add(sidebarPanel, BorderLayout.WEST);

        // Main area on Center (Navbar + Content)
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setOpaque(false);
        mainArea.add(crearNavbarSuperior(), BorderLayout.NORTH);

        // Configurar Enrutamiento (SPA Routing)
        cardLayoutContenido = new CardLayout();
        contenedorCentral = new JPanel(cardLayoutContenido);
        contenedorCentral.setOpaque(false);

        // Añadir las vistas al contenedor central
        contenedorCentral.add(crearContenidoPrincipal(), "Inicio");
        contenedorCentral.add(new VistaInventario(), "Inventario");
        contenedorCentral.add(new VistaPrestamos(), "Préstamos");
        contenedorCentral.add(new VistaReservas(), "Reservas");
        contenedorCentral.add(new VistaMantenimiento(), "Mantenimiento");
        contenedorCentral.add(new VistaTickets(), "Solicitudes");
        contenedorCentral.add(new VistaUsuarios(), "Usuarios");
        contenedorCentral.add(new VistaAuditoria(), "Auditoría");
        contenedorCentral.add(new VistaNotificaciones(), "Notificaciones");
        
        // Vistas faltantes usarán paneles vacíos temporalmente
        contenedorCentral.add(crearPanelEnConstruccion("Salidas"), "Salidas");
        contenedorCentral.add(crearPanelEnConstruccion("Reportes"), "Reportes");
        contenedorCentral.add(crearPanelEnConstruccion("Configuración"), "Configuración");

        mainArea.add(contenedorCentral, BorderLayout.CENTER);

        fondoAnimado.add(mainArea, BorderLayout.CENTER);

        setContentPane(fondoAnimado);

        // Inicializar Controlador
        DashboardDAO dao = new DashboardDAO();
        controlador = new ControladorDashboard(this, dao);
        controlador.iniciar();
    }

    public void actualizarMetricas(EstadisticasDashboard stats) {
        lblTotalItems.setText(String.valueOf(stats.getTotalElementos()));
        lblDisponibles.setText(String.valueOf(stats.getDisponibles()));
        lblPrestados.setText(String.valueOf(stats.getPrestados()));
        lblMantenimiento.setText(String.valueOf(stats.getMantenimiento()));
        lblReservas.setText(String.valueOf(stats.getReservasActivas()));
        
        lblElementosEstado.setText("<html><div style='text-align:center'>" + stats.getTotalElementos() + "<br><span style='font-size:10px; font-weight:normal;'>Total</span></div></html>");
        
        // Repaint
        revalidate();
        repaint();
>>>>>>> Stashed changes
    }

    private JPanel crearNavbarSuperior() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(SenaColores.FONDO_OSCURO);
        navbar.setPreferredSize(new Dimension(0, 55));
        navbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SenaColores.BORDE_SUTIL));

        // Left: Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        logoPanel.setOpaque(false);

        JLabel logoCircle = new JLabel("BS", SwingConstants.CENTER) {
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
<<<<<<< Updated upstream
        logoCircle.setForeground(Color.WHITE);
        logoCircle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoCircle.setPreferredSize(new Dimension(38, 38));
        logoCircle.setHorizontalAlignment(SwingConstants.CENTER);
=======
        logoSena.setPreferredSize(new Dimension(20, 30));
        
        lblAppTitle = new JLabel("<html><div style='width:150px; font-weight:bold; font-size:13px;'>BIBLIOTECA & ALMACÉN SENA (ADMIN)</div></html>");
        lblAppTitle.setForeground(Color.WHITE);
        
        pnlLogo.add(logoSena);
        pnlLogo.add(lblAppTitle);
        
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
        btnHam.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSidebar();
            }
        });
        pnlHamburger.add(btnHam);
>>>>>>> Stashed changes

        JLabel titulo = new JLabel("BIBLIOTECA & ALMACÉN SENA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        logoPanel.add(logoCircle);
        logoPanel.add(titulo);

<<<<<<< Updated upstream
        // Right: Avatar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        rightPanel.setOpaque(false);

        JLabel lblAdmin = new JLabel("Administrador");
        lblAdmin.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
=======
        // Center Sidebar (Menu Links)
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        actualizarMenuPanel();
>>>>>>> Stashed changes

        JLabel avatar = new JLabel("AD", SwingConstants.CENTER) {
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
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        avatar.setPreferredSize(new Dimension(36, 36));
<<<<<<< Updated upstream

        rightPanel.add(lblAdmin);
        rightPanel.add(avatar);

        navbar.add(logoPanel, BorderLayout.WEST);
        navbar.add(rightPanel, BorderLayout.EAST);

        return navbar;
    }

    private JPanel crearSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SenaColores.FONDO_OSCURO);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SenaColores.BORDE_SUTIL));

        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        agregarItemMenu("dashboard",     "\u2302   Dashboard");
        agregarItemMenu("usuarios",      "\u263A   Usuarios");
        agregarItemMenu("inventario",    "\u2692   Inventario");
        agregarItemMenu("prestamos",     "\u2261   Préstamos");
        agregarItemMenu("reservas",      "\u2637   Reservas");
        agregarItemMenu("mantenimiento", "\u2699   Mantenimiento");
        agregarItemMenu("tickets",       "\u2709   Soporte Técnico");
        agregarItemMenu("auditoria",     "\u2630   Auditoría");

        sidebar.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 15));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton btnLogout = crearBotonSidebar("\u2190   Cerrar Sesión");
        btnLogout.setForeground(SenaColores.ROJO_PELIGRO);
        btnLogout.addActionListener(e -> System.exit(0));
        sidebar.add(btnLogout);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        return sidebar;
    }

    private void agregarItemMenu(String id, String texto) {
        JButton btn = crearBotonSidebar(texto);
        btn.addActionListener(e -> seleccionarMenu(id));
        botonesMenu.add(btn);
        idsMenu.add(id);
        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 2)));
    }

    private JButton crearBotonSidebar(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(SenaColores.TEXTO_SECUNDARIO);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(230, 42));
        btn.setPreferredSize(new Dimension(230, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 10));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getForeground().equals(Color.WHITE) && !btn.getForeground().equals(SenaColores.ROJO_PELIGRO)) {
                    btn.setBackground(SenaColores.SUPERFICIE);
                    btn.setOpaque(true);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!btn.getForeground().equals(Color.WHITE)) {
                    btn.setOpaque(false);
                }
=======
        
        pnlUserText = new JPanel();
        pnlUserText.setLayout(new BoxLayout(pnlUserText, BoxLayout.Y_AXIS));
        pnlUserText.setOpaque(false);
        
        JLabel lblAdminName = new JLabel("Administrador");
        lblAdminName.setForeground(new Color(203, 213, 225));
        lblAdminName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlUserText.add(lblAdminName);
        
        userInfo.add(avatar);
        userInfo.add(pnlUserText);
        
        lblLogout = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblLogout.setForeground(new Color(239, 68, 68)); // Red 500
        lblLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogout.setBorder(new EmptyBorder(15, 0, 0, 0));
        lblLogout.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Login().setVisible(true);
                dispose();
>>>>>>> Stashed changes
            }
        });

        return btn;
    }

    private void seleccionarMenu(String id) {
        seccionActiva = id;
        for (int i = 0; i < botonesMenu.size(); i++) {
            JButton btn = botonesMenu.get(i);
            boolean activo = idsMenu.get(i).equals(id);
            btn.setForeground(activo ? Color.WHITE : SenaColores.TEXTO_SECUNDARIO);
            btn.setFont(activo ? new Font("Segoe UI", Font.BOLD, 14) : new Font("Segoe UI", Font.PLAIN, 14));
            btn.setOpaque(activo);
            btn.setBackground(activo ? SenaColores.SUPERFICIE : SenaColores.FONDO_OSCURO);
            final boolean isActivo = activo;
            btn.setBorder(BorderFactory.createCompoundBorder(
                isActivo ? BorderFactory.createMatteBorder(0, 3, 0, 0, SenaColores.VERDE_SENA) : BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(0, isActivo ? 19 : 22, 0, 10)
            ));
        }
        cardLayout.show(panelContenido, id);
    }

    private JPanel crearContenidoPrincipal() {
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(SenaColores.FONDO_OSCURO);

        panelContenido.add(crearPanelBienvenida(), "dashboard");
        panelContenido.add(new VistaUsuarios(), "usuarios");
        panelContenido.add(new VistaInventario(), "inventario");
        panelContenido.add(new VistaPrestamos(), "prestamos");
        panelContenido.add(new VistaReservas(), "reservas");
        panelContenido.add(new VistaMantenimiento(), "mantenimiento");
        panelContenido.add(new VistaTickets(), "tickets");
        panelContenido.add(new VistaAuditoria(), "auditoria");

        return panelContenido;
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SenaColores.FONDO_OSCURO);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel titulo = new JLabel("Panel de Control General");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitulo = new JLabel("Bienvenido al Sistema de Biblioteca & Almacén SENA");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(titulo);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitulo);

        JPanel cardsGrid = new JPanel(new GridLayout(2, 4, 20, 20));
        cardsGrid.setOpaque(false);
        cardsGrid.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        cardsGrid.add(crearTarjetaEstadistica("Usuarios", "127", SenaColores.VERDE_SENA));
        cardsGrid.add(crearTarjetaEstadistica("Inventario", "584", SenaColores.AZUL_INFO));
        cardsGrid.add(crearTarjetaEstadistica("Préstamos", "43", SenaColores.AMARILLO_WARN));
        cardsGrid.add(crearTarjetaEstadistica("Reservas", "18", new Color(168, 85, 247)));
        cardsGrid.add(crearTarjetaEstadistica("Mantenimiento", "12", new Color(236, 72, 153)));
        cardsGrid.add(crearTarjetaEstadistica("Tickets", "7", new Color(20, 184, 166)));
        cardsGrid.add(crearTarjetaEstadistica("Salidas", "35", new Color(249, 115, 22)));
        cardsGrid.add(crearTarjetaEstadistica("Auditorías", "256", new Color(99, 102, 241)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(cardsGrid, BorderLayout.CENTER);

        return panel;
    }

<<<<<<< Updated upstream
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color acento) {
        PanelRedondeado card = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(acento);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 38));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblTitulo, BorderLayout.NORTH);
        top.add(Box.createRigidArea(new Dimension(0, 8)), BorderLayout.CENTER);
        top.add(lblValor, BorderLayout.SOUTH);

        JPanel bar = new JPanel() {
=======
    private void toggleSidebar() {
        isSidebarExpanded = !isSidebarExpanded;
        
        // Ajustar el ancho del panel principal
        sidebarPanel.setPreferredSize(new Dimension(isSidebarExpanded ? 260 : 70, 0));
        
        // Mostrar/Ocultar textos
        lblAppTitle.setVisible(isSidebarExpanded);
        pnlUserText.setVisible(isSidebarExpanded);
        lblLogout.setVisible(isSidebarExpanded);
        
        // Refrescar los botones del menú para que oculten o muestren sus textos
        actualizarMenuPanel();
        
        // Repintar layout
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
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
>>>>>>> Stashed changes
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(acento);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
<<<<<<< Updated upstream
        bar.setPreferredSize(new Dimension(0, 4));
        bar.setOpaque(false);
=======
        lblIcon.setPreferredSize(new Dimension(16, 16));
        
        pnl.add(lblIcon);
        
        if (isSidebarExpanded) {
            JLabel lblText = new JLabel(texto);
            lblText.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 13));
            lblText.setForeground(active ? SenaColores.VERDE_SENA : new Color(203, 213, 225));
            pnl.add(lblText);
        }
        
        return pnl;
    }

    private void actualizarMenuPanel() {
        menuPanel.removeAll();
        for (String link : links) {
            boolean isActive = link.equals(seccionActiva);
            JPanel pnlLink = crearBotonMenu(link, isActive);
            
            // Lógica de enrutamiento (Click event)
            pnlLink.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cambiarSeccion(link);
                }
            });
            
            menuPanel.add(pnlLink);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void cambiarSeccion(String nuevaSeccion) {
        if (seccionActiva.equals(nuevaSeccion)) return;
        
        seccionActiva = nuevaSeccion;
        actualizarMenuPanel(); // Repintar el menú (efecto verde)
        cardLayoutContenido.show(contenedorCentral, nuevaSeccion); // Cambiar la vista central
    }

    private JPanel crearPanelEnConstruccion(String titulo) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        JLabel lbl = new JLabel(titulo + " - En Construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(new Color(148, 163, 184));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }
>>>>>>> Stashed changes

        card.add(top, BorderLayout.CENTER);
        card.add(bar, BorderLayout.SOUTH);

<<<<<<< Updated upstream
        return card;
    }

=======
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
        lblBell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblBell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cambiarSeccion("Notificaciones");
            }
        });
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
        // Setup JLabels
        Font valFont = new Font("Segoe UI", Font.BOLD, 22);
        lblTotalItems.setFont(valFont); lblTotalItems.setForeground(Color.WHITE);
        lblDisponibles.setFont(valFont); lblDisponibles.setForeground(Color.WHITE);
        lblPrestados.setFont(valFont); lblPrestados.setForeground(Color.WHITE);
        lblMantenimiento.setFont(valFont); lblMantenimiento.setForeground(Color.WHITE);
        lblReservas.setFont(valFont); lblReservas.setForeground(Color.WHITE);

        gbc.gridx = 0; pnlContent.add(crearKpiCard("Total de elementos", lblTotalItems, 0, null), gbc);
        gbc.gridx = 1; pnlContent.add(crearKpiCard("Disponibles", lblDisponibles, 1, "0% del total"), gbc);
        gbc.gridx = 2; pnlContent.add(crearKpiCard("Prestados", lblPrestados, 2, "0% del total"), gbc);
        gbc.gridx = 3; pnlContent.add(crearKpiCard("Mantenimiento", lblMantenimiento, 3, "0% del total"), gbc);
        gbc.gridx = 4; pnlContent.add(crearKpiCard("Reservas activas", lblReservas, 4, "Pendientes por aprobar"), gbc);

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

    private PanelCristal crearKpiCard(String title, JLabel lblVal, int type, String subtitle) {
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
        JLabel lblContent = new JLabel(title.equals("Préstamos por mes") ? "" : "No hay datos disponibles");
        lblContent.setHorizontalAlignment(SwingConstants.CENTER);
        lblContent.setForeground(new Color(148, 163, 184));
        lblContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (title.equals("Elementos por estado")) {
            lblElementosEstado.setHorizontalAlignment(SwingConstants.CENTER);
            lblElementosEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblElementosEstado.setForeground(Color.WHITE);
            pnl.add(lblElementosEstado, BorderLayout.CENTER);
            return pnl;
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
>>>>>>> Stashed changes
}
