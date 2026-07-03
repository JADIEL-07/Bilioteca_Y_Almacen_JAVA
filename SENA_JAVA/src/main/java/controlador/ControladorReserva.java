package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import modelo.AuditLog;
import modelo.Item;
import modelo.Reserva;
import vista.VistaReservas;

public class ControladorReserva implements ActionListener, KeyListener {
    private SwingWorker<Item, Void> currentWorker;
    private SwingWorker<List<Reserva>, Void> workerTabla;
    
    private VistaReservas vista;
    private Reserva modelo;
    private Timer autoRefresh;
    private Timer validacionDebounce;
    
    public ControladorReserva(VistaReservas vista, Reserva modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Reserva.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla reservas: " + e.getMessage());
        }
        
        cargarDatosTabla("");
        autoRefresh = new Timer(30000, e -> { if (vista.isShowing()) cargarDatosTabla(vista.getTxtBuscar().getText()); });
        autoRefresh.start();

        validacionDebounce = new Timer(500, e -> validarDisponibilidad());
        validacionDebounce.setRepeats(false);

        vista.setOnCodigoChanged(() -> {
            validacionDebounce.restart();
        });

        // Conectar modal de edición de estado
        vista.setControladorEditar(this);
        vista.setOnEditarFila(() -> {
            int fila = vista.getTabla().getSelectedRow();
            if (fila < 0) return;
            int id       = (int)   vista.getModeloTabla().getValueAt(fila, 0);
            String doc   = (String) vista.getModeloTabla().getValueAt(fila, 1);
            String cod   = (String) vista.getModeloTabla().getValueAt(fila, 2);
            String estado = (String) vista.getModeloTabla().getValueAt(fila, 4);
            vista.abrirEditorFila(id, "Usuario: " + doc + "  |  Item: " + cod, estado);
        });
    }

    private void validarDisponibilidad() {
        String codigo = vista.getTxtCodigoItem().getText().trim();
        if (codigo.isEmpty()) {
            vista.ocultarDisponibilidad();
            return;
        }

        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
        }
        
        currentWorker = new SwingWorker<Item, Void>() {
            @Override
            protected Item doInBackground() {
                return Item.consultarDisponibilidad(codigo);
            }
            @Override
            protected void done() {
                try {
                    Item item = get();
                    if (item == null) {
                        vista.mostrarDisponibilidad(false, 0, null);
                    } else {
                        vista.mostrarDisponibilidad(true, item.getDisponibles(), item.getFechaLibre());
                    }
                } catch (Exception e) {
                    vista.ocultarDisponibilidad();
                }
            }
        };
        currentWorker.execute();
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        if (workerTabla != null && !workerTabla.isDone()) {
            workerTabla.cancel(true);
        }

        workerTabla = new SwingWorker<List<Reserva>, Void>() {
            @Override
            protected List<Reserva> doInBackground() {
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
                    List<Reserva> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Reserva item : lista) {
                            tablaModelo.addRow(obtenerFila(item));
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar tabla: " + e.getMessage());
                }
            }
        };
        workerTabla.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String documento = vista.getTxtDocumentoUsuario().getText().trim();
            String codigo = vista.getTxtCodigoItem().getText().trim();
            String fecha = vista.getTxtFechaReserva().getText().trim();
            
            if (documento.isEmpty() || codigo.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }

            Item itemDispo = Item.consultarDisponibilidad(codigo);
            if (itemDispo == null) {
                JOptionPane.showMessageDialog(vista, "El ítem especificado no existe.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setCodigoItem(codigo);
            modelo.setFechaReserva(fecha);
            modelo.setEstado("Pendiente");
            
            if (modelo.insertar()) {
                AuditLog.registrar("admin", "CREATE", "Reservas", "Item: " + codigo + " / Usuario: " + documento);
                JOptionPane.showMessageDialog(vista, "Reserva registrada con éxito.");
                vista.cerrarDialogo();
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar reserva.");
            }
        }

        if (accion.equals("EditarGuardar")) {
            int id = vista.getIdFilaSeleccionada();
            if (id < 0) return;
            String nuevoEstado = (String) vista.getCbEditEstado().getSelectedItem();
            
            if (modelo.modificarEstado(id, nuevoEstado)) {
                AuditLog.registrar("admin", "UPDATE", "Reservas", "ID " + id + " → " + nuevoEstado);
                JOptionPane.showMessageDialog(vista, "✅ Estado actualizado correctamente.");
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
            String texto = vista.getTxtBuscar().getText();
            cargarDatosTabla(texto);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private Object[] obtenerFila(Reserva item) {
        return new Object[]{
            item.getId(),
            item.getDocumentoUsuario(),
            item.getCodigoItem(),
            item.getFechaReserva(),
            item.getEstado()
        };
    }
}
