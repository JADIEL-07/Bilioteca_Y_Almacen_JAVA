package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaPrestamos extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaPrestamos() {
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
        JLabel t = new JLabel("Control de Préstamos");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Gestiona los préstamos de elementos a usuarios del centro");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t);
        tp.add(Box.createRigidArea(new Dimension(0, 4)));
        tp.add(s);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        acciones.add(new CampoTextoModerno("Buscar préstamo...") {{ setPreferredSize(new Dimension(220, 38)); }});
        BotonPlano btn = new BotonPlano("+ Nuevo Préstamo");
        btn.addActionListener(e -> mostrarDialogo());
        acciones.add(btn);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Usuario", "Elemento", "Fecha Préstamo", "Fecha Devolución", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);

        modeloTabla.addRow(new Object[]{"1", "Carlos Pérez", "Laptop HP ProBook", "2026-06-10", "2026-06-17", "Activo"});
        modeloTabla.addRow(new Object[]{"2", "María López", "Proyector Epson", "2026-06-12", "2026-06-14", "Devuelto"});
        modeloTabla.addRow(new Object[]{"3", "Juan Rodríguez", "Multímetro Fluke", "2026-06-15", "2026-06-22", "Activo"});
        modeloTabla.addRow(new Object[]{"4", "Laura Gómez", "Java Cómo Programar", "2026-06-01", "2026-06-08", "Vencido"});

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void mostrarDialogo() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Préstamo", true);
        d.setSize(480, 420);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(SenaColores.FONDO_OSCURO);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel lbl = new JLabel("Registrar Préstamo");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);
        form.add(new CampoTextoModerno("Usuario (Documento o Nombre)"));
        form.add(new CampoTextoModerno("Elemento (Código o Nombre)"));
        form.add(new CampoTextoModerno("Fecha de Devolución (YYYY-MM-DD)"));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        BotonPlano bc = new BotonPlano("Cancelar", new Color(100,116,139), new Color(71,85,105));
        bc.addActionListener(e -> d.dispose());
        BotonPlano bg = new BotonPlano("Registrar");
        bg.addActionListener(e -> d.dispose());
        botones.add(bc);
        botones.add(bg);
        form.add(botones);
        d.add(form);
        d.setVisible(true);
    }
}
