package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    private int id;
    private String nombre;
    private String documento;
    private String tipo;
    private String email;
    private String celular;
    private String estado;
    private String password;
    
    public Usuario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean insertar() {
        String sql = "INSERT INTO usuarios (nombre, documento, tipo, email, celular, estado, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, documento);
            ps.setString(3, tipo);
            ps.setString(4, email);
            ps.setString(5, celular);
            ps.setString(6, estado);
            ps.setString(7, password);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setDocumento(rs.getString("documento"));
                u.setTipo(rs.getString("tipo"));
                u.setEmail(rs.getString("email"));
                u.setCelular(rs.getString("celular"));
                u.setEstado(rs.getString("estado"));
                // Solo leer password si existe (puede ser null en la BD)
                try {
                    u.setPassword(rs.getString("password"));
                } catch (Exception ex) {}
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                     "id SERIAL PRIMARY KEY, " +
                     "nombre VARCHAR(150) NOT NULL, " +
                     "documento VARCHAR(50) UNIQUE NOT NULL, " +
                     "tipo VARCHAR(50) NOT NULL, " +
                     "email VARCHAR(100), " +
                     "celular VARCHAR(20), " +
                     "estado VARCHAR(50) DEFAULT 'Activo', " +
                     "password VARCHAR(255))";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'usuarios' verificada/creada.");
            
            // Add column if it doesn't exist (for existing tables)
            try {
                PreparedStatement psAlt = con.prepareStatement("ALTER TABLE usuarios ADD COLUMN password VARCHAR(255)");
                psAlt.execute();
            } catch (SQLException ignore) {
                // Column probably already exists
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear tabla usuarios: " + e.getMessage());
        }
    }
}
