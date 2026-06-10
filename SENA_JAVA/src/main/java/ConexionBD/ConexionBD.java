/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author JADIEL
 */
public class ConexionBD {
    
    
    public static Connection conexion;
    private ConexionBD() {
        try {
            String driverDB = "org.postgresql.Driver";
            String urlDB = "jdbc:postgresql://194.163.142.34:5437/biblioteca_db";
            String usuarioDB = "Jadiel_Zz";
            String claveDB = "12872Jadiel#";
            Class.forName(driverDB);
            conexion = DriverManager.getConnection(urlDB,usuarioDB,claveDB);
            
        } catch (ClassNotFoundException ex) {
            System.err.println("No encuentro el driver"+ex.getMessage());
        } catch (SQLException ex){
            System.err.println("Error al conectarme"+ex.getMessage());
        }
    }
    public static boolean validarConexion() {
        if (conexion == null) {
            return false;
        }
        try {
            return !conexion.isClosed();
        } catch (SQLException ex) {
            System.err.println("Error al validar la conexión: " + ex.getMessage());
            return false;
        }
    }

    public static Connection getConnection() {
        if (!validarConexion()) {
            getInstance();
        }
        return conexion;
    }

    public static void desconectar(){
        if (conexion == null) {
            return;
        }
        try {
            if (!conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al desconectarse: " + ex.getMessage());
        }
    }
    
    public static ConexionBD getInstance() {
        return ConexionBDHolder.INSTANCE;
    }
    
    private static class ConexionBDHolder {

        private static final ConexionBD INSTANCE = new ConexionBD();
    }
}
