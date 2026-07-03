package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import modelo.AuditLog;
import modelo.Item;
import vista.VistaInventario;

public class ControladorInventario implements ActionListener, KeyListener {
    
    private VistaInventario vista;
    private Item modelo;
    private Timer autoRefresh;
    
    public ControladorInventario(VistaInventario vista, Item modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Item.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla items: " + e.getMessage());
        }
        
        cargarDatosTabla("");
        autoRefresh = new Timer(15000, e -> { if (vista.isShowing()) cargarDatosTabla(vista.getTxtBuscar().getText()); });
        autoRefresh.start();

        // Conectar el modal de edición
        vista.setControladorEditar(this);
        vista.setOnEditarItem(() -> {
            int fila = vista.getTabla().getSelectedRow();
            if (fila < 0) return;
            int id          = (int) vista.getModeloTabla().getValueAt(fila, 0);
            String nombre   = (String) vista.getModeloTabla().getValueAt(fila, 1);
            String codigo   = (String) vista.getModeloTabla().getValueAt(fila, 2);
            String stock    = String.valueOf(vista.getModeloTabla().getValueAt(fila, 4));
            String ubicacion = String.valueOf(vista.getModeloTabla().getValueAt(fila, 6));
            String estado   = String.valueOf(vista.getModeloTabla().getValueAt(fila, 7));
            vista.abrirEditorFila(id, nombre, codigo, stock, ubicacion, estado);
        });
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        
        new SwingWorker<List<Item>, Void>() {
            @Override
            protected List<Item> doInBackground() {
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
                    List<Item> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Item item : lista) {
                            tablaModelo.addRow(new Object[]{
                                item.getId(),
                                item.getNombre(),
                                item.getCodigo(),
                                item.getCategoria(),
                                item.getCantidad(),     // Stock Total
                                item.getDisponibles(),  // Disponibles
                                item.getUbicacion() != null ? item.getUbicacion() : "",
                                item.getEstado()
                            });
                        }
                    }
                    int total = lista != null ? lista.size() : 0;
                    vista.getLblContador().setText(total + " elemento" + (total != 1 ? "s" : ""));
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
            String nombre      = vista.getTxtNombre().getText().trim();
            String codigo      = vista.getTxtCodigo().getText().trim();
            String categoria   = (String) vista.getCbCategoria().getSelectedItem();
            String cantidadStr = vista.getTxtCantidad().getText().trim();
            String ubicacion   = (String) vista.getCbUbicacion().getSelectedItem();
            String estadoSel   = (String) vista.getCbEstado().getSelectedItem();

            if (nombre.isEmpty() || codigo.isEmpty() || cantidadStr.isEmpty() || ubicacion == null) {
                JOptionPane.showMessageDialog(vista, "Por favor complete todos los campos obligatorios.");
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "La cantidad debe ser un número válido.");
                return;
            }

            modelo.setNombre(nombre);
            modelo.setCodigo(codigo);
            modelo.setCategoria(categoria);
            modelo.setCantidad(cantidad);
            modelo.setUbicacion(ubicacion);
            modelo.setEstado(estadoSel != null ? estadoSel : "Disponible");

            if (modelo.insertar()) {
                AuditLog.registrar("admin", "CREATE", "Inventario", "Item: " + nombre + " (" + codigo + ")");
                JOptionPane.showMessageDialog(vista, "Elemento guardado con éxito.");
                vista.cerrarDialogo();
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el elemento.");
            }
        }

        if (accion.equals("EditarGuardar")) {
            int id = vista.getIdItemSeleccionado();
            if (id < 0) return;

            String cantidadStr = vista.getTxtEditCantidad().getText().trim();
            String ubicacion   = (String) vista.getCbEditUbicacion().getSelectedItem();
            String estadoSel   = (String) vista.getCbEditEstado().getSelectedItem();

            if (cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El stock total no puede estar vacío.");
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "El stock debe ser un número entero positivo.");
                return;
            }

            // Obtener nombre y código actuales desde la fila seleccionada
            int fila = vista.getTabla().getSelectedRow();
            String nombre   = (String) vista.getModeloTabla().getValueAt(fila, 1);
            String codigo   = (String) vista.getModeloTabla().getValueAt(fila, 2);
            String categoria = (String) vista.getModeloTabla().getValueAt(fila, 3);

            modelo.setId(id);
            modelo.setNombre(nombre);
            modelo.setCodigo(codigo);
            modelo.setCategoria(categoria);
            modelo.setCantidad(cantidad);
            modelo.setUbicacion(ubicacion);
            modelo.setEstado(estadoSel != null ? estadoSel : "Disponible");

            if (modelo.modificar()) {
                AuditLog.registrar("admin", "UPDATE", "Inventario",
                    "Stock actualizado: " + nombre + " → " + cantidad + " unidades");
                JOptionPane.showMessageDialog(vista, "✅ Elemento actualizado con éxito.");
                vista.cerrarDialogoEditar();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el elemento.");
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDatosTabla(vista.getTxtBuscar().getText());
        }
    }
}
