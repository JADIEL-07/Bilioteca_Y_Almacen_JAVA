package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import modelo.Prestamo;
import vista.VistaPrestamos;

public class ControladorPrestamo implements ActionListener, KeyListener {
    
    private VistaPrestamos vista;
    private Prestamo modelo;
    
    public ControladorPrestamo(VistaPrestamos vista, Prestamo modelo) {
        this.vista = vista;
        this.modelo = modelo;
        
        this.vista.setControlador(this);
        if (this.vista.getTxtBuscar() != null) {
            this.vista.getTxtBuscar().addKeyListener(this);
        }
        
        try {
            Prestamo.inicializarTabla();
        } catch (Exception e) {
            System.err.println("No se pudo inicializar tabla prestamos: " + e.getMessage());
        }
        
        cargarDatosTabla("");
    }
    
    public void cargarDatosTabla(String filtro) {
        DefaultTableModel tablaModelo = vista.getModeloTabla();
        tablaModelo.setRowCount(0);

        new SwingWorker<java.util.List<Prestamo>, Void>() {
            @Override
            protected java.util.List<Prestamo> doInBackground() {
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
                    java.util.List<Prestamo> lista = get();
                    tablaModelo.setRowCount(0);
                    if (lista != null && !lista.isEmpty()) {
                        for (Prestamo item : lista) {
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
            String fechaPrestamo = vista.getTxtFechaPrestamo().getText();
            String fechaDevolucion = vista.getTxtFechaDevolucion().getText();
            
            if (documento.isEmpty() || codigo.isEmpty() || fechaPrestamo.isEmpty() || fechaDevolucion.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Todos los campos son obligatorios.");
                return;
            }
            
            modelo.setDocumentoUsuario(documento);
            modelo.setCodigoItem(codigo);
            modelo.setFechaPrestamo(fechaPrestamo);
            modelo.setFechaDevolucion(fechaDevolucion);
            modelo.setEstado("Activo");
            
            if (modelo.insertar()) {
                JOptionPane.showMessageDialog(vista, "Préstamo registrado con éxito.");
                vista.getDialogo().setVisible(false);
                vista.limpiarFormulario();
                cargarDatosTabla("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar préstamo. Verifique el formato de fecha (YYYY-MM-DD).");
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

    private Object[] obtenerFila(Prestamo item) {
        return new Object[]{
                    item.getId(),
                    item.getDocumentoUsuario(),
                    item.getCodigoItem(),
                    item.getFechaPrestamo(),
                    item.getFechaDevolucion(),
                    item.getEstado()
                };
    }
}
