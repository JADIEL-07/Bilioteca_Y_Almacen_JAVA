package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    // Modal de EDICIÓN DE ESTADO
    private JDialog dialogoEditar;
    private JLabel lblEditInfo;
    private JComboBox<String> cbEditEstado;
    private BotonPlano btnEditGuardar;
    private BotonPlano btnEditCancelar;
    private int idFilaSeleccionada = -1;
    private Runnable onEditarFila;

    public VistaMantenimiento() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
        crearDialogo();
        crearDialogoEditar();
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

        JLabel hint = new JLabel("  Doble clic en una fila para cambiar el estado");
        hint.setForeground(new Color(100, 116, 139));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setBorder(BorderFactory.createEmptyBorder(6, 6, 4, 0));
        body.add(hint, BorderLayout.SOUTH);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() >= 0) {
                    if (onEditarFila != null) onEditarFila.run();
                }
            }
        });

        return body;
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComboBox<String> crearCombo(String[] opciones) {
        JComboBox<String> cb = new JComboBox<>(opciones);
        cb.setBackground(new Color(30, 41, 59));
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        cb.setFocusable(true);
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? SenaColores.VERDE_SENA : new Color(15, 23, 42));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        return cb;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Reportar Falla / Mantenimiento", true);
        dialogo.setSize(480, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(true);
        dialogo.setBackground(new Color(0, 0, 0, 0));

        // Panel glassmorphism redondeado
        PanelRedondeado wrapper = new PanelRedondeado(new Color(15, 23, 42, 240), 20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        // Título con acento de color
        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);
        tituloPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lbl = new JLabel("Reportar Falla en Elemento");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel sub = new JLabel("Completa los campos para registrar el reporte");
        sub.setForeground(SenaColores.TEXTO_SECUNDARIO);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        tituloPanel.add(lbl);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(sub);
        wrapper.add(tituloPanel, BorderLayout.NORTH);

        // Formulario
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        Dimension campoSize = new Dimension(Integer.MAX_VALUE, 52);
        Dimension comboSize = new Dimension(Integer.MAX_VALUE, 42);

        // Elemento del Inventario
        cbElemento = crearCombo(new String[]{"Cargando elementos..."});
        cbElemento.setMaximumSize(comboSize);
        cbElemento.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(crearEtiquetaForm("Elemento del Inventario *"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbElemento);
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        // Descripción
        txtDescripcion = new CampoTextoModerno("Describe la falla o problema detectado");
        txtDescripcion.setMaximumSize(campoSize);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(crearEtiquetaForm("Descripción de la Falla *"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtDescripcion);
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        // Severidad
        cbSeveridad = crearCombo(new String[]{"Baja", "Media", "Alta", "Crítica"});
        cbSeveridad.setMaximumSize(comboSize);
        cbSeveridad.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(crearEtiquetaForm("Severidad *"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbSeveridad);
        form.add(Box.createRigidArea(new Dimension(0, 14)));

        // Reportado por
        txtReportadoPor = new CampoTextoModerno("Nombre o documento de quien reporta");
        txtReportadoPor.setMaximumSize(campoSize);
        txtReportadoPor.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(crearEtiquetaForm("Reportado Por *"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtReportadoPor);

        wrapper.add(form, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar  = new BotonPlano("Reportar Falla");

        btnCancelar.addActionListener(e -> animarCierre());
        btnNuevoMantenimiento.addActionListener(e -> {
            limpiarFormulario();
            animarApertura();
        });

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);

        dialogo.add(wrapper);
    }

    private void animarApertura() {
        dialogo.setOpacity(0.0f);
        dialogo.setLocationRelativeTo(this);
        Point p = dialogo.getLocation();
        dialogo.setLocation(p.x, p.y - 30);

        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0.0f;
            int yOffset = -30;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity += 0.1f;
                yOffset += 3;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    yOffset = 0;
                    timer.stop();
                }
                dialogo.setOpacity(opacity);
                dialogo.setLocation(p.x, p.y + yOffset);
            }
        });
        timer.start();
        dialogo.setVisible(true);
    }

    private void animarCierre() {
        Point p = dialogo.getLocation();
        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;
            int yOffset = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity -= 0.1f;
                yOffset -= 3;
                if (opacity <= 0.0f) {
                    timer.stop();
                    dialogo.setVisible(false);
                } else {
                    dialogo.setOpacity(opacity);
                    dialogo.setLocation(p.x, p.y + yOffset);
                }
            }
        });
        timer.start();
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
    public JTable getTabla()                  { return tabla; }

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

    public void cerrarDialogo() {
        animarCierre();
    }

    // ── Modal de EDICIÓN DE ESTADO ─────────────────────────────────

    private void crearDialogoEditar() {
        dialogoEditar = new JDialog((Frame) null, "Editar Mantenimiento", true);
        dialogoEditar.setSize(400, 300);
        dialogoEditar.setLocationRelativeTo(this);
        dialogoEditar.setUndecorated(true);
        dialogoEditar.setBackground(new Color(0, 0, 0, 0));

        PanelRedondeado wrapper = new PanelRedondeado(new Color(15, 23, 42, 245), 20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SenaColores.VERDE_SENA, 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JPanel head = new JPanel();
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.setOpaque(false);
        head.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        JLabel titulo = new JLabel("Actualizar Estado de Mantenimiento");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEditInfo = new JLabel(" ");
        lblEditInfo.setForeground(SenaColores.VERDE_SENA);
        lblEditInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        head.add(titulo);
        head.add(Box.createRigidArea(new Dimension(0, 4)));
        head.add(lblEditInfo);
        wrapper.add(head, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        cbEditEstado = new JComboBox<>(new String[]{"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"});
        cbEditEstado.setBackground(new Color(30, 41, 59));
        cbEditEstado.setForeground(Color.WHITE);
        cbEditEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbEditEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cbEditEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbEditEstado.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? SenaColores.VERDE_SENA : new Color(15, 23, 42));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        JLabel lblEst = new JLabel("Estado");
        lblEst.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblEst.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEst.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(lblEst);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbEditEstado);
        wrapper.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        btnEditCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnEditGuardar  = new BotonPlano("Guardar");
        btnEditCancelar.addActionListener(e -> animarCierreEditar());
        botones.add(btnEditCancelar);
        botones.add(btnEditGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);
        dialogoEditar.add(wrapper);
    }

    public void abrirEditorFila(int id, String info, String estadoActual) {
        idFilaSeleccionada = id;
        lblEditInfo.setText(info);
        for (int i = 0; i < cbEditEstado.getItemCount(); i++) {
            if (cbEditEstado.getItemAt(i).equalsIgnoreCase(estadoActual)) {
                cbEditEstado.setSelectedIndex(i); break;
            }
        }
        animarAperturaEditar();
    }

    private void animarAperturaEditar() {
        dialogoEditar.setOpacity(0.0f);
        dialogoEditar.setLocationRelativeTo(this);
        Point p = dialogoEditar.getLocation();
        dialogoEditar.setLocation(p.x, p.y - 30);
        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0.0f; int yOffset = -30;
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity += 0.1f; yOffset += 3;
                if (opacity >= 1.0f) { opacity = 1.0f; yOffset = 0; timer.stop(); }
                dialogoEditar.setOpacity(opacity);
                dialogoEditar.setLocation(p.x, p.y + yOffset);
            }
        });
        timer.start();
        dialogoEditar.setVisible(true);
    }

    private void animarCierreEditar() {
        Point p = dialogoEditar.getLocation();
        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f; int yOffset = 0;
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity -= 0.1f; yOffset -= 3;
                if (opacity <= 0.0f) { timer.stop(); dialogoEditar.setVisible(false); }
                else { dialogoEditar.setOpacity(opacity); dialogoEditar.setLocation(p.x, p.y + yOffset); }
            }
        });
        timer.start();
    }

    public void cerrarDialogoEditar()         { animarCierreEditar(); }
    public int getIdFilaSeleccionada()        { return idFilaSeleccionada; }
    public JComboBox<String> getCbEditEstado(){ return cbEditEstado; }
    public void setOnEditarFila(Runnable r)   { this.onEditarFila = r; }
    public void setControladorEditar(ActionListener c) {
        btnEditGuardar.setActionCommand("EditarGuardar");
        btnEditGuardar.addActionListener(c);
    }
}
