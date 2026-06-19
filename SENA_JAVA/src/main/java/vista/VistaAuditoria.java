package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaAuditoria extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaAuditoria() {
        setBackground(SenaColores.FONDO_OSCURO);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        JPanel tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        tp.setOpaque(false);
        JLabel t = new JLabel("Auditoría del Sistema");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Registro completo de todas las acciones realizadas en el sistema");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t); tp.add(Box.createRigidArea(new Dimension(0,4))); tp.add(s);

        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acc.setOpaque(false);
        acc.add(new CampoTextoModerno("Buscar en log...") {{ setPreferredSize(new Dimension(220, 38)); }});

        header.add(tp, BorderLayout.WEST);
        header.add(acc, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        String[] cols = {"ID", "Usuario", "Acción", "Módulo", "Fecha", "Detalles"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        modeloTabla.addRow(new Object[]{"256", "admin", "CREATE", "Usuarios", "2026-06-17 14:30:00", "Creó usuario juan.rodriguez"});
        modeloTabla.addRow(new Object[]{"255", "admin", "UPDATE", "Inventario", "2026-06-17 13:45:00", "Actualizó cantidad de Laptop HP"});
        modeloTabla.addRow(new Object[]{"254", "bibliotecario", "CREATE", "Préstamos", "2026-06-17 12:15:00", "Registró préstamo #43"});
        modeloTabla.addRow(new Object[]{"253", "admin", "DELETE", "Reservas", "2026-06-17 11:00:00", "Eliminó reserva #12 (expirada)"});
        modeloTabla.addRow(new Object[]{"252", "soporte", "UPDATE", "Tickets", "2026-06-17 10:30:00", "Cerró ticket #4 como resuelto"});
        modeloTabla.addRow(new Object[]{"251", "admin", "LOGIN", "Auth", "2026-06-17 08:00:00", "Inicio de sesión exitoso"});
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }
}
