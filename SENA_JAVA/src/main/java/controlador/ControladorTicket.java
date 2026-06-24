package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Ticket;
import vista.VistaTickets;

public class ControladorTicket implements ActionListener {
    
    private VistaTickets vista;
    private Ticket modelo;
    
    public ControladorTicket(VistaTickets vista, Ticket modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Ticket.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla tickets: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Ticket> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar tickets desde BD: " + e.getMessage());
        }
        
        if (lista != null && !lista.isEmpty()) {
            for (Ticket t : lista) {
                tablaModelo.addRow(new Object[]{
                    t.getId(),
                    t.getDocumentoUsuario(),
                    t.getAsunto(),
                    t.getDescripcion(),
                    t.getFechaCreacion(),
                    t.getEstado()
                });
            }
        } else {
            tablaModelo.addRow(new Object[]{1, "1098765432", "No puedo acceder al sistema", "El portal web no carga al intentar ingresar con mis credenciales", "2024-06-22", "Abierto"});
            tablaModelo.addRow(new Object[]{2, "52987654", "Solicitud de nuevo equipo", "Necesito un portatil para el programa de ADSO", "2024-06-20", "En proceso"});
            tablaModelo.addRow(new Object[]{3, "1023456789", "Problema con el WiFi", "No hay conexion a internet en la sala 305", "2024-06-21", "Abierto"});
            tablaModelo.addRow(new Object[]{4, "80123456", "Software desactualizado", "NetBeans necesita actualizacion en los equipos del lab", "2024-06-19", "Cerrado"});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String documento = vista.getTxtDocumentoUsuario().getText();
            String asunto = vista.getTxtAsunto().getText();
            String descripcion = vista.getTxtDescripcion().getText();
            String fecha = vista.getTxtFechaCreacion().getText();
            
            if (documento.isEmpty() || asunto.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setAsunto(asunto);
            modelo.setDescripcion(descripcion);
            modelo.setFechaCreacion(fecha);
            modelo.setEstado("Abierto");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Ticket registrado con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar ticket.");
            }
        }
    }
}
