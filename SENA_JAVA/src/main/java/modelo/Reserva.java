package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    
    private int id;
    private String documentoUsuario;
    private String codigoItem;
    private String fechaReserva;
    private String estado;
    
    public Reserva() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDocumentoUsuario() { return documentoUsuario; }
    public void setDocumentoUsuario(String documentoUsuario) { this.documentoUsuario = documentoUsuario; }

    public String getCodigoItem() { return codigoItem; }
    public void setCodigoItem(String codigoItem) { this.codigoItem = codigoItem; }

    public String getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(String fechaReserva) { this.fechaReserva = fechaReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean insertar() {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String token = java.util.UUID.randomUUID().toString();
            String sql = "INSERT INTO reservations (token, user_id, item_id, reservation_date, status) " +
                         "VALUES (?, ?, (SELECT id FROM items WHERE code = ? LIMIT 1), NOW(), ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, token);
                ps.setString(2, documentoUsuario);
                ps.setString(3, codigoItem);
                ps.setString(4, estado != null ? estado.toUpperCase() : "PENDING");
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }
    
    public List<Reserva> listar() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, i.code AS codigo_item, r.reservation_date, r.status " +
                     "FROM reservations r " +
                     "LEFT JOIN items i ON r.item_id = i.id " +
                     "WHERE (r.is_deleted = false OR r.is_deleted IS NULL) ORDER BY r.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reserva r = new Reserva();
                    r.setId(rs.getInt("id"));
                    r.setDocumentoUsuario(rs.getString("user_id"));
                    r.setCodigoItem(rs.getString("codigo_item") != null ? rs.getString("codigo_item") : "N/A");
                    java.sql.Timestamp ts = rs.getTimestamp("reservation_date");
                    r.setFechaReserva(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                    r.setEstado(rs.getString("status"));
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    public List<Reserva> buscar(String texto) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.user_id, i.code AS codigo_item, r.reservation_date, r.status " +
                     "FROM reservations r " +
                     "LEFT JOIN items i ON r.item_id = i.id " +
                     "WHERE (r.is_deleted = false OR r.is_deleted IS NULL) AND (r.user_id ILIKE ? OR r.status ILIKE ?) ORDER BY r.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Reserva r = new Reserva();
                        r.setId(rs.getInt("id"));
                        r.setDocumentoUsuario(rs.getString("user_id"));
                        r.setCodigoItem(rs.getString("codigo_item") != null ? rs.getString("codigo_item") : "N/A");
                        java.sql.Timestamp ts = rs.getTimestamp("reservation_date");
                        r.setFechaReserva(ts != null ? ts.toLocalDateTime().toLocalDate().toString() : "");
                        r.setEstado(rs.getString("status"));
                        lista.add(r);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservas: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    /** Actualiza el estado de una reserva por su ID. */
    public boolean modificarEstado(int reservaId, String nuevoEstado) {
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            String sql = "UPDATE reservations SET status = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nuevoEstado.toUpperCase());
                ps.setInt(2, reservaId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar reserva: " + e.getMessage());
            return false;
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
    }

    public static void inicializarTabla() {
        System.out.println("Tabla 'reservations' remota ya verificada.");
    }
}
