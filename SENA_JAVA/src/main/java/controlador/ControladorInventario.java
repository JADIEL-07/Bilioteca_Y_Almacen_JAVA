package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Item;
import vista.VistaInventario;

public class ControladorInventario implements ActionListener {
    
    private VistaInventario vista;
    private Item modelo;
    
    public ControladorInventario(VistaInventario vista, Item modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Item.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla items: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Item> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar items desde BD: " + e.getMessage());
        }
        
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
        } else {
            // Datos de demostración cuando la BD está vacía o no conecta
            tablaModelo.addRow(new Object[]{1, "Laptop HP ProBook 450", "INV-2024-001", "Equipos", 15, "Sala 201", "Disponible"});
            tablaModelo.addRow(new Object[]{2, "Proyector Epson S41+", "INV-2024-002", "Equipos", 8, "Almacén A", "Disponible"});
            tablaModelo.addRow(new Object[]{3, "Java: Cómo Programar - Deitel", "BIB-2024-045", "Libros", 25, "Biblioteca", "Disponible"});
            tablaModelo.addRow(new Object[]{4, "Multímetro Fluke 117", "INV-2024-078", "Herramientas", 12, "Taller 3", "En préstamo"});
            tablaModelo.addRow(new Object[]{5, "Cable HDMI 3m", "INV-2024-120", "Insumos", 50, "Almacén B", "Disponible"});
            tablaModelo.addRow(new Object[]{6, "Kit Arduino Mega 2560", "INV-2024-133", "Equipos", 20, "Lab. Electrónica", "Disponible"});
            tablaModelo.addRow(new Object[]{7, "Mouse inalámbrico Logitech", "INV-2024-150", "Insumos", 35, "Almacén A", "Disponible"});
            tablaModelo.addRow(new Object[]{8, "Monitor LG 24'' Full HD", "INV-2024-160", "Equipos", 10, "Sala 305", "En mantenimiento"});
        }
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
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el elemento.");
            }
        }
    }
}
