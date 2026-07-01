package controlador;

import modelo.Usuario;
import vista.Dashboard;
import vista.Login;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class ControladorLogin implements ActionListener {
    
    private Login vistaLogin;
    
    public ControladorLogin(Login vistaLogin) {
        this.vistaLogin = vistaLogin;
        
        // Asignar listeners a los botones
        this.vistaLogin.getBtnLogin().addActionListener(this);
        this.vistaLogin.getBtnReg().addActionListener(this);
    }
    
    public void iniciar() {
        vistaLogin.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaLogin.getBtnLogin()) {
            String documento = vistaLogin.getTxtLogDoc().getRealText();
            String password = new String(vistaLogin.getTxtLogPass().getPassword());
            
            if (documento.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vistaLogin, "Por favor, ingresa tus credenciales.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validacion temporal (Demo o Base de Datos)
            if (documento.equals("admin") || documento.equals("12345")) {
                // Login exitoso demo
                Dashboard d = new Dashboard();
                d.setVisible(true);
                vistaLogin.dispose();
            } else {
                // Verificar en base de datos
                Usuario modeloUsuario = new Usuario();
                List<Usuario> usuarios = modeloUsuario.listar();
                boolean auth = false;
                
                for (Usuario u : usuarios) {
                    if (u.getDocumento() != null && u.getDocumento().equals(documento)) {
                        String storedHash = u.getPassword();
                        if (storedHash != null) {
                            if (storedHash.startsWith("$2b$")) {
                                storedHash = "$2a$" + storedHash.substring(4);
                            }
                            if (BCrypt.checkpw(password, storedHash)) {
                                auth = true;
                                break;
                            }
                        }
                    }
                }
                
                if (auth) {
                    Dashboard d = new Dashboard();
                    d.setVisible(true);
                    vistaLogin.dispose();
                } else {
                    JOptionPane.showMessageDialog(vistaLogin, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        if (e.getSource() == vistaLogin.getBtnReg()) {
            String nombre = vistaLogin.getTxtRegName().getRealText();
            String tipo = vistaLogin.getCmbRegTipo().getSelectedItem().toString();
            String documento = vistaLogin.getTxtRegNum().getRealText();
            String email = vistaLogin.getTxtRegEmail().getRealText();
            String telefono = vistaLogin.getTxtRegPhone().getRealText();
            String password = new String(vistaLogin.getTxtRegPass().getPassword());
            
            if (nombre.isEmpty() || documento.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(vistaLogin, "Por favor, llena todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setTipo(tipo);
            nuevo.setDocumento(documento);
            nuevo.setEmail(email);
            nuevo.setCelular(telefono);
            nuevo.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            nuevo.setEstado("Activo");
            
            if (nuevo.insertar()) {
                JOptionPane.showMessageDialog(vistaLogin, "Cuenta creada exitosamente. Ya puedes iniciar sesión.");
                vistaLogin.mostrarLogin();
            } else {
                JOptionPane.showMessageDialog(vistaLogin, "Error al crear la cuenta. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
