package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuditLog {
    
    private int idAuditLog;
    private String nombreAuditLog;      // user_id (document)
    private String documentoAuditLog;   // action
    private String direccionAuditLog;   // entity / entity_name
    private String celularAuditLog;     // created_at (fecha)
    
    public AuditLog() {}

    public int getIdAuditLog() { return idAuditLog; }
    public void setIdAuditLog(int idAuditLog) { this.idAuditLog = idAuditLog; }

    public String getNombreAuditLog() { return nombreAuditLog; }
    public void setNombreAuditLog(String nombreAuditLog) { this.nombreAuditLog = nombreAuditLog; }

    public String getDocumentoAuditLog() { return documentoAuditLog; }
    public void setDocumentoAuditLog(String documentoAuditLog) { this.documentoAuditLog = documentoAuditLog; }

    public String getDireccionAuditLog() { return direccionAuditLog; }
    public void setDireccionAuditLog(String direccionAuditLog) { this.direccionAuditLog = direccionAuditLog; }

    public String getCelularAuditLog() { return celularAuditLog; }
    public void setCelularAuditLog(String celularAuditLog) { this.celularAuditLog = celularAuditLog; }

    public void insertar() {
        System.out.println("Módulo de auditoría de solo lectura.");
    }
    
    public void modificar() {
        System.out.println("Módulo de auditoría de solo lectura.");
    }
    
    public void eliminar() {
        System.out.println("Módulo de auditoría de solo lectura.");
    }
    
    /**
     * Lista todos los registros de auditoría desde la tabla remota audit_logs.
     * El mapeo de campos es:
     *   nombreAuditLog   -> user_id
     *   documentoAuditLog -> action
     *   direccionAuditLog -> entity_name (o entity si entity_name es nulo)
     *   celularAuditLog   -> created_at (fecha formateada)
     */
    public Iterator<AuditLog> listar() {
        List<AuditLog> lista = new ArrayList<>();
        String sql = "SELECT id, user_id, action, entity, entity_name, created_at " +
                     "FROM audit_logs ORDER BY created_at DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setIdAuditLog(rs.getInt("id"));
                log.setNombreAuditLog(rs.getString("user_id"));
                log.setDocumentoAuditLog(rs.getString("action"));
                String entidad = rs.getString("entity_name");
                if (entidad == null || entidad.isBlank()) entidad = rs.getString("entity");
                log.setDireccionAuditLog(entidad != null ? entidad : "—");
                java.sql.Timestamp ts = rs.getTimestamp("created_at");
                log.setCelularAuditLog(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                lista.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar audit_logs: " + e.getMessage());
        }
        return lista.iterator();
    }
    
    public Iterator<AuditLog> buscar(String busqueda) {
        List<AuditLog> lista = new ArrayList<>();
        String sql = "SELECT id, user_id, action, entity, entity_name, created_at " +
                     "FROM audit_logs WHERE user_id ILIKE ? OR action ILIKE ? OR entity_name ILIKE ? " +
                     "ORDER BY created_at DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            String patron = "%" + busqueda + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setIdAuditLog(rs.getInt("id"));
                log.setNombreAuditLog(rs.getString("user_id"));
                log.setDocumentoAuditLog(rs.getString("action"));
                String entidad = rs.getString("entity_name");
                if (entidad == null || entidad.isBlank()) entidad = rs.getString("entity");
                log.setDireccionAuditLog(entidad != null ? entidad : "—");
                java.sql.Timestamp ts = rs.getTimestamp("created_at");
                log.setCelularAuditLog(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                lista.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar audit_logs: " + e.getMessage());
        }
        return lista.iterator();
    }
}
