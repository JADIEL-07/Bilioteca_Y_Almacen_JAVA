package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public EstadisticasDashboard obtenerEstadisticas() {
        EstadisticasDashboard stats = new EstadisticasDashboard();
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();

            // 1. Total de elementos
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM items WHERE is_deleted = false");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setTotalElementos(rs.getInt(1));
            }

            // 2. Disponibles (status_id = 1 normalmente representa "Available")
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM items i JOIN statuses s ON i.status_id = s.id " +
                    "WHERE i.is_deleted = false AND s.name ILIKE '%available%'");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setDisponibles(rs.getInt(1));
            }

            // 3. Prestados (loans activos)
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM loans WHERE status NOT IN ('RETURNED','DELETED','CANCELLED')");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setPrestados(rs.getInt(1));
            }

            // 4. En mantenimiento
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM maintenance WHERE is_deleted = false AND status NOT IN ('COMPLETED','CANCELLED')");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setMantenimiento(rs.getInt(1));
            }

            // 5. Reservas activas
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE is_deleted = false AND status NOT IN ('COMPLETED','CANCELLED','EXPIRED')");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setReservasActivas(rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        } finally {
            ConexionBD.getInstance().releaseConnection(con);
        }
        return stats;
    }
}
