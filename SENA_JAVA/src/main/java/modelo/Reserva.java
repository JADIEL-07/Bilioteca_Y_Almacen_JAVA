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
        String sql = "INSERT INTO reservas (documento_usuario, codigo_item, fecha_reserva, estado) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, documentoUsuario);
            ps.setString(2, codigoItem);
            ps.setDate(3, java.sql.Date.valueOf(fechaReserva));
            ps.setString(4, estado);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
            return false;
        }
    }
    
    public List<Reserva> listar() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas ORDER BY id DESC";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setDocumentoUsuario(rs.getString("documento_usuario"));
                r.setCodigoItem(rs.getString("codigo_item"));
                r.setFechaReserva(rs.getDate("fecha_reserva").toString());
                r.setEstado(rs.getString("estado"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
        }
        return lista;
    }
    
    public static void inicializarTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS reservas (" +
                     "id SERIAL PRIMARY KEY, " +
                     "documento_usuario VARCHAR(50) NOT NULL, " +
                     "codigo_item VARCHAR(50) NOT NULL, " +
                     "fecha_reserva DATE NOT NULL, " +
                     "estado VARCHAR(50) DEFAULT 'Pendiente')";
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            System.out.println("Tabla 'reservas' verificada/creada.");
        } catch (SQLException e) {
            System.err.println("Error al crear tabla reservas: " + e.getMessage());
        }
    }
}
