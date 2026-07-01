package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Repuesto {
    
    private int idRepuesto;
    private String nombreRepuesto;
    private String documentoRepuesto;
    private String direccionRepuesto;
    private String celularRepuesto;
    
    public Repuesto() {}

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public String getNombreRepuesto() { return nombreRepuesto; }
    public void setNombreRepuesto(String nombreRepuesto) { this.nombreRepuesto = nombreRepuesto; }

    public String getDocumentoRepuesto() { return documentoRepuesto; }
    public void setDocumentoRepuesto(String documentoRepuesto) { this.documentoRepuesto = documentoRepuesto; }

    public String getDireccionRepuesto() { return direccionRepuesto; }
    public void setDireccionRepuesto(String direccionRepuesto) { this.direccionRepuesto = direccionRepuesto; }

    public String getCelularRepuesto() { return celularRepuesto; }
    public void setCelularRepuesto(String celularRepuesto) { this.celularRepuesto = celularRepuesto; }

    public boolean insertar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "INSERT INTO spare_parts (name, document, address, phone) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombreRepuesto);
                ps.setString(2, documentoRepuesto);
                ps.setString(3, direccionRepuesto);
                ps.setString(4, celularRepuesto);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar repuesto: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public boolean modificar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "UPDATE spare_parts SET name=?, document=?, address=?, phone=? WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombreRepuesto);
                ps.setString(2, documentoRepuesto);
                ps.setString(3, direccionRepuesto);
                ps.setString(4, celularRepuesto);
                ps.setInt(5, idRepuesto);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar repuesto: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public boolean eliminar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "UPDATE spare_parts SET is_deleted = true WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idRepuesto);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar repuesto: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public Iterator<Repuesto> listar() {
        return listar("").iterator();
    }
    
    public Iterator<Repuesto> buscar(String busqueda) {
        return listar(busqueda).iterator();
    }
    
    private java.util.List<Repuesto> listar(String filtro) {
        java.util.List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT id, name, document, address, phone FROM spare_parts WHERE is_deleted = false";
        if (filtro != null && !filtro.trim().isEmpty()) {
            sql += " AND (name ILIKE ? OR document ILIKE ?)";
        }
        sql += " ORDER BY id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                if (filtro != null && !filtro.trim().isEmpty()) {
                    ps.setString(1, "%" + filtro.trim() + "%");
                    ps.setString(2, "%" + filtro.trim() + "%");
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Repuesto r = new Repuesto();
                        r.setIdRepuesto(rs.getInt("id"));
                        r.setNombreRepuesto(rs.getString("name"));
                        r.setDocumentoRepuesto(rs.getString("document"));
                        r.setDireccionRepuesto(rs.getString("address"));
                        r.setCelularRepuesto(rs.getString("phone"));
                        lista.add(r);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar repuestos: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
}
