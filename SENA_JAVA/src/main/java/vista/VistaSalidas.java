package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

public class VistaSalidas extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevaSalida;
    
    // Modal
    private JDialog dialogo;
    private CampoTextoModerno txtArticulo;
    private CampoTextoModerno txtCantidad;
    private CampoTextoModerno txtMotivo;
    private CampoTextoModerno txtResponsable;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaSalidas() {
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

        JLabel titulo = new JLabel("Control de Salidas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Registro de materiales consumidos o dados de baja");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        txtBuscar = new CampoTextoModerno("Buscar salidas...", 0);
        txtBuscar.setPreferredSize(new Dimension(250, 40));

        btnNuevaSalida = new BotonPlano("+ Registrar Salida");
        btnNuevaSalida.setPreferredSize(new Dimension(160, 40));

        acciones.add(txtBuscar);
        acciones.add(btnNuevaSalida);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        String[] columnas = {"ID", "ARTÍCULO", "CANTIDAD", "FECHA", "MOTIVO", "RESPONSABLE"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Registrar Salida", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 15));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Registrar Nueva Salida");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        txtArticulo = new CampoTextoModerno("Nombre del Artículo", 0);
        txtCantidad = new CampoTextoModerno("Cantidad", 0);
        txtMotivo = new CampoTextoModerno("Motivo (Ej. Consumo, Daño)", 0);
        txtResponsable = new CampoTextoModerno("Responsable / Instructor", 1);
        
        form.add(txtArticulo);
        form.add(txtCantidad);
        form.add(txtMotivo);
        form.add(txtResponsable);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");
        
        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevaSalida.addActionListener(e -> {
            limpiarFormulario();
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        });
        
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        form.add(botones);

        dialogo.add(form);
    }
    
    public void limpiarFormulario() {
        txtArticulo.setText("");
        txtCantidad.setText("");
        txtMotivo.setText("");
        txtResponsable.setText("");
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JDialog getDialogo() { return dialogo; }
    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
    
    public CampoTextoModerno getTxtArticulo() { return txtArticulo; }
    public CampoTextoModerno getTxtCantidad() { return txtCantidad; }
    public CampoTextoModerno getTxtMotivo() { return txtMotivo; }
    public CampoTextoModerno getTxtResponsable() { return txtResponsable; }

    public void setControlador(ActionListener c, KeyListener k) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
        if (txtBuscar != null) txtBuscar.addKeyListener(k);
    }
}
