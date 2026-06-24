package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Prestamo {
    
    private int id;
    private String documentoUsuario;
    private String codigoItem;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado;
    
    public Prestamo() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDocumentoUsuario() { return documentoUsuario; }
    public void setDocumentoUsuario(String documentoUsuario) { this.documentoUsuario = documentoUsuario; }

    public String getCodigoItem() { return codigoItem; }
    public void setCodigoItem(String codigoItem) { this.codigoItem = codigoItem; }

    public String getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public String getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean insertar() {
        String sql = "INSERT INTO prestamos (documento_usuario, codigo_item, fecha_prestamo, fecha_devolucion, estado) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, documentoUsuario);
            ps.setString(2, codigoItem);
            ps.setDate(3, java.sql.Date.valueOf(fechaPrestamo));
            ps.setDate(4, java.sql.Date.valueOf(fechaDevolucion));
            ps.setString(5, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar prestamo: " + e.getMessage());
            return false;
        }
    }
    
    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setId(rs.getInt("id"));
                p.setDocumentoUsuario(rs.getString("documento_usuario"));
                p.setCodigoItem(rs.getString("codigo_item"));
                p.setFechaPrestamo(rs.getDate("fecha_prestamo").toString());
                p.setFechaDevolucion(rs.getDate("fecha_devolucion").toString());
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS prestamos (" +
                     "id SERIAL PRIMARY KEY, " +
                     "documento_usuario VARCHAR(50) NOT NULL, " +
                     "codigo_item VARCHAR(50) NOT NULL, " +
                     "fecha_prestamo DATE NOT NULL, " +
                     "fecha_devolucion DATE NOT NULL, " +
                     "estado VARCHAR(50) DEFAULT 'Activo')";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'prestamos' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla prestamos: " + e.getMessage());
        }
    }
}
