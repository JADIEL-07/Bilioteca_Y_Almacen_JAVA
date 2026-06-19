package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.SalidaItem;

public class ControladorSalidaItem implements ActionListener {

    public void controlarAccion(ActionEvent evento, SalidaItem unaSalidaItem) {
        String accion = evento.getActionCommand();

        switch (accion) {
            case "Insertar":
                unaSalidaItem.insertar();
                break;
            case "Modificar":
                unaSalidaItem.modificar();
                break;
            case "Eliminar":
                unaSalidaItem.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
