package controlador;

import javax.swing.SwingWorker;
import modelo.DashboardDAO;
import modelo.EstadisticasDashboard;
import vista.Dashboard;

/**
 * Controlador del Dashboard con actualización asíncrona (SwingWorker)
 * y auto-refresh cada 30 segundos para mostrar datos en vivo.
 */
public class ControladorDashboard {

    private Dashboard vista;
    private DashboardDAO modelo;
    private javax.swing.Timer autoRefreshTimer;

    public ControladorDashboard(Dashboard vista, DashboardDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;
    }

    public void iniciar() {
        // Carga inicial asíncrona — no bloquea la UI
        cargarAsync();

        // Auto-refresh cada 30 segundos
        autoRefreshTimer = new javax.swing.Timer(30_000, e -> cargarAsync());
        autoRefreshTimer.start();
    }

    /** Carga datos en un hilo de fondo y actualiza la UI en el EDT. */
    public void cargarAsync() {
        new SwingWorker<EstadisticasDashboard, Void>() {
            @Override
            protected EstadisticasDashboard doInBackground() {
                return modelo.obtenerEstadisticas();
            }

            @Override
            protected void done() {
                try {
                    EstadisticasDashboard stats = get();
                    vista.actualizarMetricas(stats);
                } catch (Exception e) {
                    System.err.println("Error al actualizar dashboard: " + e.getMessage());
                }
            }
        }.execute();
    }

    public void detener() {
        if (autoRefreshTimer != null) autoRefreshTimer.stop();
    }
}
