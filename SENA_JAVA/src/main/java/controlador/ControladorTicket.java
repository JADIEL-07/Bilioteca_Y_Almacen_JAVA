package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Ticket;

public class ControladorTicket implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Ticket unTicket) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unTicket.insertar();
                break;
            case "Modificar":
                unTicket.modificar();
                break;
            case "Eliminar":
                unTicket.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Método requerido por la interfaz, pero usaremos controlarAccion(evt, obj) 
        // para pasar el objeto directamente desde la Vista, como lo hace SistemaClientes.
    }
}
