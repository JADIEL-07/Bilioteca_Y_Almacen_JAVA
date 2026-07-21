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
import modelo.Usuario;
import vista.VistaUsuarios;

public class ControladorUsuario implements ActionListener, KeyListener {

    private VistaUsuarios vista;
    private Usuario modelo;
    private Timer autoRefresh;

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
        autoRefresh = new Timer(30000, e -> { if (vista.isShowing()) cargarDatosTabla(vista.getTxtBuscar().getText()); });
        autoRefresh.start();
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
                    if (lista != null) {
                        for (Usuario u : lista) {
                            tablaModelo.addRow(obtenerFila(u));
                        }
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

            Usuario uInsertar = new Usuario();
            uInsertar.setNombre(nombre);
            uInsertar.setDocumento(documento);
            uInsertar.setTipo(tipo);
            uInsertar.setEmail(email);
            uInsertar.setCelular(celular);
            uInsertar.setEstado("Activo");

            vista.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    boolean ok = uInsertar.insertar();
                    if (ok) {
                        AuditLog.registrar("admin", "CREATE", "Usuarios", "Usuario: " + nombre + " (" + documento + ")");
                    }
                    return ok;
                }

                @Override
                protected void done() {
                    vista.setCursor(java.awt.Cursor.getDefaultCursor());
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(vista, "Usuario registrado con éxito.");
                            vista.getDialogo().setVisible(false);
                            vista.limpiarFormulario();
                            cargarDatosTabla("");
                        } else {
                            JOptionPane.showMessageDialog(vista, "Error al registrar. Puede que el documento ya exista.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(vista, "Error de red al registrar usuario: " + ex.getMessage());
                    }
                }
            }.execute();
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
