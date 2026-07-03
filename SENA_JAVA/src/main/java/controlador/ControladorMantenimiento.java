package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import modelo.AuditLog;
import modelo.Mantenimiento;
import vista.VistaMantenimiento;

public class ControladorMantenimiento implements ActionListener, KeyListener {

    private VistaMantenimiento vista;
    private Mantenimiento modelo;
    private Timer autoRefresh;

    public ControladorMantenimiento(VistaMantenimiento vista, Mantenimiento modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }

        try {
            Mantenimiento.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla mantenimientos: " + e.getMessage());
        }

        cargarDatosTabla("");
        autoRefresh = new Timer(30000, e -> { if (vista.isShowing()) cargarDatosTabla(vista.getTxtBuscar().getText()); });
        autoRefresh.start();

        // Conectar modal de edición de estado
        vista.setControladorEditar(this);
        vista.setOnEditarFila(() -> {
            int fila = vista.getTabla().getSelectedRow();
            if (fila < 0) return;
            int id       = (int)   vista.getModeloTabla().getValueAt(fila, 0);
            String item  = (String) vista.getModeloTabla().getValueAt(fila, 1);
            String desc  = (String) vista.getModeloTabla().getValueAt(fila, 2);
            String estado = (String) vista.getModeloTabla().getValueAt(fila, 5);
            vista.abrirEditorFila(id, "Item: " + item + "  |  " + desc, estado);
        });
    }

    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<java.util.List<Mantenimiento>, Void>() {
            @Override
            protected java.util.List<Mantenimiento> doInBackground() {
                try {
                    if (filtro == null || filtro.trim().isEmpty()) {
                        return modelo.listar();
                    } else {
                        return modelo.buscar(filtro.trim());
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar datos: " + e.getMessage());
                    return new java.util.ArrayList<>();
                }
            }

            @Override
            protected void done() {
                try {
                    java.util.List<Mantenimiento> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Mantenimiento item : lista) {
                            tablaModelo.addRow(obtenerFila(item));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar tabla: " + e.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();

        if (accion.equals("Guardar")) {
            String codigo      = vista.getCodigoSeleccionado();
            String descripcion = vista.getTxtDescripcion().getText().trim();
            String severidad   = (String) vista.getCbSeveridad().getSelectedItem();
            String reportadoPor = vista.getTxtReportadoPor().getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor selecciona un elemento del inventario.");
                return;
            }
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor describe la falla.");
                return;
            }
            if (reportadoPor.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor ingresa quién reporta la falla.");
                return;
            }

            modelo.setCodigoItem(codigo);
            modelo.setDescripcion(descripcion);
            modelo.setSeveridad(severidad);
            modelo.setReportadoPor(reportadoPor);
            modelo.setEstado("PENDING");

            if (modelo.insertar()) {
                AuditLog.registrar("admin", "CREATE", "Mantenimiento",
                    "Falla reportada en: " + codigo + " — " + descripcion);
                JOptionPane.showMessageDialog(vista, "✅ Falla reportada exitosamente. El elemento queda en revisión.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista,
                    "❌ Error al registrar el reporte. Verifica que el elemento exista en inventario.");
            }
        }

        if (accion.equals("EditarGuardar")) {
            int id = vista.getIdFilaSeleccionada();
            if (id < 0) return;
            String nuevoEstado = (String) vista.getCbEditEstado().getSelectedItem();
            
            if (modelo.modificarEstado(id, nuevoEstado)) {
                AuditLog.registrar("admin", "UPDATE", "Mantenimiento", "ID " + id + " → " + nuevoEstado);
                JOptionPane.showMessageDialog(vista, "✅ Estado de mantenimiento actualizado.");
                vista.cerrarDialogoEditar();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el estado.");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDatosTabla(vista.getTxtBuscar().getText());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private Object[] obtenerFila(Mantenimiento item) {
        return new Object[]{
            item.getId(),
            item.getCodigoItem(),
            item.getDescripcion(),
            item.getSeveridad() != null ? item.getSeveridad() : "—",
            item.getFechaEnvio(),
            item.getEstado()
        };
    }
}
