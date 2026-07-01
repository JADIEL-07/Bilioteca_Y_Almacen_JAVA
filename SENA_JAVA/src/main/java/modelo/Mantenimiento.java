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

    public boolean insertar() {
        String sql = "INSERT INTO maintenance (item_id, failure_description, status, report_date) " +
                     "VALUES ((SELECT id FROM items WHERE code = ? LIMIT 1), ?, ?, NOW())";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigoItem);
            ps.setString(2, descripcion);
            ps.setString(3, estado != null ? estado.toUpperCase() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar mantenimiento: " + e.getMessage());
            return false;
        }
    }
    
    public List<Mantenimiento> listar() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id, i.code AS codigo_item, m.failure_description, m.report_date, m.status " +
                     "FROM maintenance m " +
                     "LEFT JOIN items i ON m.item_id = i.id " +
                     "ORDER BY m.id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setId(rs.getInt("id"));
                m.setCodigoItem(rs.getString("codigo_item") != null ? rs.getString("codigo_item") : "N/A");
                m.setDescripcion(rs.getString("failure_description"));
                java.sql.Timestamp ts = rs.getTimestamp("report_date");
                m.setFechaEnvio(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                m.setEstado(rs.getString("status"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mantenimientos: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        System.out.println("Tabla 'maintenance' remota ya verificada.");
    }
}
