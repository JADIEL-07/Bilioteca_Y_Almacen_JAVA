package vista;

import vista.componentes.PanelCristal;
import vista.componentes.PanelRedondeado;
import vista.componentes.SenaColores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VistaNotificaciones extends JPanel {
    
    private JPanel listaContenedor;
    private JPanel btnFiltroTodas;
    private JPanel btnFiltroNoLeidas;
    private JPanel btnMarcarLeidas;
    private JPanel btnLimpiarTodo;
    private JLabel lblBadgeNum;
    private JPanel pnlBadge;

    public VistaNotificaciones() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        add(crearHeader(), BorderLayout.NORTH);
        add(crearListaNotificaciones(), BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        PanelCristal headerPanel = new PanelCristal();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Izquierda: TÃ­tulo y Badge
        JPanel pnlIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlIzquierda.setOpaque(false);

        // Icono campana
        JLabel lblIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SenaColores.VERDE_SENA);
                g2.setStroke(new BasicStroke(2f));
                g2.drawArc(4, 6, 12, 10, 0, 180);
                g2.drawLine(4, 11, 4, 16);
                g2.drawLine(16, 11, 16, 16);
                g2.drawLine(2, 16, 18, 16);
                g2.drawArc(8, 16, 4, 4, 180, 180);
                g2.dispose();
            }
        };
        lblIcon.setPreferredSize(new Dimension(20, 20));

        JLabel lblTitle = new JLabel("Centro de Notificaciones");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        // Badge verde "2 sin leer"
        pnlBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SenaColores.VERDE_SENA);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        pnlBadge.setOpaque(false);
        pnlBadge.setPreferredSize(new Dimension(70, 22));
        pnlBadge.setLayout(new BorderLayout());
        lblBadgeNum = new JLabel("0 sin leer", SwingConstants.CENTER);
        lblBadgeNum.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadgeNum.setForeground(new Color(15, 23, 42)); // Texto oscuro
        pnlBadge.add(lblBadgeNum, BorderLayout.CENTER);
        pnlBadge.setVisible(false);

        pnlIzquierda.add(lblIcon);
        pnlIzquierda.add(lblTitle);
        pnlIzquierda.add(pnlBadge);

        // Derecha: Botones de filtro
        JPanel pnlDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlDerecha.setOpaque(false);

        btnFiltroTodas = crearBotonFiltro("Todas", true, SenaColores.VERDE_SENA);
        btnFiltroNoLeidas = crearBotonFiltro("No leídas", false, SenaColores.VERDE_SENA);
        btnMarcarLeidas = crearBotonFiltro("Marcar leídas", false, new Color(148, 163, 184));
        btnLimpiarTodo = crearBotonFiltro("Limpiar todo", false, new Color(239, 68, 68));

        pnlDerecha.add(btnFiltroTodas);
        pnlDerecha.add(btnFiltroNoLeidas);
        pnlDerecha.add(btnMarcarLeidas);
        pnlDerecha.add(btnLimpiarTodo);

        headerPanel.add(pnlIzquierda, BorderLayout.WEST);
        headerPanel.add(pnlDerecha, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel crearBotonFiltro(String texto, boolean activo, Color colorAcento) {
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", activo ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(activo ? Color.WHITE : new Color(148, 163, 184));
        
        btn.add(lbl);
        return btn;
    }

    private JPanel crearListaNotificaciones() {
        listaContenedor = new JPanel();
        listaContenedor.setLayout(new BoxLayout(listaContenedor, BoxLayout.Y_AXIS));
        listaContenedor.setOpaque(false);

        // Se usa un panel contenedor que empuja todo hacia arriba
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(listaContenedor, BorderLayout.NORTH);
        
        // Agregar scroll
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }
    
    public void limpiarNotificaciones() {
        listaContenedor.removeAll();
        listaContenedor.revalidate();
        listaContenedor.repaint();
    }
    
    public void agregarTarjetaNotificacion(int id, String titulo, String mensaje, String fecha, boolean noLeido) {
        listaContenedor.add(crearTarjetaNotificacion(titulo, mensaje, fecha, noLeido));
        listaContenedor.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    public void actualizarBadge(int cantidad) {
        if (cantidad > 0) {
            pnlBadge.setVisible(true);
            lblBadgeNum.setText(cantidad + " sin leer");
        } else {
            pnlBadge.setVisible(false);
        }
        pnlBadge.revalidate();
        pnlBadge.repaint();
    }
    
    // Getters para el controlador
    public JPanel getBtnFiltroTodas() { return btnFiltroTodas; }
    public JPanel getBtnFiltroNoLeidas() { return btnFiltroNoLeidas; }
    public JPanel getBtnMarcarLeidas() { return btnMarcarLeidas; }
    public JPanel getBtnLimpiarTodo() { return btnLimpiarTodo; }


    private JPanel crearTarjetaNotificacion(String titulo, String mensaje, String fecha, boolean noLeido) {
        JPanel tarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (noLeido) {
                    g2.setColor(new Color(57, 169, 0, 30)); // Fondo verde translucido
                } else {
                    g2.setColor(new Color(15, 23, 42, 150)); // Fondo oscuro translucido
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                if (noLeido) {
                    g2.setColor(new Color(57, 169, 0, 100)); // Borde verde
                } else {
                    g2.setColor(new Color(255, 255, 255, 20)); // Borde blanco
                }
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Icono circular a la izquierda
        JPanel pnlIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo circular
                g2.setColor(noLeido ? SenaColores.VERDE_SENA : new Color(148, 163, 184));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, 36, 36);
                
                // Campana chiquita adentro
                int cx = 18; int cy = 18;
                g2.drawArc(cx-4, cy-5, 8, 7, 0, 180);
                g2.drawLine(cx-4, cy-1, cx-4, cy+3);
                g2.drawLine(cx+4, cy-1, cx+4, cy+3);
                g2.drawLine(cx-6, cy+3, cx+6, cy+3);
                g2.drawArc(cx-2, cy+3, 4, 3, 180, 180);
                
                g2.dispose();
            }
        };
        pnlIcon.setPreferredSize(new Dimension(50, 40));
        pnlIcon.setOpaque(false);

        // Contenido central
        JPanel pnlContenido = new JPanel(new GridLayout(3, 1, 0, 2));
        pnlContenido.setOpaque(false);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblMensaje = new JLabel(mensaje);
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setForeground(new Color(203, 213, 225));
        
        JLabel lblFecha = new JLabel(fecha);
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFecha.setForeground(new Color(100, 116, 139));
        
        pnlContenido.add(lblTitulo);
        pnlContenido.add(lblMensaje);
        pnlContenido.add(lblFecha);

        // Punto indicador a la derecha
        JPanel pnlIndicador = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (noLeido) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(SenaColores.VERDE_SENA);
                    g2.fillOval(0, 18, 8, 8);
                    g2.dispose();
                }
            }
        };
        pnlIndicador.setPreferredSize(new Dimension(20, 40));
        pnlIndicador.setOpaque(false);

        tarjeta.add(pnlIcon, BorderLayout.WEST);
        tarjeta.add(pnlContenido, BorderLayout.CENTER);
        tarjeta.add(pnlIndicador, BorderLayout.EAST);

        return tarjeta;
    }
}
