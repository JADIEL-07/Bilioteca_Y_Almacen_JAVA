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
        autoRefresh = new Timer(30000, e -> cargarDatosTabla(vista.getTxtBuscar().getText()));
        autoRefresh.start();
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

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
                                item.getCantidad(),
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
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el elemento.");
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
