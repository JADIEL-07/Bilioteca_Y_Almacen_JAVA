package vista;

import vista.componentes.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VistaUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private CampoTextoModerno txtBuscar;

    public VistaUsuarios() {
        setBackground(SenaColores.FONDO_OSCURO);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
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

        JLabel subtitulo = new JLabel("Administra los usuarios del sistema, roles y permisos");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(subtitulo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);

        txtBuscar = new CampoTextoModerno("Buscar usuario...");
        txtBuscar.setPreferredSize(new Dimension(220, 38));

        BotonPlano btnAgregar = new BotonPlano("+ Nuevo Usuario");
        btnAgregar.addActionListener(e -> mostrarDialogo());

        acciones.add(txtBuscar);
        acciones.add(btnAgregar);

        header.add(tituloPanel, BorderLayout.WEST);
        header.add(acciones, BorderLayout.EAST);
        return header;
    }

    private JPanel crearCuerpo() {
        PanelRedondeado body = new PanelRedondeado(SenaColores.SUPERFICIE, 18);
        body.setLayout(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnas = {"ID", "Nombre Completo", "Documento", "Email", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);

        modeloTabla.addRow(new Object[]{"1", "Carlos Andrés Pérez", "1098765432", "carlos@sena.edu.co", "Admin", "Activo"});
        modeloTabla.addRow(new Object[]{"2", "María Fernanda López", "1087654321", "maria@sena.edu.co", "Bibliotecario", "Activo"});
        modeloTabla.addRow(new Object[]{"3", "Juan David Rodríguez", "1076543210", "juan@sena.edu.co", "Aprendiz", "Activo"});
        modeloTabla.addRow(new Object[]{"4", "Laura Valentina Gómez", "1065432109", "laura@sena.edu.co", "Almacenista", "Inactivo"});
        modeloTabla.addRow(new Object[]{"5", "Andrés Felipe Martínez", "1054321098", "andres@sena.edu.co", "Aprendiz", "Activo"});

        body.add(TablaModerna.crear(tabla), BorderLayout.CENTER);
        return body;
    }

    private void mostrarDialogo() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Usuario", true);
        d.setSize(480, 480);
        d.setLocationRelativeTo(this);
        d.getContentPane().setBackground(SenaColores.FONDO_OSCURO);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 12));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lbl = new JLabel("Nuevo Usuario");
        lbl.setForeground(SenaColores.TEXTO_PRINCIPAL);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        form.add(lbl);

        form.add(new CampoTextoModerno("Nombre Completo"));
        form.add(new CampoTextoModerno("Número de Documento"));
        form.add(new CampoTextoModerno("Correo Electrónico"));
        form.add(new CampoTextoModerno("Rol (Admin / Bibliotecario / Aprendiz)"));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);
        BotonPlano btnCancelar = new BotonPlano("Cancelar", new Color(100, 116, 139), new Color(71, 85, 105));
        btnCancelar.addActionListener(e -> d.dispose());
        BotonPlano btnGuardar = new BotonPlano("Guardar");
        btnGuardar.addActionListener(e -> d.dispose());
        botones.add(btnCancelar);
        botones.add(btnGuardar);
        form.add(botones);

        d.add(form);
        d.setVisible(true);
    }
}
