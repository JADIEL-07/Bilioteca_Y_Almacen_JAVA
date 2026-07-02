package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaMantenimiento extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoMantenimiento;

    // Modal
    private JDialog dialogo;
    private JComboBox<String> cbElemento;   // carga "Nombre — CÓDIGO" desde BD
    private CampoTextoModerno txtDescripcion;
    private JComboBox<String> cbSeveridad;
    private CampoTextoModerno txtReportadoPor;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaMantenimiento() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
        crearDialogo();
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel tp = new JPanel();
        tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        tp.setOpaque(false);
        JLabel t = new JLabel("Control de Mantenimientos");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Registra y monitorea reportes de fallas de cualquier elemento");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t);
        tp.add(Box.createRigidArea(new Dimension(0, 4)));
        tp.add(s);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        txtBuscar = new CampoTextoModerno("Buscar elemento...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        btnNuevoMantenimiento = new BotonPlano("+ Reportar Falla");

        acciones.add(txtBuscar);
        acciones.add(btnNuevoMantenimiento);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Elemento", "Descripción de Falla", "Severidad", "Fecha", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private JPanel crearEtiqueta(String texto) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(lbl);
        return p;
    }

    private JComboBox<String> crearCombo(String[] opciones) {
        JComboBox<String> cb = new JComboBox<>(opciones);
        cb.setBackground(new Color(30, 41, 59));
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return cb;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Reportar Falla / Mantenimiento", true);
        dialogo.setSize(500, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));

        JLabel titulo = new JLabel("Reportar Falla en Elemento");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(titulo);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        // Elemento (ComboBox cargado dinámicamente)
        cbElemento = crearCombo(new String[]{"Cargando elementos..."});
        cbElemento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbElemento.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(crearEtiqueta("Elemento del Inventario *").getComponent(0) instanceof JLabel
            ? crearEtiqueta("Elemento del Inventario *") : crearEtiqueta("Elemento del Inventario *"));
        form.add(Box.createRigidArea(new Dimension(0, 4)));
        form.add(cbElemento);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // Descripción
        txtDescripcion = new CampoTextoModerno("Describe la falla o problema detectado");
        txtDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(txtDescripcion);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // Severidad
        cbSeveridad = crearCombo(new String[]{"Baja", "Media", "Alta", "Crítica"});
        cbSeveridad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbSeveridad.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(crearEtiqueta("Severidad *"));
        form.add(Box.createRigidArea(new Dimension(0, 4)));
        form.add(cbSeveridad);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        // Reportado por
        txtReportadoPor = new CampoTextoModerno("Nombre o documento de quien reporta");
        txtReportadoPor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        txtReportadoPor.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(txtReportadoPor);
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar  = new BotonPlano("Reportar Falla");

        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoMantenimiento.addActionListener(e -> {
            limpiarFormulario();
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        });

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        form.add(botones);

        dialogo.add(form);
    }

    /** Recarga el JComboBox con todos los ítems del inventario desde la BD. */
    public void cargarElementosEnCombo() {
        new SwingWorker<java.util.List<String>, Void>() {
            @Override
            protected java.util.List<String> doInBackground() {
                java.util.List<String> opciones = new java.util.ArrayList<>();
                try {
                    java.sql.Connection con = modelo.ConexionBD.getInstance().getConnection();
                    String sql = "SELECT name, code FROM items WHERE is_deleted = false ORDER BY name";
                    try (java.sql.PreparedStatement ps = con.prepareStatement(sql);
                         java.sql.ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            opciones.add(rs.getString("name") + " — " + rs.getString("code"));
                        }
                    }
                    modelo.ConexionBD.getInstance().releaseConnection(con);
                } catch (Exception ex) {
                    System.err.println("Error cargando elementos: " + ex.getMessage());
                }
                return opciones;
            }
            @Override
            protected void done() {
                try {
                    java.util.List<String> ops = get();
                    cbElemento.removeAllItems();
                    if (ops.isEmpty()) {
                        cbElemento.addItem("— Sin elementos en inventario —");
                    } else {
                        for (String op : ops) cbElemento.addItem(op);
                    }
                } catch (Exception ignored) {}
            }
        }.execute();
    }

    /** Extrae el código del elemento seleccionado en el combo (ej: "Libro — ISBN-001" → "ISBN-001"). */
    public String getCodigoSeleccionado() {
        String sel = (String) cbElemento.getSelectedItem();
        if (sel == null || !sel.contains("—")) return "";
        return sel.substring(sel.lastIndexOf("—") + 1).trim();
    }

    public void limpiarFormulario() {
        txtDescripcion.setText("");
        txtReportadoPor.setText("");
        if (cbElemento.getItemCount() > 0) cbElemento.setSelectedIndex(0);
        if (cbSeveridad.getItemCount() > 0) cbSeveridad.setSelectedIndex(0);
        cargarElementosEnCombo();
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JDialog getDialogo()               { return dialogo; }
    public CampoTextoModerno getTxtBuscar()   { return txtBuscar; }

    public JComboBox<String> getCbElemento()  { return cbElemento; }
    public CampoTextoModerno getTxtDescripcion() { return txtDescripcion; }
    public JComboBox<String> getCbSeveridad() { return cbSeveridad; }
    public CampoTextoModerno getTxtReportadoPor() { return txtReportadoPor; }

    // Getter legacy para el controlador
    public CampoTextoModerno getTxtCodigoItem() { return null; }
    public CampoTextoModerno getTxtFechaEnvio() { return null; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }
}
