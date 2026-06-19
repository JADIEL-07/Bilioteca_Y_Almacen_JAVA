package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    public static Connection conexion = getInstance().getConexionInternal();
    private Connection conReal;

    private ConexionBD() {
        try {
            String driverBD = "org.postgresql.Driver";
            
            // Usamos localhost y la base de datos biblioteca_sena
            String urlBD = "jdbc:postgresql://localhost:5432/biblioteca_sena";
            String usuarioBD = "postgres"; 
            String claveBD = "12345"; 
            
            Class.forName(driverBD);
            conReal = DriverManager.getConnection(urlBD, usuarioBD, claveBD);
            
            System.out.println("¡Conexión establecida con éxito a PostgreSQL!");
        } catch (ClassNotFoundException ex) {
            System.err.println("No se encuentra el driver JDBC: " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("Error de base de datos al conectar: " + ex.getMessage());
        }
    }
    
    private Connection getConexionInternal() {
        return this.conReal;
    }

    public static void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
    
    public static ConexionBD getInstance() {
        return ConexionBDHolder.INSTANCE;
    }
    
    private static class ConexionBDHolder {
        private static final ConexionBD INSTANCE = new ConexionBD();
    }
}
