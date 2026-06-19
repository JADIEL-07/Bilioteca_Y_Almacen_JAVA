package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Usuario;

public class ControladorUsuario implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Usuario unUsuario) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unUsuario.insertar();
                break;
            case "Modificar":
                unUsuario.modificar();
                break;
            case "Eliminar":
                unUsuario.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Método requerido por la interfaz, pero usaremos controlarAccion(evt, obj) 
        // para pasar el objeto directamente desde la Vista, como lo hace SistemaClientes.
    }
}
