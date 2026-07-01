package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class VistaAuditoria extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;

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
        JLabel t = new JLabel("Auditor\u00eda del Sistema");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Registro de todas las acciones y movimientos en el sistema");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t);
        tp.add(Box.createRigidArea(new Dimension(0, 4)));
        tp.add(s);

        txtBuscar = new CampoTextoModerno("Buscar en logs...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        acciones.add(txtBuscar);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Usuario", "Acci\u00f3n", "M\u00f3dulo", "Detalle", "Fecha"};
        modeloTabla = new DefaultTableModel(cols, 0) { 
            public boolean isCellEditable(int r, int c) { return false; } 
        };
        tabla = new JTable(modeloTabla);

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
    
    public void setControlador(KeyListener c) {
        txtBuscar.addKeyListener(c);
    }
}
