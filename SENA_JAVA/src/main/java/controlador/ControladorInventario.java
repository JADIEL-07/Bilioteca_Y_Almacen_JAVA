package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
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

        new SwingWorker<java.util.List<Item>, Void>() {
            @Override
            protected java.util.List<Item> doInBackground() {
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
                    java.util.List<Item> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Item item : lista) {
                            tablaModelo.addRow(obtenerFila(item));
                        }
                    } else {
                        // No data found, table remains empty
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
            
            if (nombre.isEmpty() || codigo.isEmpty() || categoria.isEmpty() || cantidadStr.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor complete los campos obligatorios.");
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

    private Object[] obtenerFila(Item item) {
        return new Object[]{
                    item.getId(),
                    item.getNombre(),
                    item.getCodigo(),
                    item.getCategoria(),
                    item.getCantidad(),
                    item.getUbicacion(),
                    item.getEstado()
                };
    }
}
