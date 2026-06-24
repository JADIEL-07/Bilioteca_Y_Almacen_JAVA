package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Prestamo;
import vista.VistaPrestamos;

public class ControladorPrestamo implements ActionListener {
    
    private VistaPrestamos vista;
    private Prestamo modelo;
    
    public ControladorPrestamo(VistaPrestamos vista, Prestamo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Prestamo.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla prestamos: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Prestamo> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar prestamos desde BD: " + e.getMessage());
        }
        
        if (lista != null && !lista.isEmpty()) {
            for (Prestamo p : lista) {
                tablaModelo.addRow(new Object[]{
                    p.getId(),
                    p.getDocumentoUsuario(),
                    p.getCodigoItem(),
                    p.getFechaPrestamo(),
                    p.getFechaDevolucion(),
                    p.getEstado()
                });
            }
        } else {
            tablaModelo.addRow(new Object[]{1, "1098765432", "INV-2024-001", "2024-06-20", "2024-06-23", "Activo"});
            tablaModelo.addRow(new Object[]{2, "52987654", "INV-2024-078", "2024-06-18", "2024-06-21", "Devuelto"});
            tablaModelo.addRow(new Object[]{3, "1023456789", "BIB-2024-045", "2024-06-22", "2024-06-25", "Activo"});
            tablaModelo.addRow(new Object[]{4, "80123456", "INV-2024-133", "2024-06-15", "2024-06-18", "Vencido"});
            tablaModelo.addRow(new Object[]{5, "1098765432", "INV-2024-150", "2024-06-24", "2024-06-27", "Activo"});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String documento = vista.getTxtDocumentoUsuario().getText();
            String codigo = vista.getTxtCodigoItem().getText();
            String fechaPrestamo = vista.getTxtFechaPrestamo().getText();
            String fechaDevolucion = vista.getTxtFechaDevolucion().getText();
            
            if (documento.isEmpty() || codigo.isEmpty() || fechaPrestamo.isEmpty() || fechaDevolucion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setCodigoItem(codigo);
            modelo.setFechaPrestamo(fechaPrestamo);
            modelo.setFechaDevolucion(fechaDevolucion);
            modelo.setEstado("Activo");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Préstamo registrado con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar préstamo. Verifique el formato de fecha (YYYY-MM-DD).");
            }
        }
    }
}
