package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Repuesto;

public class ControladorRepuesto implements ActionListener {

    public void controlarAccion(ActionEvent evento, Repuesto unRepuesto) {
        String accion = evento.getActionCommand();

        switch (accion) {
            case "Insertar":
                unRepuesto.insertar();
                break;
            case "Modificar":
                unRepuesto.modificar();
                break;
            case "Eliminar":
                unRepuesto.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
