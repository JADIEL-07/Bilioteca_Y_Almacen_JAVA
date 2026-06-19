package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Reserva;

public class ControladorReserva implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Reserva unaReserva) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unaReserva.insertar();
                break;
            case "Modificar":
                unaReserva.modificar();
                break;
            case "Eliminar":
                unaReserva.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Método requerido por la interfaz, pero usaremos controlarAccion(evt, obj) 
        // para pasar el objeto directamente desde la Vista, como lo hace SistemaClientes.
    }
}
