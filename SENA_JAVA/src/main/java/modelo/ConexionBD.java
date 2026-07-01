package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private static final String URL = "jdbc:postgresql://194.163.142.34:5437/biblioteca_db";
    private static final String USUARIO = "Jadiel_Zz";
    private static final String CLAVE = "12872Jadiel#";
    
    public static Connection conexion;
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexion establecida con exito a PostgreSQL!");
        } catch (ClassNotFoundException ex) {
            System.err.println("No se encuentra el driver JDBC: " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("Error de base de datos al conectar: " + ex.getMessage());
        }
    }

    private ConexionBD() {}

    // Método usado por los modelos nuevos (MVC)
    public Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
        }
        return conexion;
    }

    public static void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexion: " + ex.getMessage());
        }
    }
    
    public static ConexionBD getInstance() {
        return ConexionBDHolder.INSTANCE;
    }
    
    private static class ConexionBDHolder {
        private static final ConexionBD INSTANCE = new ConexionBD();
    }
}
