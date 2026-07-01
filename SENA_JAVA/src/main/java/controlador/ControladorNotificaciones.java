package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.Notificacion;
import vista.VistaNotificaciones;

public class ControladorNotificaciones {

    private VistaNotificaciones vista;
    private Notificacion modelo;
    private boolean verSoloNoLeidas = false;

    public ControladorNotificaciones(VistaNotificaciones vista, Notificacion modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        try {
            Notificacion.inicializarTabla();
        } catch (Exception e) {
            System.err.println("Error al inicializar tabla notificaciones: " + e.getMessage());
        }

        inicializarEventos();
        cargarNotificaciones();
    }

    private void inicializarEventos() {
        // Evento: Ver todas
        agregarEfectoClick(vista.getBtnFiltroTodas(), () -> {
            verSoloNoLeidas = false;
            cargarNotificaciones();
        });

        // Evento: Ver no leídas
        agregarEfectoClick(vista.getBtnFiltroNoLeidas(), () -> {
            verSoloNoLeidas = true;
            cargarNotificaciones();
        });

        // Evento: Marcar todas como leídas
        agregarEfectoClick(vista.getBtnMarcarLeidas(), () -> {
            if (modelo.marcarTodasComoLeidas()) {
                cargarNotificaciones();
            }
        });

        // Evento: Limpiar todo
        agregarEfectoClick(vista.getBtnLimpiarTodo(), () -> {
            int r = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar todas las notificaciones?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                if (modelo.limpiarTodo()) {
                    cargarNotificaciones();
                }
            }
        });
    }

    public void cargarNotificaciones() {
        vista.limpiarNotificaciones();
        
        List<Notificacion> lista = modelo.listar(verSoloNoLeidas);
        for (Notificacion n : lista) {
            vista.agregarTarjetaNotificacion(n.getId(), n.getTitulo(), n.getMensaje(), n.getFecha(), !n.isLeida());
        }
        
        actualizarBadgeContador();
    }
    
    private void actualizarBadgeContador() {
        int count = modelo.contarNoLeidas();
        vista.actualizarBadge(count);
    }

    private void agregarEfectoClick(JPanel panel, Runnable accion) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
    }
}
