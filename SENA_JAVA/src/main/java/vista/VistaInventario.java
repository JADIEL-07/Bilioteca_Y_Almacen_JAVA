package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaInventario extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoElemento;
    private JLabel lblContador = new JLabel("0 elementos");

    // Elementos del Modal
    private JDialog dialogo;
    private CampoTextoModerno txtNombre;
    private CampoTextoModerno txtCodigo;
    private JComboBox<String> cbCategoria;
    private CampoTextoModerno txtCantidad;
    private JComboBox<String> cbUbicacion;
    private JComboBox<String> cbEstado;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaInventario() {
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

        btnNuevoElemento = new BotonPlano("+ Nuevo Elemento");

        acciones.add(txtBuscar);
        acciones.add(btnNuevoElemento);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnas = {"ID", "Nombre", "Código", "Categoría", "Cantidad", "Ubicación", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);

        lblContador.setForeground(new Color(148, 163, 184));
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContador.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
        body.add(lblContador, BorderLayout.SOUTH);
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Elemento de Inventario", true);
        dialogo.setSize(480, 600);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(SenaColores.FONDO_OSCURO);
        wrapper.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        JLabel lbl = new JLabel("Nuevo Elemento de Inventario");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        wrapper.add(lbl, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        txtNombre   = new CampoTextoModerno("Nombre del Elemento");
        txtCodigo   = new CampoTextoModerno("Código Interno (ej. ISBN-001)");
        txtCantidad = new CampoTextoModerno("Cantidad");

        cbCategoria = crearCombo(new String[]{"Libros", "Equipos", "Herramientas", "Insumos", "Otro"});
        cbUbicacion = crearCombo(new String[]{"Pasillo A", "Pasillo B", "Pasillo C", "Estante Principal", "Bodega"});
        cbEstado    = crearCombo(new String[]{"Disponible", "No Disponible", "En Mantenimiento", "Prestado"});

        Dimension campoSize = new Dimension(Integer.MAX_VALUE, 52);
        Dimension comboSize = new Dimension(Integer.MAX_VALUE, 42);

        txtNombre.setMaximumSize(campoSize);
        txtCodigo.setMaximumSize(campoSize);
        txtCantidad.setMaximumSize(campoSize);
        cbCategoria.setMaximumSize(comboSize);
        cbUbicacion.setMaximumSize(comboSize);
        cbEstado.setMaximumSize(comboSize);

        // Etiqueta para combos
        JLabel lblCat = crearEtiquetaForm("Categoría");
        JLabel lblUbi = crearEtiquetaForm("Ubicación");
        JLabel lblEst = crearEtiquetaForm("Estado");

        form.add(txtNombre);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(txtCodigo);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(lblCat);
        form.add(Box.createRigidArea(new Dimension(0, 4)));
        form.add(cbCategoria);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(txtCantidad);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(lblUbi);
        form.add(Box.createRigidArea(new Dimension(0, 4)));
        form.add(cbUbicacion);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(lblEst);
        form.add(Box.createRigidArea(new Dimension(0, 4)));
        form.add(cbEstado);

        wrapper.add(form, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar  = new BotonPlano("Guardar");

        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoElemento.addActionListener(e -> {
            limpiarFormulario();
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        });

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);

        dialogo.add(wrapper);
    }

    // ── Helpers ────────────────────────────────────────────────

    private JComboBox<String> crearCombo(String[] opciones) {
        JComboBox<String> cb = new JComboBox<>(opciones);
        cb.setBackground(new Color(30, 41, 59));
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 30)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        cb.setFocusable(true);
        // Renderer personalizado para fondo oscuro en el dropdown
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? new Color(0, 180, 90) : new Color(15, 23, 42));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        return cb;
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ── API ────────────────────────────────────────────────────

    public void limpiarFormulario() {
        txtNombre.setText("");
        txtCodigo.setText("");
        txtCantidad.setText("");
        cbCategoria.setSelectedIndex(0);
        cbUbicacion.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
    }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
    public JDialog getDialogo() { return dialogo; }
    public JLabel getLblContador() { return lblContador; }

    public CampoTextoModerno getTxtNombre()    { return txtNombre; }
    public CampoTextoModerno getTxtCodigo()    { return txtCodigo; }
    public JComboBox<String>  getCbCategoria() { return cbCategoria; }
    public CampoTextoModerno getTxtCantidad()  { return txtCantidad; }
    public JComboBox<String>  getCbUbicacion() { return cbUbicacion; }
    public JComboBox<String>  getCbEstado()    { return cbEstado; }

    /** @deprecated usa getCbCategoria() en su lugar */
    public CampoTextoModerno getTxtCategoria() { return null; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }
}
