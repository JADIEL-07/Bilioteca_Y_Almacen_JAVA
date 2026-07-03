package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Mantenimiento {

    private int id;
    private String codigoItem;
    private String descripcion;
    private String fechaEnvio;
    private String estado;
    private String reportadoPor;
    private String severidad;

    public Mantenimiento() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoItem() { return codigoItem; }
    public void setCodigoItem(String codigoItem) { this.codigoItem = codigoItem; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getReportadoPor() { return reportadoPor; }
    public void setReportadoPor(String reportadoPor) { this.reportadoPor = reportadoPor; }

    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }

    public boolean insertar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            // Primero obtenemos el item_id a partir del código
            String sqlItem = "SELECT id FROM items WHERE code = ? AND is_deleted = false LIMIT 1";
            int itemId = -1;
            try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                ps.setString(1, codigoItem);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) itemId = rs.getInt(1);
                }
            }
            if (itemId == -1) {
                System.err.println("No se encontró ítem con código: " + codigoItem);
                return false;
            }

            String sql = "INSERT INTO maintenance (item_id, reported_by, failure_description, severity, status, report_date) " +
                         "VALUES (?, ?, ?, ?, 'PENDING', NOW())";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, itemId);
                ps.setString(2, reportadoPor != null && !reportadoPor.trim().isEmpty()
                    ? reportadoPor.trim() : "admin");
                ps.setString(3, descripcion);
                ps.setString(4, severidad != null ? severidad : "Media");
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    public List<Mantenimiento> listar() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, i.code AS codigo_item, i.name AS nombre_item, " +
                     "m.failure_description, m.severity, m.report_date, m.status " +
                     "FROM maintenance m " +
                     "LEFT JOIN items i ON m.item_id = i.id " +
                     "WHERE (m.is_deleted IS NULL OR m.is_deleted = false) " +
                     "ORDER BY m.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mantenimiento m = new Mantenimiento();
                    m.setId(rs.getInt("id"));
                    String nombre = rs.getString("nombre_item");
                    String codigo = rs.getString("codigo_item");
                    m.setCodigoItem(nombre != null ? nombre + " (" + codigo + ")" : (codigo != null ? codigo : "N/A"));
                    m.setDescripcion(rs.getString("failure_description"));
                    m.setSeveridad(rs.getString("severity"));
                    java.sql.Timestamp ts = rs.getTimestamp("report_date");
                    m.setFechaEnvio(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                    m.setEstado(rs.getString("status"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mantenimientos: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }

    public List<Mantenimiento> buscar(String texto) {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, i.code AS codigo_item, i.name AS nombre_item, " +
                     "m.failure_description, m.severity, m.report_date, m.status " +
                     "FROM maintenance m " +
                     "LEFT JOIN items i ON m.item_id = i.id " +
                     "WHERE (m.is_deleted IS NULL OR m.is_deleted = false) " +
                     "  AND (m.failure_description ILIKE ? OR m.status ILIKE ? OR i.name ILIKE ? OR i.code ILIKE ?) " +
                     "ORDER BY m.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String q = "%" + texto + "%";
                ps.setString(1, q);
                ps.setString(2, q);
                ps.setString(3, q);
                ps.setString(4, q);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Mantenimiento m = new Mantenimiento();
                        m.setId(rs.getInt("id"));
                        String nombre = rs.getString("nombre_item");
                        String codigo = rs.getString("codigo_item");
                        m.setCodigoItem(nombre != null ? nombre + " (" + codigo + ")" : (codigo != null ? codigo : "N/A"));
                        m.setDescripcion(rs.getString("failure_description"));
                        m.setSeveridad(rs.getString("severity"));
                        java.sql.Timestamp ts = rs.getTimestamp("report_date");
                        m.setFechaEnvio(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                        m.setEstado(rs.getString("status"));
                        lista.add(m);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar mantenimientos: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }

    /** Actualiza el estado de un mantenimiento por su ID. */
    public boolean modificarEstado(int mantenimientoId, String nuevoEstado) {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "UPDATE maintenance SET status = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nuevoEstado.toUpperCase());
                ps.setInt(2, mantenimientoId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    public static void inicializarTabla() {
        System.out.println("Tabla 'maintenance' remota ya verificada.");
    }
}
