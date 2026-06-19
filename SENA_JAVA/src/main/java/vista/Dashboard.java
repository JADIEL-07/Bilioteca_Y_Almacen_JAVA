package vista;

import modelo.ConexionBD;
import vista.componentes.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private List<JButton> botonesMenu = new ArrayList<>();
    private List<String> idsMenu = new ArrayList<>();
    private String seccionActiva = "dashboard";

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

        seleccionarMenu("dashboard");
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
        logoCircle.setForeground(Color.WHITE);
        logoCircle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoCircle.setPreferredSize(new Dimension(38, 38));
        logoCircle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titulo = new JLabel("BIBLIOTECA & ALMACÉN SENA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        logoPanel.add(logoCircle);
        logoPanel.add(titulo);

        // Right: Avatar
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        rightPanel.setOpaque(false);

        JLabel lblAdmin = new JLabel("Administrador");
        lblAdmin.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));

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
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(acento);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 4));
        bar.setOpaque(false);

        card.add(top, BorderLayout.CENTER);
        card.add(bar, BorderLayout.SOUTH);

        return card;
    }

}
