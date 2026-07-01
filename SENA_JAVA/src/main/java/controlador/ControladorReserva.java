package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import modelo.AuditLog;
import modelo.Reserva;
import vista.VistaReservas;

public class ControladorReserva implements ActionListener, KeyListener {
    
    private VistaReservas vista;
    private Reserva modelo;
    private Timer autoRefresh;
    
    public ControladorReserva(VistaReservas vista, Reserva modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Reserva.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla reservas: " + e.getMessage());
        }
        
        cargarDatosTabla("");
        autoRefresh = new Timer(30000, e -> cargarDatosTabla(vista.getTxtBuscar().getText()));
        autoRefresh.start();
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<java.util.List<Reserva>, Void>() {
            @Override
            protected java.util.List<Reserva> doInBackground() {
                try {
                    if (filtro == null || filtro.trim().isEmpty()) {
                        return modelo.listar();
                    } else {
                        return modelo.buscar(filtro.trim());
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar datos: " + e.getMessage());
                    return new java.util.ArrayList<>();
                }
            }

            @Override
            protected void done() {
                try {
                    java.util.List<Reserva> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Reserva item : lista) {
                            tablaModelo.addRow(obtenerFila(item));
                        }
                    } else {
                        // No data found, table remains empty
                    }
                } catch (Exception e) {
                    System.err.println("Error al actualizar tabla: " + e.getMessage());
                }
            }
        }.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accion = e.getActionCommand();
        
        if (accion.equals("Guardar")) {
            String documento = vista.getTxtDocumentoUsuario().getText();
            String codigo = vista.getTxtCodigoItem().getText();
            String fecha = vista.getTxtFechaReserva().getText();
            
            if (documento.isEmpty() || codigo.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setCodigoItem(codigo);
            modelo.setFechaReserva(fecha);
            modelo.setEstado("Pendiente");
            
            if (modelo.insertar()) {
                AuditLog.registrar("admin", "CREATE", "Reservas", "Item: " + codigo + " / Usuario: " + documento);
                JOptionPane.showMessageDialog(vista, "Reserva registrada con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar reserva.");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String texto = vista.getTxtBuscar().getText();
            cargarDatosTabla(texto);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private Object[] obtenerFila(Reserva item) {
        return new Object[]{
                    item.getId(),
                    item.getDocumentoUsuario(),
                    item.getCodigoItem(),
                    item.getFechaReserva(),
                    item.getEstado()
                };
    }
}
