package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public EstadisticasDashboard obtenerEstadisticas() {
        EstadisticasDashboard stats = new EstadisticasDashboard();
        
        // Ensure connection is active
        Connection con = ConexionBD.conexion;
        if (con == null) {
            // Re-instantiate if null
            con = ConexionBD.getInstance().conexion;
        }

        if (con != null) {
            try {
                // 1. Total de elementos
                String sqlTotal = "SELECT COUNT(*) FROM items";
                try (PreparedStatement ps = con.prepareStatement(sqlTotal);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.setTotalElementos(rs.getInt(1));
                }

                // 2. Disponibles
                String sqlDisp = "SELECT COUNT(*) FROM items WHERE estado = 'Disponible'";
                try (PreparedStatement ps = con.prepareStatement(sqlDisp);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.setDisponibles(rs.getInt(1));
                }

                // 3. Prestados
                String sqlPrestados = "SELECT COUNT(*) FROM prestamos WHERE estado = 'Activo'";
                try (PreparedStatement ps = con.prepareStatement(sqlPrestados);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.setPrestados(rs.getInt(1));
                }

                // 4. Mantenimiento
                String sqlMant = "SELECT COUNT(*) FROM mantenimientos WHERE estado = 'En proceso'";
                try (PreparedStatement ps = con.prepareStatement(sqlMant);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.setMantenimiento(rs.getInt(1));
                }

                // 5. Reservas activas
                String sqlRes = "SELECT COUNT(*) FROM reservas WHERE estado = 'Pendiente'";
                try (PreparedStatement ps = con.prepareStatement(sqlRes);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) stats.setReservasActivas(rs.getInt(1));
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener estadísticas del dashboard: " + e.getMessage());
                // En caso de que las tablas no existan aún, se manejará silenciosamente para no romper la UI
            }
        } else {
            System.err.println("No hay conexión a la base de datos.");
        }

        return stats;
    }
}
