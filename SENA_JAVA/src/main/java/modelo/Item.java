package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Item {
    
    private int id;
    private String nombre;
    private String codigo;
    private String categoria;
    private int cantidad;
    private String ubicacion;
    private String estado;
    
    public Item() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean insertar() {
        String sql = "INSERT INTO items (nombre, codigo, categoria, cantidad, ubicacion, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setString(3, categoria);
            ps.setInt(4, cantidad);
            ps.setString(5, ubicacion);
            ps.setString(6, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean modificar() {
        String sql = "UPDATE items SET nombre=?, codigo=?, categoria=?, cantidad=?, ubicacion=?, estado=? WHERE id=?";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setString(3, categoria);
            ps.setInt(4, cantidad);
            ps.setString(5, ubicacion);
            ps.setString(6, estado);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar() {
        String sql = "DELETE FROM items WHERE id=?";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
            return false;
        }
    }
    
    public List<Item> listar() {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setNombre(rs.getString("nombre"));
                item.setCodigo(rs.getString("codigo"));
                item.setCategoria(rs.getString("categoria"));
                item.setCantidad(rs.getInt("cantidad"));
                item.setUbicacion(rs.getString("ubicacion"));
                item.setEstado(rs.getString("estado"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar items: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Item> buscar(String busqueda) {
        List<Item> lista = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE nombre ILIKE ? OR codigo ILIKE ?";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, "%" + busqueda + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setNombre(rs.getString("nombre"));
                item.setCodigo(rs.getString("codigo"));
                item.setCategoria(rs.getString("categoria"));
                item.setCantidad(rs.getInt("cantidad"));
                item.setUbicacion(rs.getString("ubicacion"));
                item.setEstado(rs.getString("estado"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar items: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS items (" +
                     "id SERIAL PRIMARY KEY, " +
                     "nombre VARCHAR(100) NOT NULL, " +
                     "codigo VARCHAR(50) UNIQUE NOT NULL, " +
                     "categoria VARCHAR(50) NOT NULL, " +
                     "cantidad INT DEFAULT 0, " +
                     "ubicacion VARCHAR(100), " +
                     "estado VARCHAR(50) DEFAULT 'Disponible')";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'items' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla items: " + e.getMessage());
        }
    }
}
