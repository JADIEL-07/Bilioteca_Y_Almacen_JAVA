package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VistaTickets extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;
    private BotonPlano btnNuevoTicket;
    
    // Modal
    private JDialog dialogo;
    private CampoTextoModerno txtDocumentoUsuario;
    private CampoTextoModerno txtAsunto;
    private CampoTextoModerno txtDescripcion;
    private CampoTextoModerno txtFechaCreacion;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaTickets() {
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
        JLabel t = new JLabel("Tickets de Soporte");
        t.setForeground(SenaColores.TEXTO_PRINCIPAL);
        t.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel s = new JLabel("Gestiona incidencias y solicitudes de los usuarios");
        s.setForeground(SenaColores.TEXTO_SECUNDARIO);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tp.add(t);
        tp.add(Box.createRigidArea(new Dimension(0, 4)));
        tp.add(s);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        
        txtBuscar = new CampoTextoModerno("Buscar ticket...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));
        
        btnNuevoTicket = new BotonPlano("+ Nuevo Ticket");
        
        acciones.add(txtBuscar);
        acciones.add(btnNuevoTicket);

        header.add(tp, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Doc. Usuario", "Asunto", "Descripción", "Fecha Creación", "Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) { 
            public boolean isCellEditable(int r, int c) { return false; } 
        };
        tabla = new JTable(modeloTabla);

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Crear Ticket", true);
        dialogo.setSize(450, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Nueva Solicitud/Incidencia");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        txtDocumentoUsuario = new CampoTextoModerno("Documento del Usuario");
        txtAsunto = new CampoTextoModerno("Asunto principal");
        txtDescripcion = new CampoTextoModerno("Descripción detallada");
        txtFechaCreacion = new CampoTextoModerno("Fecha (YYYY-MM-DD)");
        
        form.add(txtDocumentoUsuario);
        form.add(txtAsunto);
        form.add(txtDescripcion);
        form.add(txtFechaCreacion);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");
        
        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoTicket.addActionListener(e -> {
            limpiarFormulario();
            txtFechaCreacion.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        });
        
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        form.add(botones);

        dialogo.add(form);
    }
    
    public void limpiarFormulario() {
        txtDocumentoUsuario.setText("");
        txtAsunto.setText("");
        txtDescripcion.setText("");
        txtFechaCreacion.setText("");
    }

    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JDialog getDialogo() { return dialogo; }
    
    public CampoTextoModerno getTxtDocumentoUsuario() { return txtDocumentoUsuario; }
    public CampoTextoModerno getTxtAsunto() { return txtAsunto; }
    public CampoTextoModerno getTxtDescripcion() { return txtDescripcion; }
    public CampoTextoModerno getTxtFechaCreacion() { return txtFechaCreacion; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }
}
