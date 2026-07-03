package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VistaInventario extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoElemento;
    private JLabel lblContador = new JLabel("0 elementos");

    // Modal de NUEVO elemento
    private JDialog dialogo;
    private CampoTextoModerno txtNombre;
    private CampoTextoModerno txtCodigo;
    private JComboBox<String> cbCategoria;
    private CampoTextoModerno txtCantidad;
    private JComboBox<String> cbUbicacion;
    private JComboBox<String> cbEstado;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    // Modal de EDICIÓN (clic en fila)
    private JDialog dialogoEditar;
    private JLabel lblEditNombre;
    private JLabel lblEditCodigo;
    private CampoTextoModerno txtEditCantidad;
    private JComboBox<String> cbEditUbicacion;
    private JComboBox<String> cbEditEstado;
    private BotonPlano btnEditGuardar;
    private BotonPlano btnEditCancelar;
    private int idItemSeleccionado = -1;
    private Runnable onEditarItem;

    public VistaInventario() {
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

        String[] columnas = {"ID", "Nombre", "Código", "Categoría", "Stock Total", "Disponibles", "Ubicación", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        
        // Custom renderer para la columna Disponibles (índice 5)
        DefaultTableCellRenderer rendererDisponibles = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    try {
                        int disp = Integer.parseInt(value.toString());
                        if (disp > 0) {
                            c.setForeground(new Color(34, 197, 94)); // Verde
                        } else {
                            c.setForeground(new Color(239, 68, 68)); // Rojo
                        }
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } catch (NumberFormatException e) {
                        c.setForeground(table.getForeground());
                    }
                }
                if (isSelected) {
                    c.setForeground(Color.WHITE); // Mantener blanco si está seleccionado
                }
                return c;
            }
        };
        
        JScrollPane scroll = TablaModerna.crear(tabla);
        tabla.getColumnModel().getColumn(5).setCellRenderer(rendererDisponibles);
        
        body.add(scroll, BorderLayout.CENTER);

        // Hint de doble clic
        JLabel hint = new JLabel("  Doble clic en una fila para editar el stock");
        hint.setForeground(new Color(100, 116, 139));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setBorder(BorderFactory.createEmptyBorder(4, 6, 2, 0));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(lblContador, BorderLayout.WEST);
        footer.add(hint, BorderLayout.EAST);

        lblContador.setForeground(new Color(148, 163, 184));
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContador.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
        body.add(footer, BorderLayout.SOUTH);

        // Doble clic abre el modal de edición
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && tabla.getSelectedRow() >= 0) {
                    if (onEditarItem != null) onEditarItem.run();
                }
            }
        });

        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Elemento de Inventario", true);
        dialogo.setSize(480, 600);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(true); // Para hacer animación y borde personalizado
        dialogo.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente

        // Panel principal con bordes redondeados (Glassmorphism look)
        PanelRedondeado wrapper = new PanelRedondeado(new Color(15, 23, 42, 240), 20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

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
        txtCantidad = new CampoTextoModerno("Cantidad (Stock Total)");

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

        btnCancelar.addActionListener(e -> animarCierre());
        btnNuevoElemento.addActionListener(e -> {
            limpiarFormulario();
            animarApertura();
        });

        botones.add(btnCancelar);
        botones.add(btnGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);

        dialogo.add(wrapper);
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

    // ── Modal de EDICIÓN ───────────────────────────────────────────

    private void crearDialogoEditar() {
        dialogoEditar = new JDialog((Frame) null, "Editar Elemento", true);
        dialogoEditar.setSize(440, 440);
        dialogoEditar.setLocationRelativeTo(this);
        dialogoEditar.setUndecorated(true);
        dialogoEditar.setBackground(new Color(0, 0, 0, 0));

        PanelRedondeado wrapper = new PanelRedondeado(new Color(15, 23, 42, 245), 20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SenaColores.VERDE_SENA, 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        // Encabezado
        JPanel encHead = new JPanel();
        encHead.setLayout(new BoxLayout(encHead, BoxLayout.Y_AXIS));
        encHead.setOpaque(false);
        encHead.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel titulo = new JLabel("Editar Elemento");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel sub = new JLabel("Modifica el stock, ubicación o estado del elemento");
        sub.setForeground(SenaColores.TEXTO_SECUNDARIO);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        encHead.add(titulo);
        encHead.add(Box.createRigidArea(new Dimension(0, 4)));
        encHead.add(sub);
        wrapper.add(encHead, BorderLayout.NORTH);

        // Cuerpo del formulario
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);

        Dimension labelSize = new Dimension(Integer.MAX_VALUE, 20);
        Dimension campoSize = new Dimension(Integer.MAX_VALUE, 52);
        Dimension comboSize = new Dimension(Integer.MAX_VALUE, 42);

        // Nombre (solo lectura, informativo)
        lblEditNombre = new JLabel("—");
        lblEditNombre.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lblEditNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblEditNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEditNombre.setMaximumSize(labelSize);

        lblEditCodigo = new JLabel("—");
        lblEditCodigo.setForeground(SenaColores.VERDE_SENA);
        lblEditCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEditCodigo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblEditCodigo.setMaximumSize(labelSize);

        // Separador visual
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 25));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Stock total editable
        txtEditCantidad = new CampoTextoModerno("Nuevo stock total");
        txtEditCantidad.setMaximumSize(campoSize);
        txtEditCantidad.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ubicación
        cbEditUbicacion = crearCombo(new String[]{"Pasillo A", "Pasillo B", "Pasillo C", "Estante Principal", "Bodega"});
        cbEditUbicacion.setMaximumSize(comboSize);
        cbEditUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Estado
        cbEditEstado = crearCombo(new String[]{"Disponible", "No Disponible", "En Mantenimiento", "Prestado"});
        cbEditEstado.setMaximumSize(comboSize);
        cbEditEstado.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(lblEditNombre);
        form.add(Box.createRigidArea(new Dimension(0, 2)));
        form.add(lblEditCodigo);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(sep);
        form.add(Box.createRigidArea(new Dimension(0, 14)));
        form.add(crearEtiquetaForm("Stock Total"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(txtEditCantidad);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(crearEtiquetaForm("Ubicación"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbEditUbicacion);
        form.add(Box.createRigidArea(new Dimension(0, 12)));
        form.add(crearEtiquetaForm("Estado"));
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(cbEditEstado);

        wrapper.add(form, BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        btnEditCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnEditGuardar  = new BotonPlano("Guardar Cambios");

        btnEditCancelar.addActionListener(e -> animarCierreEditar());

        botones.add(btnEditCancelar);
        botones.add(btnEditGuardar);
        wrapper.add(botones, BorderLayout.SOUTH);

        dialogoEditar.add(wrapper);
    }

    public void abrirEditorFila(int id, String nombre, String codigo,
                                String stockActual, String ubicacion, String estado) {
        idItemSeleccionado = id;
        lblEditNombre.setText(nombre);
        lblEditCodigo.setText("Código: " + codigo);
        txtEditCantidad.setText(stockActual);

        // Seleccionar ubicacion en combo
        for (int i = 0; i < cbEditUbicacion.getItemCount(); i++) {
            if (cbEditUbicacion.getItemAt(i).equalsIgnoreCase(ubicacion)) {
                cbEditUbicacion.setSelectedIndex(i);
                break;
            }
        }

        // Seleccionar estado en combo
        for (int i = 0; i < cbEditEstado.getItemCount(); i++) {
            if (cbEditEstado.getItemAt(i).equalsIgnoreCase(estado)) {
                cbEditEstado.setSelectedIndex(i);
                break;
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
            float opacity = 0.0f;
            int yOffset = -30;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity += 0.1f;
                yOffset += 3;
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
            float opacity = 1.0f;
            int yOffset = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                opacity -= 0.1f;
                yOffset -= 3;
                if (opacity <= 0.0f) { timer.stop(); dialogoEditar.setVisible(false); }
                else { dialogoEditar.setOpacity(opacity); dialogoEditar.setLocation(p.x, p.y + yOffset); }
            }
        });
        timer.start();
    }

    public void cerrarDialogoEditar() { animarCierreEditar(); }

    public int getIdItemSeleccionado()         { return idItemSeleccionado; }
    public CampoTextoModerno getTxtEditCantidad() { return txtEditCantidad; }
    public JComboBox<String> getCbEditUbicacion() { return cbEditUbicacion; }
    public JComboBox<String> getCbEditEstado()    { return cbEditEstado; }

    public void setControladorEditar(ActionListener c) {
        btnEditGuardar.setActionCommand("EditarGuardar");
        btnEditGuardar.addActionListener(c);
    }

    public void setOnEditarItem(Runnable r) { this.onEditarItem = r; }

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
    
    public void cerrarDialogo() {
        animarCierre();
    }
}
