package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import modelo.Usuario;
import vista.VistaUsuarios;

/**
 * Controlador actualizado con carga asíncrona (SwingWorker) y etiqueta flotante
 * en los campos de búsqueda.
 */
public class ControladorUsuario implements ActionListener, KeyListener {

    private VistaUsuarios vista;
    private Usuario modelo;

    public ControladorUsuario(VistaUsuarios vista, Usuario modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }

        try {
            Usuario.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla usuarios: " + e.getMessage());
        }

        cargarDatosTabla("");
    }

    /** Carga la tabla de usuarios de forma asíncrona */
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<java.util.List<Usuario>, Void>() {
            @Override
            protected java.util.List<Usuario> doInBackground() throws Exception {
                if (filtro == null || filtro.trim().isEmpty()) {
                    return modelo.listar();
                } else {
                    return modelo.buscar(filtro.trim());
                }
            }

            @Override
            protected void done() {
                try {
                    List<Usuario> lista = get();
                    if (lista != null && !lista.isEmpty()) {
                        for (Usuario u : lista) {
                            tablaModelo.addRow(obtenerFila(u));
                        }
                    } else {
                        // Datos de demostración cuando la BD está vacía
                        tablaModelo.addRow(new Object[]{1, "Carlos Andrés Pérez", "1098765432", "Aprendiz", "carlos.perez@sena.edu.co", "3201234567", "Activo"});
                        tablaModelo.addRow(new Object[]{2, "María Fernanda López", "52987654", "Instructor", "maria.lopez@sena.edu.co", "3109876543", "Activo"});
                        tablaModelo.addRow(new Object[]{3, "Juan David Rodríguez", "80123456", "Administrativo", "juan.rodriguez@sena.edu.co", "3154567890", "Activo"});
                        tablaModelo.addRow(new Object[]{4, "Ana Sofía Martínez", "1023456789", "Aprendiz", "ana.martinez@sena.edu.co", "3001122334", "Activo"});
                        tablaModelo.addRow(new Object[]{5, "Diego Alejandro Torres", "79654321", "Instructor", "diego.torres@sena.edu.co", "3187654321", "Inactivo"});
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar tabla usuarios: " + e.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        if ("Guardar".equals(accion)) {
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
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar. Puede que el documento ya exista.");
            }
        }
    }

    // ── KeyListener ──────────────────────────────────────
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDatosTabla(vista.getTxtBuscar().getText());
        }
    }

    /** Convierte un objeto Usuario en una fila de la tabla */
    private Object[] obtenerFila(Usuario u) {
        return new Object[]{
            u.getId(),
            u.getNombre(),
            u.getDocumento(),
            u.getTipo(),
            u.getEmail(),
            u.getCelular(),
            u.getEstado()
        };
    }
}
