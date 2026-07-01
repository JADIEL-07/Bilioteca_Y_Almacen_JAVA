package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    
    private String id;
    private String nombre;
    private String documento;
    private String tipo;
    private String email;
    private String celular;
    private String estado;
    private String password;
    
    public Usuario() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "INSERT INTO users (id, name, email, phone, password, is_active, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, documento);
                ps.setString(2, nombre);
                ps.setString(3, email);
                ps.setString(4, celular);
                ps.setString(5, password);
                ps.setBoolean(6, "Activo".equalsIgnoreCase(estado));
                
                int roleId = 7;
                if (tipo != null) {
                    String t = tipo.toUpperCase();
                    if (t.contains("ADMIN")) roleId = 1;
                    else if (t.contains("BIBLIOTECARIO")) roleId = 2;
                    else if (t.contains("ALMACENISTA")) roleId = 3;
                    else if (t.contains("SOPORTE") || t.contains("TECNICO")) roleId = 4;
                    else if (t.contains("EMPRESA")) roleId = 5;
                    else if (t.contains("APRENDIZ")) roleId = 6;
                }
                ps.setInt(7, roleId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_deleted = false ORDER BY created_at DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getString("id"));
                    u.setNombre(rs.getString("name"));
                    u.setDocumento(rs.getString("id"));
                    u.setEmail(rs.getString("email"));
                    u.setCelular(rs.getString("phone"));
                    u.setEstado(rs.getBoolean("is_active") ? "Activo" : "Inactivo");
                    u.setPassword(rs.getString("password"));
                    
                    int roleId = rs.getInt("role_id");
                    String t = "USUARIO";
                    switch (roleId) {
                        case 1: t = "ADMIN"; break;
                        case 2: t = "BIBLIOTECARIO"; break;
                        case 3: t = "ALMACENISTA"; break;
                        case 4: t = "SOPORTE_TECNICO"; break;
                        case 5: t = "EMPRESA"; break;
                        case 6: t = "APRENDIZ"; break;
                        case 7: t = "USUARIO"; break;
                    }
                    u.setTipo(t);
                    lista.add(u);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    public List<Usuario> buscar(String texto) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_deleted = false AND (name ILIKE ? OR email ILIKE ?) ORDER BY id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Usuario u = new Usuario();
                        u.setId(rs.getString("id"));
                        u.setNombre(rs.getString("name"));
                        u.setDocumento(rs.getString("id"));
                        u.setEmail(rs.getString("email"));
                        u.setCelular(rs.getString("phone"));
                        u.setEstado(rs.getBoolean("is_active") ? "Activo" : "Inactivo");
                        u.setPassword(rs.getString("password"));
                        
                        int roleId = rs.getInt("role_id");
                        String t = "USUARIO";
                        switch (roleId) {
                            case 1: t = "ADMIN"; break;
                            case 2: t = "BIBLIOTECARIO"; break;
                            case 3: t = "ALMACENISTA"; break;
                            case 4: t = "SOPORTE_TECNICO"; break;
                            case 5: t = "EMPRESA"; break;
                            case 6: t = "APRENDIZ"; break;
                            case 7: t = "USUARIO"; break;
                        }
                        u.setTipo(t);
                        lista.add(u);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    /**
     * Autentica directamente al usuario con una sola consulta WHERE id = documento.
     * Evita cargar todos los usuarios en memoria.
     * @return el hash BCrypt almacenado, o null si no existe el usuario.
     */
    public static String obtenerHashPassword(String documento) {
        String sql = "SELECT password FROM users WHERE id = ? AND is_active = true AND is_deleted = false";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, documento);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("password");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar hash de usuario: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return null;
    }

    public static void inicializarTabla() {
        System.out.println("Tabla 'users' remota ya verificada.");
    }
}
