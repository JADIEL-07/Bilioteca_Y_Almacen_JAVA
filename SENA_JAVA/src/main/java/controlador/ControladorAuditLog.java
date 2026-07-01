package controlador;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import modelo.AuditLog;
import vista.VistaAuditoria;

public class ControladorAuditLog implements KeyListener {
    
    private VistaAuditoria vista;
    private AuditLog modelo;
    private Timer autoRefresh;
    
    public ControladorAuditLog(VistaAuditoria vista, AuditLog modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setControlador(this);
        cargarDatosTabla("");
        autoRefresh = new Timer(15000, e -> cargarDatosTabla(vista.getTxtBuscar().getText()));
        autoRefresh.start();
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> filas = new ArrayList<>();
                try {
                    Iterator<AuditLog> iter;
                    if (filtro == null || filtro.trim().isEmpty()) {
                        iter = modelo.listar();
                    } else {
                        iter = modelo.buscar(filtro.trim());
                    }
                    while (iter.hasNext()) {
                        AuditLog log = iter.next();
                        filas.add(new Object[]{
                            log.getId(),
                            log.getUsuario(),
                            log.getAccion(),
                            log.getModulo(),
                            log.getDetalle(),
                            log.getFecha()
                        });
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar auditoría: " + e.getMessage());
                }
                return filas;
            }

            @Override
            protected void done() {
                try {
                    List<Object[]> filas = get();
                    tablaModelo.setRowCount(0);
                    for (Object[] fila : filas) {
                        tablaModelo.addRow(fila);
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar tabla auditoría: " + e.getMessage());
                }
            }
        }.execute();
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDatosTabla(vista.getTxtBuscar().getText());
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
}
