package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    
    private int id;
    private String documentoUsuario;
    private String asunto;
    private String descripcion;
    private String fechaCreacion;
    private String estado;
    
    public Ticket() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDocumentoUsuario() { return documentoUsuario; }
    public void setDocumentoUsuario(String documentoUsuario) { this.documentoUsuario = documentoUsuario; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean insertar() {
        String sql = "INSERT INTO tickets (documento_usuario, asunto, descripcion, fecha_creacion, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, documentoUsuario);
            ps.setString(2, asunto);
            ps.setString(3, descripcion);
            ps.setDate(4, java.sql.Date.valueOf(fechaCreacion));
            ps.setString(5, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar ticket: " + e.getMessage());
            return false;
        }
    }
    
    public List<Ticket> listar() {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id"));
                t.setDocumentoUsuario(rs.getString("documento_usuario"));
                t.setAsunto(rs.getString("asunto"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFechaCreacion(rs.getDate("fecha_creacion").toString());
                t.setEstado(rs.getString("estado"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tickets: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets (" +
                     "id SERIAL PRIMARY KEY, " +
                     "documento_usuario VARCHAR(50) NOT NULL, " +
                     "asunto VARCHAR(150) NOT NULL, " +
                     "descripcion TEXT NOT NULL, " +
                     "fecha_creacion DATE NOT NULL, " +
                     "estado VARCHAR(50) DEFAULT 'Abierto')";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'tickets' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla tickets: " + e.getMessage());
        }
    }
}
