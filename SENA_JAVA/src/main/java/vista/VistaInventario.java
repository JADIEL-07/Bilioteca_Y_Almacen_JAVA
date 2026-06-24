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
    
    // Elementos del Modal
    private JDialog dialogo;
    private CampoTextoModerno txtNombre;
    private CampoTextoModerno txtCodigo;
    private CampoTextoModerno txtCategoria;
    private CampoTextoModerno txtCantidad;
    private CampoTextoModerno txtUbicacion;
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
        return body;
    }

    private void crearDialogo() {
        dialogo = new JDialog((Frame) null, "Nuevo Elemento", true);
        dialogo.setSize(480, 560);
        dialogo.setLocationRelativeTo(this);
        dialogo.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Nuevo Elemento de Inventario");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        txtNombre = new CampoTextoModerno("Nombre del Elemento");
        txtCodigo = new CampoTextoModerno("Código Interno");
        txtCategoria = new CampoTextoModerno("Categoría (Equipos / Libros / Herramientas / Insumos)");
        txtCantidad = new CampoTextoModerno("Cantidad");
        txtUbicacion = new CampoTextoModerno("Ubicación");
        
        form.add(txtNombre);
        form.add(txtCodigo);
        form.add(txtCategoria);
        form.add(txtCantidad);
        form.add(txtUbicacion);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnGuardar = new BotonPlano("Guardar");
        
        btnCancelar.addActionListener(e -> dialogo.setVisible(false));
        btnNuevoElemento.addActionListener(e -> {
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
        txtCodigo.setText("");
        txtCategoria.setText("");
        txtCantidad.setText("");
        txtUbicacion.setText("");
    }

    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public CampoTextoModerno getTxtBuscar() { return txtBuscar; }
    public JDialog getDialogo() { return dialogo; }
    
    public CampoTextoModerno getTxtNombre() { return txtNombre; }
    public CampoTextoModerno getTxtCodigo() { return txtCodigo; }
    public CampoTextoModerno getTxtCategoria() { return txtCategoria; }
    public CampoTextoModerno getTxtCantidad() { return txtCantidad; }
    public CampoTextoModerno getTxtUbicacion() { return txtUbicacion; }

    public void setControlador(ActionListener c) {
        btnGuardar.setActionCommand("Guardar");
        btnGuardar.addActionListener(c);
    }
}
