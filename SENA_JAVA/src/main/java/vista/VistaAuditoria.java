package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaAuditoria extends JPanel {

    public VistaAuditoria() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        tp.setOpaque(false);
        JLabel t = new JLabel("Auditoría del Sistema");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Registro de todas las acciones y movimientos en el sistema");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t);
        tp.add(Box.createRigidArea(new Dimension(0, 4)));
        tp.add(s);

        CampoTextoModerno buscador = new CampoTextoModerno("Buscar en logs...");
        buscador.setPreferredSize(new Dimension(220, 38));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        acciones.add(buscador);
        BotonPlano btnExp = new BotonPlano("Exportar CSV", new Color(100, 116, 139), new Color(71, 85, 105));
        acciones.add(btnExp);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"Fecha / Hora", "Usuario", "Acción", "Módulo", "Detalles", "IP"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { 
            public boolean isCellEditable(int r, int c) { return false; } 
        };
        JTable table = new JTable(model);

        model.addRow(new Object[]{"2024-03-20 10:30", "admin_01", "LOGIN", "Auth", "Inicio de sesión exitoso", "192.168.1.100"});
        model.addRow(new Object[]{"2024-03-20 10:35", "admin_01", "CREATE", "Inventario", "Se añadió Laptop HP ProBook", "192.168.1.100"});
        model.addRow(new Object[]{"2024-03-20 11:15", "asistente_1", "UPDATE", "Préstamos", "Se aprobó préstamo #402", "192.168.1.105"});

        body.add(TablaModerna.crear(table), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }
}
