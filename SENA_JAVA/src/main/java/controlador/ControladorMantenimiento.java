package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Mantenimiento;

public class ControladorMantenimiento implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Mantenimiento unMantenimiento) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unMantenimiento.insertar();
                break;
            case "Modificar":
                unMantenimiento.modificar();
                break;
            case "Eliminar":
                unMantenimiento.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
