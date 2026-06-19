package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaTickets extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaTickets() {
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
        JLabel t = new JLabel("Soporte Técnico");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Gestiona solicitudes de soporte y tickets de incidencias");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t); tp.add(Box.createRigidArea(new Dimension(0,4))); tp.add(s);
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acc.setOpaque(false);
        acc.add(new CampoTextoModerno("Buscar ticket...") {{ setPreferredSize(new Dimension(220, 38)); }});
        BotonPlano btn = new BotonPlano("+ Nuevo Ticket");
        btn.addActionListener(e -> mostrarDialogo());
        acc.add(btn);
        header.add(tp, BorderLayout.WEST);
        header.add(acc, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        String[] cols = {"ID", "Solicitante", "Asunto", "Prioridad", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        modeloTabla.addRow(new Object[]{"1", "María López", "Laptop no enciende", "Alta", "2026-06-15", "Abierto"});
        modeloTabla.addRow(new Object[]{"2", "Juan Rodríguez", "Proyector sin señal", "Media", "2026-06-14", "En progreso"});
        modeloTabla.addRow(new Object[]{"3", "Carlos Pérez", "Red WiFi caída Bloque 3", "Crítica", "2026-06-16", "Abierto"});
        modeloTabla.addRow(new Object[]{"4", "Andrés Martínez", "Instalar software CAD", "Baja", "2026-06-13", "Resuelto"});
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void mostrarDialogo() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Ticket", true);
        d.setSize(480, 480);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(SenaColores.FONDO_OSCURO);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel lbl = new JLabel("Crear Ticket de Soporte");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);
        form.add(new CampoTextoModerno("Asunto"));
        form.add(new CampoTextoModerno("Descripción detallada"));
        form.add(new CampoTextoModerno("Prioridad (Baja / Media / Alta / Crítica)"));
        form.add(new CampoTextoModerno("Categoría (Hardware / Software / Red)"));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        BotonPlano bc = new BotonPlano("Cancelar", new Color(100,116,139), new Color(71,85,105));
        bc.addActionListener(e -> d.dispose());
        BotonPlano bg = new BotonPlano("Crear Ticket");
        bg.addActionListener(e -> d.dispose());
        botones.add(bc); botones.add(bg);
        form.add(botones);
        d.add(form);
        d.setVisible(true);
    }
}
