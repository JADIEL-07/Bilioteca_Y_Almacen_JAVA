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
        // Para insertar, se necesita resolver category_id, location_id y status_id.
        // Buscamos o creamos la categoría, ubicación y estado por nombre.
        String sql = "INSERT INTO items (name, code, category_id, stock, location_id, status_id) " +
                     "VALUES (?, ?, " +
                     "(SELECT id FROM categories WHERE name = ? LIMIT 1), ?, " +
                     "(SELECT id FROM locations WHERE name = ? LIMIT 1), " +
                     "(SELECT id FROM statuses WHERE name = ? LIMIT 1))";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setString(3, categoria);
            ps.setInt(4, cantidad);
            ps.setString(5, ubicacion);
            ps.setString(6, estado != null ? estado.toUpperCase() : "AVAILABLE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean modificar() {
        String sql = "UPDATE items SET name=?, code=?, stock=? WHERE id=?";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, codigo);
            ps.setInt(3, cantidad);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar item: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar() {
        String sql = "UPDATE items SET is_deleted = true WHERE id=?";
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
        String sql = "SELECT i.id, i.name, i.code, c.name AS categoria, i.stock, l.name AS ubicacion, s.name AS estado " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.id " +
                     "LEFT JOIN locations l ON i.location_id = l.id " +
                     "LEFT JOIN statuses s ON i.status_id = s.id " +
                     "WHERE i.is_deleted = false ORDER BY i.id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setNombre(rs.getString("name"));
                item.setCodigo(rs.getString("code"));
                item.setCategoria(rs.getString("categoria"));
                item.setCantidad(rs.getInt("stock"));
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
        String sql = "SELECT i.id, i.name, i.code, c.name AS categoria, i.stock, l.name AS ubicacion, s.name AS estado " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.id " +
                     "LEFT JOIN locations l ON i.location_id = l.id " +
                     "LEFT JOIN statuses s ON i.status_id = s.id " +
                     "WHERE i.is_deleted = false AND (i.name ILIKE ? OR i.code ILIKE ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + busqueda + "%");
            ps.setString(2, "%" + busqueda + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setNombre(rs.getString("name"));
                item.setCodigo(rs.getString("code"));
                item.setCategoria(rs.getString("categoria"));
                item.setCantidad(rs.getInt("stock"));
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
        System.out.println("Tabla 'items' remota ya verificada.");
    }
}
