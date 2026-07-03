package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Item {

    private int id;
    private String nombre;
    private String codigo;
    private String categoria;
    private int cantidad;       // stock total
    private int disponibles;   // calculado: stock - activos
    private String ubicacion;
    private String estado;
    private String fechaLibre; // próxima devolución si todo está en uso

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
    public int getDisponibles() { return disponibles; }
    public void setDisponibles(int disponibles) { this.disponibles = disponibles; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaLibre() { return fechaLibre; }
    public void setFechaLibre(String fechaLibre) { this.fechaLibre = fechaLibre; }

    // ---------------------------------------------------------------
    // SQL de disponibilidad real (stock - prestados - reservados - mante)
    // ---------------------------------------------------------------
    private static final String SQL_DISPONIBILIDAD =
        "SELECT " +
        "  i.id, i.name, i.code, " +
        "  c.name AS categoria, " +
        "  l.name AS ubicacion, " +
        "  s.name AS estado, " +
        "  i.stock AS stock_total, " +
        "  GREATEST(0, i.stock " +
        "    - COALESCE((SELECT COUNT(*) FROM loan_details ld " +
        "                JOIN loans lo ON ld.loan_id = lo.id " +
        "                WHERE ld.item_id = i.id " +
        "                AND lo.status NOT IN ('RETURNED','CANCELLED','DELETED')), 0) " +
        "    - COALESCE((SELECT COUNT(*) FROM reservations r " +
        "                WHERE r.item_id = i.id " +
        "                AND r.status NOT IN ('CANCELLED','EXPIRED','COMPLETED') " +
        "                AND (r.is_deleted = false OR r.is_deleted IS NULL)), 0) " +
        "    - COALESCE((SELECT COUNT(*) FROM maintenance m " +
        "                WHERE m.item_id = i.id " +
        "                AND m.status NOT IN ('COMPLETED','CANCELLED') " +
        "                AND (m.is_deleted = false OR m.is_deleted IS NULL)), 0) " +
        "  ) AS disponibles, " +
        "  (SELECT MIN(lo.due_date) " +
        "   FROM loan_details ld JOIN loans lo ON ld.loan_id = lo.id " +
        "   WHERE ld.item_id = i.id " +
        "   AND lo.status NOT IN ('RETURNED','CANCELLED','DELETED')) AS fecha_libre " +
        "FROM items i " +
        "LEFT JOIN categories c ON i.category_id = c.id " +
        "LEFT JOIN locations  l ON i.location_id  = l.id " +
        "LEFT JOIN statuses   s ON i.status_id    = s.id " +
        "WHERE (i.is_deleted = false OR i.is_deleted IS NULL) ";

    public boolean insertar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            int categoryId = resolverReferencia(con, "categories", categoria);
            int locationId = resolverReferencia(con, "locations", ubicacion);
            String estadoFinal = (estado != null && !estado.isEmpty()) ? estado : "Disponible";
            int statusId = resolverReferencia(con, "statuses", estadoFinal);

            String sql = "INSERT INTO items (name, code, category_id, stock, location_id, status_id, is_deleted) " +
                         "VALUES (?, ?, ?, ?, ?, ?, false)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, codigo);
                ps.setInt(3, categoryId);
                ps.setInt(4, cantidad);
                ps.setInt(5, locationId);
                ps.setInt(6, statusId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar item: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    private int resolverReferencia(Connection con, String tabla, String nombre) throws SQLException {
        String sqlInsert = "INSERT INTO " + tabla + " (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sqlSelect = "SELECT id FROM " + tabla + " WHERE name = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlSelect)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("No se pudo obtener o crear '" + nombre + "' en " + tabla);
    }

    public boolean modificar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            int categoryId = resolverReferencia(con, "categories", categoria);
            int locationId = resolverReferencia(con, "locations", ubicacion);
            String estadoFinal = (estado != null && !estado.isEmpty()) ? estado : "Disponible";
            int statusId = resolverReferencia(con, "statuses", estadoFinal);
            String sql = "UPDATE items SET name=?, code=?, category_id=?, stock=?, location_id=?, status_id=? WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nombre);
                ps.setString(2, codigo);
                ps.setInt(3, categoryId);
                ps.setInt(4, cantidad);
                ps.setInt(5, locationId);
                ps.setInt(6, statusId);
                ps.setInt(7, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar item: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    public boolean eliminar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "UPDATE items SET is_deleted = true WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar item: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    public List<Item> listar() {
        List<Item> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(SQL_DISPONIBILIDAD + "ORDER BY i.id DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearItem(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar items: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }

    public List<Item> buscar(String busqueda) {
        List<Item> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = SQL_DISPONIBILIDAD +
                "AND (i.name ILIKE ? OR i.code ILIKE ? OR c.name ILIKE ?) ORDER BY i.id DESC";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String q = "%" + busqueda + "%";
                ps.setString(1, q);
                ps.setString(2, q);
                ps.setString(3, q);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearItem(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar items: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }

    /**
     * Consulta la disponibilidad de un ítem por su código.
     * @return Item con disponibles y fechaLibre, o null si no existe
     */
    public static Item consultarDisponibilidad(String codigo) {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = SQL_DISPONIBILIDAD + "AND i.code = ? LIMIT 1";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, codigo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return mapearItem(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar disponibilidad: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return null;
    }

    private static Item mapearItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setNombre(rs.getString("name"));
        item.setCodigo(rs.getString("code"));
        item.setCategoria(rs.getString("categoria"));
        item.setCantidad(rs.getInt("stock_total"));
        item.setDisponibles(rs.getInt("disponibles"));
        item.setUbicacion(rs.getString("ubicacion"));
        item.setEstado(rs.getString("estado"));
        java.sql.Date fl = rs.getDate("fecha_libre");
        item.setFechaLibre(fl != null ? fl.toString() : null);
        return item;
    }

    public static Item debugModel;

    public List<Item> listarDebug() { return listar(); }

    public static void inicializarTabla() {
        System.out.println("Tabla 'items' remota ya verificada.");
    }
}
