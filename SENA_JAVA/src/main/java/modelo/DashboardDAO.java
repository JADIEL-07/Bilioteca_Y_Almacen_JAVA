package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    public EstadisticasDashboard obtenerEstadisticas() {
        EstadisticasDashboard stats = new EstadisticasDashboard();
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();

            // 1. Total de elementos
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM items WHERE (is_deleted = false OR is_deleted IS NULL)");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setTotalElementos(rs.getInt(1));
            }

            // 2. Disponibles
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM items i JOIN statuses s ON i.status_id = s.id " +
                    "WHERE (i.is_deleted = false OR i.is_deleted IS NULL) AND s.name ILIKE '%available%'");
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
                    "SELECT COUNT(*) FROM maintenance WHERE (is_deleted = false OR is_deleted IS NULL) AND status NOT IN ('COMPLETED','CANCELLED')");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setMantenimiento(rs.getInt(1));
            }

            // 5. Reservas activas
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM reservations WHERE (is_deleted = false OR is_deleted IS NULL) AND status NOT IN ('COMPLETED','CANCELLED','EXPIRED')");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.setReservasActivas(rs.getInt(1));
            }

            // 6. Préstamos por mes (últimos 12 meses)
            List<String> prestamosPorMes = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT to_char(DATE_TRUNC('month', loan_date), 'YYYY-MM') AS mes, COUNT(*) AS total " +
                    "FROM loans GROUP BY mes ORDER BY mes DESC LIMIT 12");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prestamosPorMes.add(rs.getString("mes") + ": " + rs.getInt("total"));
                }
            }
            stats.setPrestamosPorMes(prestamosPorMes);

            // 7. Próximas devoluciones (préstamos activos con fecha de vencimiento)
            List<String> proximasDevoluciones = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT l.due_date, i.code FROM loans l " +
                    "LEFT JOIN loan_details ld ON l.id = ld.loan_id " +
                    "LEFT JOIN items i ON ld.item_id = i.id " +
                    "WHERE l.due_date >= CURRENT_DATE AND l.status NOT IN ('RETURNED','DELETED','CANCELLED') " +
                    "ORDER BY l.due_date ASC LIMIT 5");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String itemCode = rs.getString("code") != null ? rs.getString("code") : "N/A";
                    String dueDate = rs.getDate("due_date") != null ? rs.getDate("due_date").toString() : "?";
                    proximasDevoluciones.add(itemCode + " - Vence: " + dueDate);
                }
            }
            stats.setProximasDevoluciones(proximasDevoluciones);

            // 8. Actividad reciente (últimos 10 registros de auditoría)
            List<String> actividadReciente = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT user_id, action, entity, entity_name, created_at FROM audit_logs " +
                    "ORDER BY created_at DESC LIMIT 10");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String user = rs.getString("user_id") != null ? rs.getString("user_id") : "?";
                    String action = rs.getString("action") != null ? rs.getString("action") : "?";
                    String entity = rs.getString("entity") != null ? rs.getString("entity") : "";
                    String entityName = rs.getString("entity_name") != null ? rs.getString("entity_name") : "";
                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    String date = ts != null ? ts.toLocalDateTime().format(
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm")) : "";
                    actividadReciente.add(action + " " + entity + " - " + entityName + " (" + user + ") " + date);
                }
            }
            stats.setActividadReciente(actividadReciente);

        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        } finally {
            ConexionBD.getInstance().releaseConnection(con);
        }
        return stats;
    }
}
