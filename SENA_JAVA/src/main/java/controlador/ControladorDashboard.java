package controlador;

import modelo.DashboardDAO;
import modelo.EstadisticasDashboard;
import vista.Dashboard;

public class ControladorDashboard {

    private Dashboard vista;
    private DashboardDAO modelo;

    public ControladorDashboard(Dashboard vista, DashboardDAO modelo) {
        this.vista = vista;
        this.modelo = modelo;
    }

    public void iniciar() {
        // Obtenemos los datos de la base de datos
        EstadisticasDashboard stats = modelo.obtenerEstadisticas();
        
        // Actualizamos la vista con los datos reales
        vista.actualizarMetricas(stats);
    }
}
