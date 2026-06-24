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
        String sql = "INSERT INTO mantenimientos (codigo_item, descripcion, fecha_envio, estado) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigoItem);
            ps.setString(2, descripcion);
            ps.setDate(3, java.sql.Date.valueOf(fechaEnvio));
            ps.setString(4, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar mantenimiento: " + e.getMessage());
            return false;
        }
    }
    
    public List<Mantenimiento> listar() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM mantenimientos ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setId(rs.getInt("id"));
                m.setCodigoItem(rs.getString("codigo_item"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setFechaEnvio(rs.getDate("fecha_envio").toString());
                m.setEstado(rs.getString("estado"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar mantenimientos: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS mantenimientos (" +
                     "id SERIAL PRIMARY KEY, " +
                     "codigo_item VARCHAR(50) NOT NULL, " +
                     "descripcion TEXT NOT NULL, " +
                     "fecha_envio DATE NOT NULL, " +
                     "estado VARCHAR(50) DEFAULT 'En progreso')";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'mantenimientos' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla mantenimientos: " + e.getMessage());
        }
    }
}
