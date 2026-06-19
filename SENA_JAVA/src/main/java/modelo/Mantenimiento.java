package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Mantenimiento {
    
    private int idMantenimiento;
    private String nombreMantenimiento;
    private String documentoMantenimiento;
    private String direccionMantenimiento;
    private String celularMantenimiento;
    
    public Mantenimiento() {}

    public int getIdMantenimiento() { return idMantenimiento; }
    public void setIdMantenimiento(int idMantenimiento) { this.idMantenimiento = idMantenimiento; }

    public String getNombreMantenimiento() { return nombreMantenimiento; }
    public void setNombreMantenimiento(String nombreMantenimiento) { this.nombreMantenimiento = nombreMantenimiento; }

    public String getDocumentoMantenimiento() { return documentoMantenimiento; }
    public void setDocumentoMantenimiento(String documentoMantenimiento) { this.documentoMantenimiento = documentoMantenimiento; }

    public String getDireccionMantenimiento() { return direccionMantenimiento; }
    public void setDireccionMantenimiento(String direccionMantenimiento) { this.direccionMantenimiento = direccionMantenimiento; }

    public String getCelularMantenimiento() { return celularMantenimiento; }
    public void setCelularMantenimiento(String celularMantenimiento) { this.celularMantenimiento = celularMantenimiento; }

    public void insertar() {
        System.out.println("Insertar mantenimiento...");
    }
    
    public void modificar() {
        System.out.println("Modificar mantenimiento...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar mantenimiento...");
    }
    
    public Iterator<Mantenimiento> listar() {
        return new ArrayList<Mantenimiento>().iterator();
    }
    
    public Iterator<Mantenimiento> buscar(String busqueda) {
        return new ArrayList<Mantenimiento>().iterator();
    }
}
