package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaInventario extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;

    public VistaInventario() {
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

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);

        JLabel titulo = new JLabel("Gestión de Inventario");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Control de equipos, libros, herramientas e insumos del centro");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        txtBuscar = new CampoTextoModerno("Buscar elemento...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        BotonPlano btnAgregar = new BotonPlano("+ Nuevo Elemento");
        btnAgregar.addActionListener(e -> mostrarDialogo());

        acciones.add(txtBuscar);
        acciones.add(btnAgregar);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnas = {"ID", "Nombre", "Código", "Categoría", "Cantidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);

        modeloTabla.addRow(new Object[]{"1", "Laptop HP ProBook 450", "INV-2024-001", "Equipos", "15", "Disponible"});
        modeloTabla.addRow(new Object[]{"2", "Proyector Epson S41+", "INV-2024-002", "Equipos", "8", "Disponible"});
        modeloTabla.addRow(new Object[]{"3", "Libro: Java Cómo Programar", "BIB-2024-045", "Libros", "25", "Disponible"});
        modeloTabla.addRow(new Object[]{"4", "Multímetro Fluke 117", "INV-2024-078", "Herramientas", "12", "En préstamo"});
        modeloTabla.addRow(new Object[]{"5", "Cable HDMI 3m", "INV-2024-120", "Insumos", "50", "Disponible"});

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void mostrarDialogo() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Elemento", true);
        d.setSize(480, 520);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Nuevo Elemento de Inventario");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        form.add(new CampoTextoModerno("Nombre del Elemento"));
        form.add(new CampoTextoModerno("Código Interno"));
        form.add(new CampoTextoModerno("Categoría (Equipos / Libros / Herramientas)"));
        form.add(new CampoTextoModerno("Cantidad"));
        form.add(new CampoTextoModerno("Ubicación"));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        BotonPlano btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnCancelar.addActionListener(e -> d.dispose());
        BotonPlano btnGuardar = new BotonPlano("Guardar");
        btnGuardar.addActionListener(e -> d.dispose());
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        form.add(botones);

        d.add(form);
        d.setVisible(true);
    }
}
