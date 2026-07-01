package vista;

import vista.componentes.*;
import modelo.DashboardDAO;
import modelo.EstadisticasDashboard;
import controlador.ControladorDashboard;
import controlador.ControladorInventario;
import controlador.ControladorMantenimiento;
import controlador.ControladorPrestamo;
import controlador.ControladorReserva;
import controlador.ControladorUsuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import modelo.Item;
import modelo.Mantenimiento;
import modelo.Prestamo;
import modelo.Reserva;
import modelo.Usuario;

public class Dashboard extends JFrame {

    // --- Routing (SPA) ---
    private CardLayout cardLayoutContenido;
    private JPanel contenedorCentral;

    // --- Sidebar ---
    private PanelParticulasAnimadas fondoAnimado;
    private JPanel menuPanel;
    private final String[] links = {"Inicio", "Inventario", "Préstamos", "Reservas",
        "Mantenimiento", "Usuarios", "Auditoría"};
    private String seccionActiva = "Inicio";

    // --- Sidebar Toggle ---
    private boolean isSidebarExpanded = true;
    private JPanel sidebarPanel;
    private JLabel lblAppTitle;
    private JPanel pnlUserText;
    private JLabel lblLogout;

    // --- Métricas del Dashboard ---
    private JLabel lblTotalItems     = new JLabel("0");
    private JLabel lblDisponibles    = new JLabel("0");
    private JLabel lblPrestados      = new JLabel("0");
    private JLabel lblMantenimiento  = new JLabel("0");
    private JLabel lblReservas       = new JLabel("0");
    private JLabel lblElementosEstado = new JLabel(
        "<html><div style='text-align:center'>0<br><span style='font-size:10px;font-weight:normal;'>Total</span></div></html>");

    private ControladorDashboard controlador;

    public Dashboard() {
        setTitle("BIBLIOTECA & ALMACÉN SENA (ADMIN)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        fondoAnimado = new PanelParticulasAnimadas();
        fondoAnimado.setLayout(new BorderLayout());

        // Sidebar
        sidebarPanel = crearSidebar();
        fondoAnimado.add(sidebarPanel, BorderLayout.WEST);

        // Área central con navbar + contenido rotativo
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setOpaque(false);
        mainArea.add(crearNavbarSuperior(), BorderLayout.NORTH);

        cardLayoutContenido = new CardLayout();
        contenedorCentral = new JPanel(cardLayoutContenido);
        contenedorCentral.setOpaque(false);

        contenedorCentral.add(crearContenidoPrincipal(),           "Inicio");
        
        // --- Integración del Módulo de Inventario (MVC) ---
        VistaInventario vistaInventario = new VistaInventario();
        Item modeloItem = new Item();
        ControladorInventario ctrlInventario = new ControladorInventario(vistaInventario, modeloItem);
        contenedorCentral.add(vistaInventario,                     "Inventario");
        // --- Integración del Módulo de Préstamos (MVC) ---
        VistaPrestamos vistaPrestamos = new VistaPrestamos();
        Prestamo modeloPrestamo = new Prestamo();
        ControladorPrestamo ctrlPrestamo = new ControladorPrestamo(vistaPrestamos, modeloPrestamo);
        contenedorCentral.add(vistaPrestamos,                      "Préstamos");
        // --- Integración del Módulo de Reservas (MVC) ---
        VistaReservas vistaReservas = new VistaReservas();
        Reserva modeloReserva = new Reserva();
        ControladorReserva ctrlReserva = new ControladorReserva(vistaReservas, modeloReserva);
        contenedorCentral.add(vistaReservas,                       "Reservas");
        // --- Integración del Módulo de Mantenimiento (MVC) ---
        VistaMantenimiento vistaMantenimiento = new VistaMantenimiento();
        Mantenimiento modeloMantenimiento = new Mantenimiento();
        ControladorMantenimiento ctrlMantenimiento = new ControladorMantenimiento(vistaMantenimiento, modeloMantenimiento);
        contenedorCentral.add(vistaMantenimiento,                  "Mantenimiento");
        // --- Integración del Módulo de Usuarios (MVC) ---
        VistaUsuarios vistaUsuarios = new VistaUsuarios();
        Usuario modeloUsuario = new Usuario();
        ControladorUsuario ctrlUsuario = new ControladorUsuario(vistaUsuarios, modeloUsuario);
        contenedorCentral.add(vistaUsuarios,                       "Usuarios");
        contenedorCentral.add(new VistaAuditoria(),                "Auditoría");
        // --- Integración del Módulo de Notificaciones (MVC) ---
        VistaNotificaciones vistaNotificaciones = new VistaNotificaciones();
        modelo.Notificacion modeloNotificacion = new modelo.Notificacion();
        controlador.ControladorNotificaciones ctrlNotificaciones = new controlador.ControladorNotificaciones(vistaNotificaciones, modeloNotificacion);
        contenedorCentral.add(vistaNotificaciones,           "Notificaciones");

        mainArea.add(contenedorCentral, BorderLayout.CENTER);
        fondoAnimado.add(mainArea, BorderLayout.CENTER);

        setContentPane(fondoAnimado);

        // Controlador de datos
        DashboardDAO dao = new DashboardDAO();
        controlador = new ControladorDashboard(this, dao);
        controlador.iniciar();
    }

    // =========================================================
    //  Actualizar métricas (llamado desde el controlador)
    // =========================================================
    public void actualizarMetricas(EstadisticasDashboard stats) {
        lblTotalItems.setText(String.valueOf(stats.getTotalElementos()));
        lblDisponibles.setText(String.valueOf(stats.getDisponibles()));
        lblPrestados.setText(String.valueOf(stats.getPrestados()));
        lblMantenimiento.setText(String.valueOf(stats.getMantenimiento()));
        lblReservas.setText(String.valueOf(stats.getReservasActivas()));
        lblElementosEstado.setText("<html><div style='text-align:center'>"
            + stats.getTotalElementos()
            + "<br><span style='font-size:10px;font-weight:normal;'>Total</span></div></html>");
        revalidate();
        repaint();
    }

    // =========================================================
    //  SIDEBAR
    // =========================================================
    private JPanel crearSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(15, 23, 42));
        panel.setPreferredSize(new Dimension(260, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 20)));

        // -- TOP: Logo + botón Hamburguesa --
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlLogo.setOpaque(false);

        JLabel logoSena = new JLabel() {
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
            }
        };
        logoSena.setPreferredSize(new Dimension(20, 30));

        lblAppTitle = new JLabel("<html><div style='width:200px;font-weight:bold;font-size:13px;'>"
            + "BIBLIOTECA &amp; ALMAC&Eacute;N SENA</div></html>");
        lblAppTitle.setForeground(Color.WHITE);

        pnlLogo.add(logoSena);
        pnlLogo.add(lblAppTitle);

        // Hamburger
        JPanel pnlHamburger = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        pnlHamburger.setOpaque(false);
        JPanel btnHam = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(8, 11, 22, 11);
                g2.drawLine(8, 17, 22, 17);
                g2.drawLine(8, 23, 22, 23);
                g2.dispose();
            }
        };
        btnHam.setPreferredSize(new Dimension(30, 34));
        btnHam.setOpaque(false);
        btnHam.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHam.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { toggleSidebar(); }
        });
        pnlHamburger.add(btnHam);

        topPanel.add(pnlLogo);
        topPanel.add(pnlHamburger);
        panel.add(topPanel, BorderLayout.NORTH);

        // -- CENTER: Botones de menú --
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        actualizarMenuPanel();

        JScrollPane scrollMenu = new JScrollPane(menuPanel);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.setBorder(null);
        panel.add(scrollMenu, BorderLayout.CENTER);

        // -- BOTTOM: Info de usuario --
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
        lblLogout.setForeground(new Color(239, 68, 68));
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

    // =========================================================
    //  Toggle: colapsar / expandir sidebar
    // =========================================================
    private void toggleSidebar() {
        isSidebarExpanded = !isSidebarExpanded;
        sidebarPanel.setPreferredSize(new Dimension(isSidebarExpanded ? 260 : 70, 0));
        lblAppTitle.setVisible(isSidebarExpanded);
        pnlUserText.setVisible(isSidebarExpanded);
        lblLogout.setVisible(isSidebarExpanded);
        actualizarMenuPanel();
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }

    // =========================================================
    //  Menú dinámico (se repinta al cambiar sección o toggle)
    // =========================================================
    private void actualizarMenuPanel() {
        menuPanel.removeAll();
        for (String link : links) {
            boolean isActive = link.equals(seccionActiva);
            JPanel pnlLink = crearBotonMenu(link, isActive);
            pnlLink.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { cambiarSeccion(link); }
            });
            menuPanel.add(pnlLink);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void cambiarSeccion(String nuevaSeccion) {
        seccionActiva = nuevaSeccion;
        actualizarMenuPanel();
        cardLayoutContenido.show(contenedorCentral, nuevaSeccion);
    }

    // =========================================================
    //  Crear botón de menú individual
    // =========================================================
    private JPanel crearBotonMenu(String texto, boolean active) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        pnl.setOpaque(active);
        if (active) pnl.setBackground(new Color(57, 169, 0, 30));
        pnl.setMaximumSize(new Dimension(240, 42));
        pnl.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Si está colapsado usar borde redondeado
        if (!isSidebarExpanded) {
            pnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));
            pnl.setMaximumSize(new Dimension(60, 42));
        }

        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = active ? SenaColores.VERDE_SENA : new Color(148, 163, 184);
                g2.setColor(c);
                g2.setStroke(new BasicStroke(1.5f));
                switch (texto) {
                    case "Inicio":
                        g2.drawRect(2, 4, 12, 10); g2.drawLine(8, 0, 2, 4); g2.drawLine(8, 0, 14, 4);
                        break;
                    case "Inventario":
                        g2.drawRect(2, 2, 12, 12); g2.drawLine(2, 6, 14, 6);
                        break;
                    case "Préstamos":
                        g2.drawRect(3, 1, 10, 14); g2.drawLine(6, 4, 10, 4); g2.drawLine(6, 7, 10, 7); g2.drawLine(6, 10, 10, 10);
                        break;
                    case "Reservas":
                        g2.drawRect(2, 3, 12, 10); g2.drawLine(5, 1, 5, 5); g2.drawLine(11, 1, 11, 5);
                        break;
                    case "Mantenimiento":
                        g2.drawLine(2, 14, 7, 9); g2.drawOval(7, 2, 7, 7); g2.drawLine(9, 9, 14, 14);
                        break;
                    case "Usuarios":
                        g2.drawOval(5, 1, 6, 6); g2.drawArc(1, 10, 14, 8, 0, 180);
                        break;
                    default: // Auditoría
                        g2.drawRect(2, 2, 12, 13); g2.drawLine(5, 6, 11, 6); g2.drawLine(5, 9, 11, 9); g2.drawLine(5, 12, 8, 12);
                }
                g2.dispose();
            }
        };
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

    // =========================================================
    //  NAVBAR SUPERIOR
    // =========================================================
    private JPanel crearNavbarSuperior() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        rightPanel.setOpaque(false);

        // Link CONTACTO
        JLabel lblContacto = new JLabel("CONTACTO");
        lblContacto.setForeground(Color.WHITE);
        lblContacto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContacto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(lblContacto);

        // Campana con badge
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
            @Override public void mouseClicked(MouseEvent e) { cambiarSeccion("Notificaciones"); }
        });
        rightPanel.add(lblBell);

        // Avatar
        JLabel avatarNav = new JLabel("A", SwingConstants.CENTER) {
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
        avatarNav.setForeground(Color.WHITE);
        avatarNav.setFont(new Font("Segoe UI", Font.BOLD, 12));
        avatarNav.setPreferredSize(new Dimension(32, 32));
        rightPanel.add(avatarNav);

        nav.add(rightPanel, BorderLayout.EAST);
        return nav;
    }

    // =========================================================
    //  CONTENIDO PRINCIPAL (Inicio - Dashboard)
    // =========================================================
    private JPanel crearContenidoPrincipal() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Fila de KPI Cards
        JPanel kpiRow = new JPanel(new GridLayout(1, 5, 15, 0));
        kpiRow.setOpaque(false);
        kpiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        kpiRow.add(crearKpiCard("Total de elementos", lblTotalItems,  "", new Color(99, 102, 241)));
        kpiRow.add(crearKpiCard("Disponibles",         lblDisponibles, "0% del total", SenaColores.VERDE_SENA));
        kpiRow.add(crearKpiCard("Prestados",           lblPrestados,   "0% del total", new Color(245, 158, 11)));
        kpiRow.add(crearKpiCard("Mantenimiento",       lblMantenimiento, "0% del total", new Color(239, 68, 68)));
        kpiRow.add(crearKpiCard("Reservas activas",    lblReservas,    "Pendientes por aprobar", new Color(139, 92, 246)));

        // Fila central: Gráficos
        JPanel middleRow = new JPanel(new GridLayout(1, 3, 15, 0));
        middleRow.setOpaque(false);
        middleRow.setPreferredSize(new Dimension(0, 220));

        middleRow.add(crearCardGrafica("Préstamos por mes",   true));
        middleRow.add(crearCardGrafica("Elementos por estado", false));
        crearActivityPanel_y_agregar(middleRow);

        // Fila inferior
        JPanel bottomRow = new JPanel(new GridLayout(1, 3, 15, 0));
        bottomRow.setOpaque(false);
        bottomRow.setPreferredSize(new Dimension(0, 160));

        bottomRow.add(crearCardGrafica("Próximas devoluciones", false));
        bottomRow.add(crearCardGrafica("Categorías principales", false));
        bottomRow.add(new JPanel() {{ setOpaque(false); }}); // espacio

        mainPanel.add(kpiRow,    BorderLayout.NORTH);
        mainPanel.add(middleRow, BorderLayout.CENTER);
        mainPanel.add(bottomRow, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel crearKpiCard(String title, JLabel valorLabel, String subtitle, Color acento) {
        PanelCristal card = new PanelCristal();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 18, 15, 18));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(148, 163, 184));
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Icono de acento
        JPanel iconCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(acento.getRed(), acento.getGreen(), acento.getBlue(), 40));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(acento);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(3, 3, getWidth()-6, getHeight()-6);
                g2.dispose();
            }
        };
        iconCircle.setPreferredSize(new Dimension(32, 32));
        iconCircle.setOpaque(false);

        top.add(lblTitle, BorderLayout.CENTER);
        top.add(iconCircle, BorderLayout.EAST);

        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valorLabel.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setForeground(acento);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        card.add(top,        BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        card.add(lblSub,     BorderLayout.SOUTH);

        return card;
    }

    private JPanel crearCardGrafica(String title, boolean conCombo) {
        PanelCristal pnl = new PanelCristal();
        pnl.setLayout(new BorderLayout());
        pnl.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);

        if (conCombo) {
            JPanel combo = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                }
            };
            combo.setOpaque(false);
            combo.setBorder(new EmptyBorder(4, 10, 4, 10));
            JLabel lblCombo = new JLabel("Este año ▾");
            lblCombo.setForeground(Color.WHITE);
            lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            combo.add(lblCombo);
            header.add(combo, BorderLayout.EAST);
        }

        pnl.add(header, BorderLayout.NORTH);

        // Contenido placeholder
        JLabel placeholder;
        if (title.equals("Elementos por estado")) {
            lblElementosEstado.setHorizontalAlignment(SwingConstants.CENTER);
            lblElementosEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblElementosEstado.setForeground(Color.WHITE);
            pnl.add(lblElementosEstado, BorderLayout.CENTER);
        } else {
            String msg = title.equals("Próximas devoluciones") ? "No hay devoluciones pendientes"
                       : title.equals("Categorías principales") ? "Sin datos registrados" : "";
            placeholder = new JLabel(msg, SwingConstants.CENTER);
            placeholder.setForeground(new Color(148, 163, 184));
            placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            pnl.add(placeholder, BorderLayout.CENTER);
        }

        return pnl;
    }

    private void crearActivityPanel_y_agregar(JPanel parent) {
        PanelCristal pnl = new PanelCristal();
        pnl.setLayout(new BorderLayout());
        pnl.setBorder(new EmptyBorder(18, 18, 18, 18));

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

        parent.add(pnl);
    }

    // =========================================================
    //  Panel placeholder "En Construcción"
    // =========================================================
    private JPanel crearPanelEnConstruccion(String titulo) {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setOpaque(false);
        JLabel lbl = new JLabel(titulo + " — En Construcción", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(new Color(148, 163, 184));
        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }
}
