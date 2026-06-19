package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaReservas extends JPanel {
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public VistaReservas() {
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
        JLabel t = new JLabel("Control de Reservas");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Gestiona las reservas anticipadas de elementos");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t); tp.add(Box.createRigidArea(new Dimension(0,4))); tp.add(s);
        JPanel acc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acc.setOpaque(false);
        acc.add(new CampoTextoModerno("Buscar reserva...") {{ setPreferredSize(new Dimension(220, 38)); }});
        BotonPlano btn = new BotonPlano("+ Nueva Reserva");
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
        String[] cols = {"ID", "Usuario", "Elemento", "Fecha Reserva", "Fecha Retiro", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tabla = new JTable(modeloTabla);
        modeloTabla.addRow(new Object[]{"1", "Andrés Martínez", "Proyector Epson", "2026-06-18", "2026-06-20", "Confirmada"});
        modeloTabla.addRow(new Object[]{"2", "Laura Gómez", "Laptop Dell XPS", "2026-06-19", "2026-06-21", "Pendiente"});
        modeloTabla.addRow(new Object[]{"3", "Carlos Pérez", "Kit Arduino Mega", "2026-06-20", "2026-06-25", "Confirmada"});
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void mostrarDialogo() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Reserva", true);
        d.setSize(480, 420);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(SenaColores.FONDO_OSCURO);
        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        JLabel lbl = new JLabel("Nueva Reserva");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);
        form.add(new CampoTextoModerno("Usuario (Documento o Nombre)"));
        form.add(new CampoTextoModerno("Elemento a Reservar"));
        form.add(new CampoTextoModerno("Fecha de Retiro (YYYY-MM-DD)"));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        BotonPlano bc = new BotonPlano("Cancelar", new Color(100,116,139), new Color(71,85,105));
        bc.addActionListener(e -> d.dispose());
        BotonPlano bg = new BotonPlano("Reservar");
        bg.addActionListener(e -> d.dispose());
        botones.add(bc); botones.add(bg);
        form.add(botones);
        d.add(form);
        d.setVisible(true);
    }
}
