package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
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
    private CampoTextoModerno txtFechaPrestamo;
    private CampoTextoModerno txtFechaDevolucion;
    private BotonPlano btnGuardar;
    private BotonPlano btnCancelar;

    public VistaPrestamos() {
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
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Préstamo", true);
        dialogo.setSize(450, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Registrar Préstamo");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        txtDocumentoUsuario = new CampoTextoModerno("Documento del Usuario");
        txtCodigoItem = new CampoTextoModerno("Código del Elemento");
        txtFechaPrestamo = new CampoTextoModerno("Fecha Préstamo (YYYY-MM-DD)");
        txtFechaDevolucion = new CampoTextoModerno("Fecha Devolución (YYYY-MM-DD)");
        
        form.add(txtDocumentoUsuario);
        form.add(txtCodigoItem);
        form.add(txtFechaPrestamo);
        form.add(txtFechaDevolucion);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");
        
        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoPrestamo.addActionListener(e -> {
            limpiarFormulario();
            
            // Sugerir fechas de hoy y dentro de 3 días
            LocalDate hoy = LocalDate.now();
            txtFechaPrestamo.setText(hoy.format(DateTimeFormatter.ISO_LOCAL_DATE));
            txtFechaDevolucion.setText(hoy.plusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE));
            
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
        txtCodigoItem.setText("");
        txtFechaPrestamo.setText("");
        txtFechaDevolucion.setText("");
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

    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
}
