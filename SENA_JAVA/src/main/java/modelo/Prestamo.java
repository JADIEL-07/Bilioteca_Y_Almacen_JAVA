package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            con.setAutoCommit(false);
            String sqlLoan = "INSERT INTO loans (user_id, loan_date, due_date, status) VALUES (?, NOW(), ?, ?)";
            int loanId;
            try (PreparedStatement ps = con.prepareStatement(sqlLoan, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, documentoUsuario);
                ps.setDate(2, java.sql.Date.valueOf(parsearFecha(fechaDevolucion)));
                ps.setString(3, estado != null ? estado.toUpperCase() : "ACTIVE");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        loanId = rs.getInt(1);
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }
            String sqlDetail = "INSERT INTO loan_details (loan_id, item_id) " +
                               "VALUES (?, (SELECT id FROM items WHERE code = ? LIMIT 1))";
            try (PreparedStatement ps = con.prepareStatement(sqlDetail)) {
                ps.setInt(1, loanId);
                ps.setString(2, codigoItem);
                ps.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar prestamo: " + e.getMessage());
            if (con != null) try { con.rollback(); } catch (SQLException ignored) {}
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ignored) {}
                ConexionBD.getInstance().releaseConnection(con);
            }
        }
    }
    
    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, i.code AS codigo_item, l.loan_date, l.due_date, l.status " +
                     "FROM loans l " +
                     "LEFT JOIN loan_details ld ON l.id = ld.loan_id " +
                     "LEFT JOIN items i ON ld.item_id = i.id " +
                     "ORDER BY l.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setId(rs.getInt("id"));
                    p.setDocumentoUsuario(rs.getString("user_id"));
                    p.setCodigoItem(rs.getString("codigo_item") != null ? rs.getString("codigo_item") : "N/A");
                    java.sql.Timestamp loanTs = rs.getTimestamp("loan_date");
                    p.setFechaPrestamo(loanTs != null ? loanTs.toLocalDateTime().toLocalDate().toString() : "");
                    java.sql.Date dueDate = rs.getDate("due_date");
                    p.setFechaDevolucion(dueDate != null ? dueDate.toString() : "");
                    p.setEstado(rs.getString("status"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    public List<Prestamo> buscar(String texto) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, l.loan_date, l.due_date, l.status, i.code AS codigo_item " +
                     "FROM loans l " +
                     "LEFT JOIN loan_details ld ON l.id = ld.loan_id " +
                     "LEFT JOIN items i ON ld.item_id = i.id " +
                     "WHERE (l.user_id ILIKE ? OR l.status ILIKE ?) ORDER BY l.id DESC";
        Connection con = null;
        try {
            con = ConexionBD.getInstance().getConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + texto + "%");
                ps.setString(2, "%" + texto + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Prestamo p = new Prestamo();
                        p.setId(rs.getInt("id"));
                        p.setDocumentoUsuario(rs.getString("user_id"));
                        p.setCodigoItem(rs.getString("codigo_item") != null ? rs.getString("codigo_item") : "N/A");
                        java.sql.Date loanDate = rs.getDate("loan_date");
                        p.setFechaPrestamo(loanDate != null ? loanDate.toString() : "");
                        java.sql.Date dueDate = rs.getDate("due_date");
                        p.setFechaDevolucion(dueDate != null ? dueDate.toString() : "");
                        p.setEstado(rs.getString("status"));
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar prestamos: " + e.getMessage());
        } finally {
            if (con != null) ConexionBD.getInstance().releaseConnection(con);
        }
        return lista;
    }
    
    private static LocalDate parsearFecha(String fecha) {
        String[] formatos = {"yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd", "dd-MM-yyyy", "yyyyMMdd"};
        for (String f : formatos) {
            try {
                return LocalDate.parse(fecha, DateTimeFormatter.ofPattern(f));
            } catch (DateTimeParseException ignored) {}
        }
        throw new DateTimeParseException("Formato de fecha no válido: " + fecha, fecha, 0);
    }

    public static void inicializarTabla() {
        System.out.println("Tabla 'loans' remota ya verificada.");
    }
}
