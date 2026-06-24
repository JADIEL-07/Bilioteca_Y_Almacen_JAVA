package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import vista.VistaUsuarios;

public class ControladorUsuario implements ActionListener {
    
    private VistaUsuarios vista;
    private Usuario modelo;
    
    public ControladorUsuario(VistaUsuarios vista, Usuario modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        
        try {
            Usuario.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla usuarios: " + e.getMessage());
        }
        
        cargarDatosTabla();
    }
    
    public void cargarDatosTabla() {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);
        
        List<Usuario> lista = null;
        try {
            lista = modelo.listar();
        } catch (Exception e) {
            System.err.println("Error al listar usuarios desde BD: " + e.getMessage());
        }
        
        if (lista != null && !lista.isEmpty()) {
            for (Usuario u : lista) {
                tablaModelo.addRow(new Object[]{
                    u.getId(),
                    u.getNombre(),
                    u.getDocumento(),
                    u.getTipo(),
                    u.getEmail(),
                    u.getCelular(),
                    u.getEstado()
                });
            }
        } else {
            tablaModelo.addRow(new Object[]{1, "Carlos Andrés Pérez", "1098765432", "Aprendiz", "carlos.perez@sena.edu.co", "3201234567", "Activo"});
            tablaModelo.addRow(new Object[]{2, "María Fernanda López", "52987654", "Instructor", "maria.lopez@sena.edu.co", "3109876543", "Activo"});
            tablaModelo.addRow(new Object[]{3, "Juan David Rodríguez", "80123456", "Administrativo", "juan.rodriguez@sena.edu.co", "3154567890", "Activo"});
            tablaModelo.addRow(new Object[]{4, "Ana Sofía Martínez", "1023456789", "Aprendiz", "ana.martinez@sena.edu.co", "3001122334", "Activo"});
            tablaModelo.addRow(new Object[]{5, "Diego Alejandro Torres", "79654321", "Instructor", "diego.torres@sena.edu.co", "3187654321", "Inactivo"});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String nombre = vista.getTxtNombre().getText();
            String documento = vista.getTxtDocumento().getText();
            String tipo = (String) vista.getCbTipo().getSelectedItem();
            String email = vista.getTxtEmail().getText();
            String celular = vista.getTxtCelular().getText();
            
            if (nombre.isEmpty() || documento.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre y documento son obligatorios.");
                return;
            }
            
            modelo.setNombre(nombre);
            modelo.setDocumento(documento);
            modelo.setTipo(tipo);
            modelo.setEmail(email);
            modelo.setCelular(celular);
            modelo.setEstado("Activo");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Usuario registrado con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar. Puede que el documento ya exista.");
            }
        }
    }
}
