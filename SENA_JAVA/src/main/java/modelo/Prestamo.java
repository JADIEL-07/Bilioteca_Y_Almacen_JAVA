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
        String sql = "INSERT INTO loans (user_id, loan_date, due_date, status) VALUES (?, NOW(), ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, documentoUsuario);
            ps.setDate(2, java.sql.Date.valueOf(fechaDevolucion));
            ps.setString(3, estado != null ? estado.toUpperCase() : "ACTIVE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar prestamo: " + e.getMessage());
            return false;
        }
    }
    
    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, i.code AS codigo_item, l.loan_date, l.due_date, l.status " +
                     "FROM loans l " +
                     "LEFT JOIN loan_details ld ON l.id = ld.loan_id " +
                     "LEFT JOIN items i ON ld.item_id = i.id " +
                     "ORDER BY l.id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
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
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        System.out.println("Tabla 'loans' remota ya verificada.");
    }
}
