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
import modelo.Prestamo;
import vista.VistaPrestamos;

public class ControladorPrestamo implements ActionListener, KeyListener {
    private SwingWorker<Item, Void> currentWorker;
    private SwingWorker<List<Prestamo>, Void> workerTabla;
    
    private VistaPrestamos vista;
    private Prestamo modelo;
    private Timer autoRefresh;
    private Timer validacionDebounce;
    
    public ControladorPrestamo(VistaPrestamos vista, Prestamo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Prestamo.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla prestamos: " + e.getMessage());
        }
        
        cargarDatosTabla("");
        autoRefresh = new Timer(30000, e -> { if (vista.isShowing()) cargarDatosTabla(vista.getTxtBuscar().getText()); });
        autoRefresh.start();

        // Configurar debounce para validación en vivo (500ms)
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
            String estado = (String) vista.getModeloTabla().getValueAt(fila, 5);
            String fechaDev = (String) vista.getModeloTabla().getValueAt(fila, 4);
            vista.abrirEditorFila(id, "Usuario: " + doc + "  |  Item: " + cod, estado, fechaDev);
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
        
        if (workerTabla != null && !workerTabla.isDone()) {
            workerTabla.cancel(true);
        }
        
        workerTabla = new SwingWorker<List<Prestamo>, Void>() {
            @Override
            protected List<Prestamo> doInBackground() {
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
                    List<Prestamo> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Prestamo item : lista) {
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
            String fechaPrestamo = vista.getTxtFechaPrestamo().getText().trim();
            String fechaDevolucion = vista.getTxtFechaDevolucion().getText().trim();
            
            if (documento.isEmpty() || codigo.isEmpty() || fechaPrestamo.isEmpty() || fechaDevolucion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }

            vista.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() {
                    Item itemDispo = Item.consultarDisponibilidad(codigo);
                    if (itemDispo == null) {
                        return "NO_EXISTE";
                    }
                    if (itemDispo.getDisponibles() <= 0) {
                        return "NO_DISPONIBLE";
                    }

                    Prestamo pInsertar = new Prestamo();
                    pInsertar.setDocumentoUsuario(documento);
                    pInsertar.setCodigoItem(codigo);
                    pInsertar.setFechaPrestamo(fechaPrestamo);
                    pInsertar.setFechaDevolucion(fechaDevolucion);
                    pInsertar.setEstado("Activo");

                    if (pInsertar.insertar()) {
                        AuditLog.registrar("admin", "CREATE", "Préstamos", "Item: " + codigo + " / Usuario: " + documento);
                        return "OK";
                    } else {
                        return "ERROR";
                    }
                }

                @Override
                protected void done() {
                    vista.setCursor(java.awt.Cursor.getDefaultCursor());
                    try {
                        String res = get();
                        switch (res) {
                            case "NO_EXISTE":
                                JOptionPane.showMessageDialog(vista, "El ítem especificado no existe.");
                                break;
                            case "NO_DISPONIBLE":
                                JOptionPane.showMessageDialog(vista, "No se puede completar el préstamo. No hay stock disponible actualmente.");
                                break;
                            case "OK":
                                JOptionPane.showMessageDialog(vista, "Préstamo registrado con éxito.");
                                vista.cerrarDialogo();
                                vista.limpiarFormulario();
                                cargarDatosTabla("");
                                break;
                            default:
                                JOptionPane.showMessageDialog(vista, "Error al registrar préstamo. Verifique el formato de fecha (YYYY-MM-DD).");
                                break;
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "Error de red al registrar préstamo: " + ex.getMessage());
                    }
                }
            }.execute();
        }

        if (accion.equals("EditarGuardar")) {
            int id = vista.getIdFilaSeleccionada();
            if (id < 0) return;
            String nuevoEstado = (String) vista.getCbEditEstado().getSelectedItem();
            String nuevaFecha  = vista.getTxtEditFechaDevolucion().getText().trim();
            if (nuevaFecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "La fecha de devolución no puede estar vacía.");
                return;
            }

            vista.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    boolean ok = modelo.modificarEstado(id, nuevoEstado, nuevaFecha);
                    if (ok) {
                        AuditLog.registrar("admin", "UPDATE", "Préstamos", "ID " + id + " → " + nuevoEstado);
                    }
                    return ok;
                }

                @Override
                protected void done() {
                    vista.setCursor(java.awt.Cursor.getDefaultCursor());
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(vista, "✅ Estado actualizado correctamente.");
                            vista.cerrarDialogoEditar();
                            cargarDatosTabla("");
                        } else {
                            JOptionPane.showMessageDialog(vista, "Error al actualizar el estado.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "Error de red al actualizar estado: " + ex.getMessage());
                    }
                }
            }.execute();
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

    private Object[] obtenerFila(Prestamo item) {
        return new Object[]{
            item.getId(),
            item.getDocumentoUsuario(),
            item.getCodigoItem(),
            item.getFechaPrestamo(),
            item.getFechaDevolucion(),
            item.getEstado()
        };
    }
}
