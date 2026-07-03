package controlador;

import modelo.Usuario;
import vista.Dashboard;
import vista.Login;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;

public class ControladorLogin implements ActionListener {
    private SwingWorker<Boolean, Void> currentWorker;
    
    private Login vistaLogin;
    
    public ControladorLogin(Login vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.vistaLogin.getBtnLogin().addActionListener(this);
        this.vistaLogin.getBtnReg().addActionListener(this);
    }
    
    public void iniciar() {
        vistaLogin.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.getBtnLogin()) {
            manejarLogin();
        }
        
        if (e.getSource() == vistaLogin.getBtnReg()) {
            manejarRegistro();
        }
    }

    private void manejarLogin() {
        String documento = vistaLogin.getTxtLogDoc().getRealText().trim();
        String password = new String(vistaLogin.getTxtLogPass().getPassword());

        if (documento.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin, "Por favor, ingresa tus credenciales.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deshabilitar el botón y mostrar cursor de espera para que el usuario sepa que está procesando
        vistaLogin.getBtnLogin().setEnabled(false);
        vistaLogin.getCursor();
        vistaLogin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // SwingWorker: la consulta corre en segundo plano, la UI no se congela
        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true);
        }
        
        currentWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                // Consulta directa: solo busca el hash del usuario específico (no trae todos)
                String storedHash = Usuario.obtenerHashPassword(documento);
                if (storedHash == null) return false;

                // Compatibilidad: $2b$ -> $2a$ (BCrypt versión de Node.js vs Java)
                if (storedHash.startsWith("$2b$")) {
                    storedHash = "$2a$" + storedHash.substring(4);
                }
                return BCrypt.checkpw(password, storedHash);
            }

            @Override
            protected void done() {
                vistaLogin.getBtnLogin().setEnabled(true);
                vistaLogin.setCursor(Cursor.getDefaultCursor());
                try {
                    boolean auth = get();
                    if (auth) {
                        Dashboard d = new Dashboard();
                        d.setVisible(true);
                        vistaLogin.dispose();
                    } else {
                        JOptionPane.showMessageDialog(vistaLogin, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vistaLogin, "Error de conexión. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.err.println("Error en login: " + ex.getMessage());
                }
            }
        };
        currentWorker.execute();
    }

    private void manejarRegistro() {
        String nombre = vistaLogin.getTxtRegName().getRealText().trim();
        String tipo = vistaLogin.getCmbRegTipo().getSelectedItem().toString();
        String documento = vistaLogin.getTxtRegNum().getRealText().trim();
        String email = vistaLogin.getTxtRegEmail().getRealText().trim();
        String telefono = vistaLogin.getTxtRegPhone().getRealText().trim();
        String password = new String(vistaLogin.getTxtRegPass().getPassword());

        if (nombre.isEmpty() || documento.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin, "Por favor, llena todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        vistaLogin.getBtnReg().setEnabled(false);
        vistaLogin.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        currentWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                Usuario nuevo = new Usuario();
                nuevo.setNombre(nombre);
                nuevo.setTipo(tipo);
                nuevo.setDocumento(documento);
                nuevo.setEmail(email);
                nuevo.setCelular(telefono);
                nuevo.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                nuevo.setEstado("Activo");
                return nuevo.insertar();
            }

            @Override
            protected void done() {
                vistaLogin.getBtnReg().setEnabled(true);
                vistaLogin.setCursor(Cursor.getDefaultCursor());
                try {
                    boolean ok = get();
                    if (ok) {
                        JOptionPane.showMessageDialog(vistaLogin, "Cuenta creada exitosamente. Ya puedes iniciar sesión.");
                        vistaLogin.mostrarLogin();
                    } else {
                        JOptionPane.showMessageDialog(vistaLogin, "Error al crear la cuenta. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vistaLogin, "Error de conexión al registrar.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.err.println("Error en registro: " + ex.getMessage());
                }
            }
        };
        currentWorker.execute();
    }
}
