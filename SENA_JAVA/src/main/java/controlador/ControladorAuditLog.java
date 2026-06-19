package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.AuditLog;

public class ControladorAuditLog implements ActionListener {
    
    public void controlarAccion(ActionEvent evento, AuditLog unAuditLog) {
        // Módulo de solo lectura — no se permiten operaciones de escritura
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
