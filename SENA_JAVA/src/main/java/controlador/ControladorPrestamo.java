package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Prestamo;

public class ControladorPrestamo implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Prestamo unPrestamo) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unPrestamo.insertar();
                break;
            case "Modificar":
                unPrestamo.modificar();
                break;
            case "Eliminar":
                unPrestamo.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Método requerido por la interfaz, pero usaremos controlarAccion(evt, obj) 
        // para pasar el objeto directamente desde la Vista, como lo hace SistemaClientes.
    }
}
