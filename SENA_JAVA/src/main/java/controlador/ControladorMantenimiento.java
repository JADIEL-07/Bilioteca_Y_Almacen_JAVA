package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Mantenimiento;
import vista.VistaMantenimiento;

public class ControladorMantenimiento implements ActionListener {
    
    private VistaMantenimiento vista;
    private Mantenimiento modelo;
    
    public ControladorMantenimiento(VistaMantenimiento vista, Mantenimiento modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Mantenimiento.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla mantenimientos: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Mantenimiento> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar mantenimientos desde BD: " + e.getMessage());
        }
        
        if (lista != null && !lista.isEmpty()) {
            for (Mantenimiento m : lista) {
                tablaModelo.addRow(new Object[]{
                    m.getId(),
                    m.getCodigoItem(),
                    m.getDescripcion(),
                    m.getFechaEnvio(),
                    m.getEstado()
                });
            }
        } else {
            tablaModelo.addRow(new Object[]{1, "INV-2024-160", "Pantalla con lineas de colores, no enciende correctamente", "2024-06-20", "En progreso"});
            tablaModelo.addRow(new Object[]{2, "INV-2024-078", "Punta de prueba dañada, lectura erratica", "2024-06-18", "Completado"});
            tablaModelo.addRow(new Object[]{3, "INV-2024-001", "Teclado no responde, bateria hinchada", "2024-06-22", "En progreso"});
            tablaModelo.addRow(new Object[]{4, "INV-2024-002", "Lampara del proyector quemada", "2024-06-15", "Pendiente"});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String codigo = vista.getTxtCodigoItem().getText();
            String descripcion = vista.getTxtDescripcion().getText();
            String fecha = vista.getTxtFechaEnvio().getText();
            
            if (codigo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setCodigoItem(codigo);
            modelo.setDescripcion(descripcion);
            modelo.setFechaEnvio(fecha);
            modelo.setEstado("En progreso");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Envio a mantenimiento registrado.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar mantenimiento.");
            }
        }
    }
}
