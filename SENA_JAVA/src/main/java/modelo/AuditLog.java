package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuditLog {
    
    private int id;
    private String usuario;
    private String accion;
    private String modulo;
    private String detalle;
    private String fecha;
    
    public AuditLog() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public static void registrar(String usuario, String accion, String modulo, String detalle) {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "INSERT INTO audit_logs (user_id, action, entity, entity_name) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, usuario);
                ps.setString(2, accion);
                ps.setString(3, modulo);
                ps.setString(4, detalle);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar audit_log: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public Iterator<AuditLog> listar() {
        return listarConFiltro("").iterator();
    }
    
    public Iterator<AuditLog> buscar(String busqueda) {
        return listarConFiltro(busqueda).iterator();
    }
    
    private List<AuditLog> listarConFiltro(String filtro) {
        List<AuditLog> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT id, user_id, action, entity, entity_name, created_at FROM audit_logs");
        if (filtro != null && !filtro.trim().isEmpty()) {
            sql.append(" WHERE user_id ILIKE ? OR action ILIKE ? OR entity ILIKE ? OR entity_name ILIKE ?");
        }
        sql.append(" ORDER BY created_at DESC LIMIT 200");
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                if (filtro != null && !filtro.trim().isEmpty()) {
                    String patron = "%" + filtro.trim() + "%";
                    ps.setString(1, patron);
                    ps.setString(2, patron);
                    ps.setString(3, patron);
                    ps.setString(4, patron);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AuditLog log = new AuditLog();
                        log.setId(rs.getInt("id"));
                        log.setUsuario(rs.getString("user_id"));
                        log.setAccion(rs.getString("action"));
                        String mod = rs.getString("entity");
                        log.setModulo(mod != null ? mod : "\u2014");
                        String det = rs.getString("entity_name");
                        log.setDetalle(det != null ? det : "");
                        java.sql.Timestamp ts = rs.getTimestamp("created_at");
                        log.setFecha(ts != null ? ts.toLocalDateTime().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "");
                        lista.add(log);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar audit_logs: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
}
