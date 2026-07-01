package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoUsuario;
    
    // Modal
    private JDialog dialogo;
    private CampoTextoModerno txtNombre;
    private CampoTextoModerno txtDocumento;
    private JComboBox<String> cbTipo;
    private CampoTextoModerno txtEmail;
    private CampoTextoModerno txtCelular;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaUsuarios() {
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

        JLabel titulo = new JLabel("Gestión de Usuarios");
        titulo.setForeground(SenaColores.TEXTO_PRINCIPAL);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Administra instructores, aprendices y personal administrativo");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        txtBuscar = new CampoTextoModerno("Buscar usuario...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        btnNuevoUsuario = new BotonPlano("+ Nuevo Usuario");

        acciones.add(txtBuscar);
        acciones.add(btnNuevoUsuario);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnas = {"ID", "Nombre", "Documento", "Tipo", "Email", "Celular", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        
        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Usuario", true);
        dialogo.setSize(450, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Registrar Nuevo Usuario");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        txtNombre = new CampoTextoModerno("Nombre Completo");
        txtDocumento = new CampoTextoModerno("Número de Documento");
        
        cbTipo = new JComboBox<>(new String[]{"Aprendiz", "Instructor", "Administrativo"});
        cbTipo.setBackground(new Color(30, 41, 59));
        cbTipo.setForeground(Color.WHITE);
        
        txtEmail = new CampoTextoModerno("Correo Electrónico");
        txtCelular = new CampoTextoModerno("Teléfono/Celular");
        
        form.add(txtNombre);
        form.add(txtDocumento);
        form.add(cbTipo);
        form.add(txtEmail);
        form.add(txtCelular);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");
        
        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoUsuario.addActionListener(e -> {
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
        txtNombre.setText("");
        txtDocumento.setText("");
        cbTipo.setSelectedIndex(0);
        txtEmail.setText("");
        txtCelular.setText("");
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JDialog getDialogo() { return dialogo; }
    
    public CampoTextoModerno getTxtNombre() { return txtNombre; }
    public CampoTextoModerno getTxtDocumento() { return txtDocumento; }
    public JComboBox<String> getCbTipo() { return cbTipo; }
    public CampoTextoModerno getTxtEmail() { return txtEmail; }
    public CampoTextoModerno getTxtCelular() { return txtCelular; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }

    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
}
