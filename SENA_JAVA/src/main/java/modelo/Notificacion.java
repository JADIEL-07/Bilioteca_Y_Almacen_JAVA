package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Notificacion {
    
    private int id;
    private String titulo;
    private String mensaje;
    private String fecha;
    private boolean leida;
    
    public Notificacion() {}
    
    public Notificacion(int id, String titulo, String mensaje, String fecha, boolean leida) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = leida;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public static void inicializarTabla() {
        try (Connection con = ConexionBD.getInstance().getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS notifications ("
                       + "id SERIAL PRIMARY KEY, "
                       + "title VARCHAR(255) NOT NULL, "
                       + "message TEXT NOT NULL, "
                       + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                       + "is_read BOOLEAN DEFAULT FALSE"
                       + ")");

            try {
                stmt.execute("ALTER TABLE notifications ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
            } catch (SQLException ignored) {
            }

        } catch (SQLException e) {
            System.err.println("Error al inicializar tabla notifications: " + e.getMessage());
        }
    }

    public boolean insertar() {
        String sql = "INSERT INTO notifications (title, message) VALUES (?, ?)";
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, mensaje);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar notificacion: " + e.getMessage());
            return false;
        }
    }

    public List<Notificacion> listar(boolean soloNoLeidas) {
        List<Notificacion> lista = new ArrayList<>();
        String sql = "SELECT id, title, message, TO_CHAR(created_at, 'DD/MM/YYYY HH12:MI AM') as fecha_fmt, is_read "
                   + "FROM notifications ";
        if (soloNoLeidas) {
            sql += "WHERE is_read = false ";
        }
        sql += "ORDER BY created_at DESC LIMIT 50";
        
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Notificacion notif = new Notificacion(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("message"),
                    rs.getString("fecha_fmt"),
                    rs.getBoolean("is_read")
                );
                lista.add(notif);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar notificaciones: " + e.getMessage());
        }
        return lista;
    }

    public boolean marcarComoLeida(int idNotificacion) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idNotificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar como leida: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarTodasComoLeidas() {
        String sql = "UPDATE notifications SET is_read = true WHERE is_read = false";
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar todas como leidas: " + e.getMessage());
            return false;
        }
    }
    
    public boolean limpiarTodo() {
        String sql = "DELETE FROM notifications";
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al limpiar notificaciones: " + e.getMessage());
            return false;
        }
    }

    public int contarNoLeidas() {
        String sql = "SELECT COUNT(*) FROM notifications WHERE is_read = false";
        try (Connection con = ConexionBD.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar no leidas: " + e.getMessage());
        }
        return 0;
    }
}
