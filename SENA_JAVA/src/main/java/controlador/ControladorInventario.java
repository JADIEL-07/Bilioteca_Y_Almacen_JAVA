package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import modelo.Item;
import vista.VistaInventario;

public class ControladorInventario implements ActionListener, KeyListener {
    
    private VistaInventario vista;
    private Item modelo;
    
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
                                item.getUbicacion(),
                                item.getEstado()
                            });
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
            String nombre = vista.getTxtNombre().getText();
            String codigo = vista.getTxtCodigo().getText();
            String categoria = vista.getTxtCategoria().getText();
            String cantidadStr = vista.getTxtCantidad().getText();
            String ubicacion = vista.getTxtUbicacion().getText();
            
            if (nombre.isEmpty() || codigo.isEmpty() || categoria.isEmpty() || cantidadStr.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor complete los campos obligatorios (Nombre, Código, Categoría, Cantidad, Ubicación).");
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
            modelo.setEstado("Disponible");
            
            if (modelo.insertar()) {
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
