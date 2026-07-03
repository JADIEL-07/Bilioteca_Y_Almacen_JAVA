package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VistaPrestamos extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoPrestamo;

    // Modal
    private JDialog dialogo;
    private CampoTextoModerno txtDocumentoUsuario;
    private CampoTextoModerno txtCodigoItem;
    private JLabel lblDisponibilidad; // Mensaje en vivo
    private CampoTextoModerno txtFechaPrestamo;
    private CampoTextoModerno txtFechaDevolucion;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    // Callbacks para validación en vivo
    private Runnable onCodigoChanged;

    // Modal de EDICIÓN DE ESTADO
    private JDialog dialogoEditar;
    private JLabel lblEditInfo;
    private JComboBox<String> cbEditEstado;
    private CampoTextoModerno txtEditFechaDevolucion;
    private BotonPlano btnEditGuardar;
    private BotonPlano btnEditCancelar;
    private int idFilaSeleccionada = -1;
    private Runnable onEditarFila;

    public VistaPrestamos() {
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

        txtBuscar = new CampoTextoModerno("Buscar préstamo...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        btnNuevoPrestamo = new BotonPlano("+ Nuevo Préstamo");

        acciones.add(txtBuscar);
        acciones.add(btnNuevoPrestamo);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Doc. Usuario", "Cód. Elemento", "Fecha Préstamo", "Fecha Devolución", "Estado"};
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

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Préstamo", true);
        dialogo.setSize(480, 550);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(true);
        dialogo.setBackground(new Color(0, 0, 0, 0));

        PanelRedondeado wrapper = new PanelRedondeado(new Color(15, 23, 42, 240), 20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JLabel lbl = new JLabel("Registrar Préstamo");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        wrapper.add(lbl, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        txtDocumentoUsuario = new CampoTextoModerno("Documento del Usuario");
        txtCodigoItem = new CampoTextoModerno("Código del Elemento");
        lblDisponibilidad = new JLabel(" ");
        lblDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDisponibilidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDisponibilidad.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));

        txtFechaPrestamo = new CampoTextoModerno("Fecha Préstamo (YYYY-MM-DD)");
        txtFechaDevolucion = new CampoTextoModerno("Fecha Devolución (YYYY-MM-DD)");

        Dimension d = new Dimension(Integer.MAX_VALUE, 52);
        txtDocumentoUsuario.setMaximumSize(d);
        txtCodigoItem.setMaximumSize(d);
        txtFechaPrestamo.setMaximumSize(d);
        txtFechaDevolucion.setMaximumSize(d);

        txtCodigoItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDocumentoUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtFechaPrestamo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtFechaDevolucion.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(txtDocumentoUsuario);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(txtCodigoItem);
        form.add(lblDisponibilidad);
        form.add(txtFechaPrestamo);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(txtFechaDevolucion);

        wrapper.add(form, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");

        btnCancelar.addActionListener(e -> animarCierre());
        btnNuevoPrestamo.addActionListener(e -> {
            limpiarFormulario();
            LocalDate hoy = LocalDate.now();
            txtFechaPrestamo.setText(hoy.format(DateTimeFormatter.ISO_LOCAL_DATE));
            txtFechaDevolucion.setText(hoy.plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE));
            animarApertura();
        });

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);

        // DocumentListener para validación en vivo
        txtCodigoItem.getCampo().getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { triggerValidation(); }
            @Override public void removeUpdate(DocumentEvent e) { triggerValidation(); }
            @Override public void changedUpdate(DocumentEvent e) { triggerValidation(); }
        });

        dialogo.add(wrapper);
    }
    
    private void triggerValidation() {
        if (onCodigoChanged != null) {
            onCodigoChanged.run();
        }
    }

    public void setOnCodigoChanged(Runnable runnable) {
        this.onCodigoChanged = runnable;
    }

    public void mostrarDisponibilidad(boolean existe, int disponibles, String fechaLibre) {
        if (!existe) {
            lblDisponibilidad.setText("Ítem no encontrado");
            lblDisponibilidad.setForeground(new Color(239, 68, 68)); // Rojo
            btnGuardar.setEnabled(false);
            return;
        }
        if (disponibles > 0) {
            lblDisponibilidad.setText("✅ " + disponibles + " disponibles en stock");
            lblDisponibilidad.setForeground(new Color(34, 197, 94)); // Verde
            btnGuardar.setEnabled(true);
        } else {
            String msg = "❌ Sin stock disponible.";
            if (fechaLibre != null && !fechaLibre.isEmpty()) {
                msg += " Fecha libre estimada: " + fechaLibre;
            }
            lblDisponibilidad.setText(msg);
            lblDisponibilidad.setForeground(new Color(239, 68, 68)); // Rojo
            btnGuardar.setEnabled(false);
        }
    }

    public void ocultarDisponibilidad() {
        lblDisponibilidad.setText(" ");
        btnGuardar.setEnabled(true); // default
    }

    // Animaciones
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

    public void limpiarFormulario() {
        txtDocumentoUsuario.setText("");
        txtCodigoItem.setText("");
        txtFechaPrestamo.setText("");
        txtFechaDevolucion.setText("");
        ocultarDisponibilidad();
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JDialog getDialogo() { return dialogo; }

    public CampoTextoModerno getTxtDocumentoUsuario() { return txtDocumentoUsuario; }
    public CampoTextoModerno getTxtCodigoItem() { return txtCodigoItem; }
    public CampoTextoModerno getTxtFechaPrestamo() { return txtFechaPrestamo; }
    public CampoTextoModerno getTxtFechaDevolucion() { return txtFechaDevolucion; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }
    
    public void cerrarDialogo() {
        animarCierre();
    }

    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
    public JTable getTabla() { return tabla; }

    // ── Modal de EDICIÓN DE ESTADO ─────────────────────────────────

    private void crearDialogoEditar() {
        dialogoEditar = new JDialog((Frame) null, "Editar Préstamo", true);
        dialogoEditar.setSize(420, 360);
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
        JLabel titulo = new JLabel("Actualizar Estado del Préstamo");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
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

        cbEditEstado = crearComboEstilo(new String[]{"ACTIVE", "RETURNED", "OVERDUE", "CANCELLED"});
        cbEditEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cbEditEstado.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEditFechaDevolucion = new CampoTextoModerno("Fecha Devolución (YYYY-MM-DD)");
        txtEditFechaDevolucion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        txtEditFechaDevolucion.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(crearLabelForm("Estado"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbEditEstado);
        form.add(Box.createRigidArea(new Dimension(0, 14)));
        form.add(crearLabelForm("Fecha Devolución"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtEditFechaDevolucion);
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

    private JLabel crearLabelForm(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(SenaColores.TEXTO_SECUNDARIO);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JComboBox<String> crearComboEstilo(String[] opts) {
        JComboBox<String> cb = new JComboBox<>(opts);
        cb.setBackground(new Color(30, 41, 59));
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value,
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

    public void abrirEditorFila(int id, String info, String estadoActual, String fechaDev) {
        idFilaSeleccionada = id;
        lblEditInfo.setText(info);
        txtEditFechaDevolucion.setText(fechaDev);
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

    public void cerrarDialogoEditar() { animarCierreEditar(); }
    public int getIdFilaSeleccionada()            { return idFilaSeleccionada; }
    public JComboBox<String> getCbEditEstado()    { return cbEditEstado; }
    public CampoTextoModerno getTxtEditFechaDevolucion() { return txtEditFechaDevolucion; }
    public void setOnEditarFila(Runnable r)       { this.onEditarFila = r; }
    public void setControladorEditar(ActionListener c) {
        btnEditGuardar.setActionCommand("EditarGuardar");
        btnEditGuardar.addActionListener(c);
    }
}
