package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Reserva;
import vista.VistaReservas;

public class ControladorReserva implements ActionListener {
    
    private VistaReservas vista;
    private Reserva modelo;
    
    public ControladorReserva(VistaReservas vista, Reserva modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Reserva.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla reservas: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Reserva> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar reservas desde BD: " + e.getMessage());
        }
        
        if (lista != null && !lista.isEmpty()) {
            for (Reserva r : lista) {
                tablaModelo.addRow(new Object[]{
                    r.getId(),
                    r.getDocumentoUsuario(),
                    r.getCodigoItem(),
                    r.getFechaReserva(),
                    r.getEstado()
                });
            }
        } else {
            tablaModelo.addRow(new Object[]{1, "1098765432", "INV-2024-002", "2024-06-25", "Pendiente"});
            tablaModelo.addRow(new Object[]{2, "52987654", "BIB-2024-045", "2024-06-26", "Confirmada"});
            tablaModelo.addRow(new Object[]{3, "1023456789", "INV-2024-133", "2024-06-27", "Pendiente"});
            tablaModelo.addRow(new Object[]{4, "80123456", "INV-2024-001", "2024-06-24", "Cancelada"});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String documento = vista.getTxtDocumentoUsuario().getText();
            String codigo = vista.getTxtCodigoItem().getText();
            String fecha = vista.getTxtFechaReserva().getText();
            
            if (documento.isEmpty() || codigo.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setCodigoItem(codigo);
            modelo.setFechaReserva(fecha);
            modelo.setEstado("Pendiente");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Reserva registrada con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar reserva.");
            }
        }
    }
}
