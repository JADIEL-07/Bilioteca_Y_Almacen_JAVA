package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.Item;

public class ControladorInventario implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, Item unItem) {
        String accion = evento.getActionCommand();
        
        switch (accion) {
            case "Insertar":
                unItem.insertar();
                break;
            case "Modificar":
                unItem.modificar();
                break;
            case "Eliminar":
                unItem.eliminar();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
