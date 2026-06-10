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
            //System.out.println("Conexion exitosa a la base de datos");
        } catch (ClassNotFoundException ex) {
            System.err.println("No encuentro el driver"+ex.getMessage());
        } catch (SQLException ex){
            System.err.println("Error al conectarme"+ex.getMessage());
        }
    }
    public static void desconectar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            System.out.println("Error al desconectarse"+ex.getMessage());
        }
    }
    
    public static ConexionBD getInstance() {
        return ConexionBDHolder.INSTANCE;
    }
    
    private static class ConexionBDHolder {

        private static final ConexionBD INSTANCE = new ConexionBD();
    }
}
