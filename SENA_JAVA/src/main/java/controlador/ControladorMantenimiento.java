package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import modelo.Mantenimiento;
import vista.VistaMantenimiento;

public class ControladorMantenimiento implements ActionListener, KeyListener {
    
    private VistaMantenimiento vista;
    private Mantenimiento modelo;
    
    public ControladorMantenimiento(VistaMantenimiento vista, Mantenimiento modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Mantenimiento.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla mantenimientos: " + e.getMessage());
        }
        
        cargarDatosTabla("");
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<java.util.List<Mantenimiento>, Void>() {
            @Override
            protected java.util.List<Mantenimiento> doInBackground() {
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
                    java.util.List<Mantenimiento> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Mantenimiento item : lista) {
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
            String codigo = vista.getTxtCodigoItem().getText();
            String descripcion = vista.getTxtDescripcion().getText();
            String fecha = vista.getTxtFechaEnvio().getText();
            
            if (codigo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setCodigoItem(codigo);
            modelo.setDescripcion(descripcion);
            modelo.setFechaEnvio(fecha);
            modelo.setEstado("En progreso");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Envio a mantenimiento registrado.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar mantenimiento.");
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

    private Object[] obtenerFila(Mantenimiento item) {
        return new Object[]{
                    item.getId(),
                    item.getCodigoItem(),
                    item.getDescripcion(),
                    item.getFechaEnvio(),
                    item.getEstado()
                };
    }
}
