package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Prestamo {
    
    private int idPrestamo;
    private String nombrePrestamo;
    private String documentoPrestamo;
    private String direccionPrestamo;
    private String celularPrestamo;
    
    public Prestamo() {}

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public String getNombrePrestamo() { return nombrePrestamo; }
    public void setNombrePrestamo(String nombrePrestamo) { this.nombrePrestamo = nombrePrestamo; }

    public String getDocumentoPrestamo() { return documentoPrestamo; }
    public void setDocumentoPrestamo(String documentoPrestamo) { this.documentoPrestamo = documentoPrestamo; }

    public String getDireccionPrestamo() { return direccionPrestamo; }
    public void setDireccionPrestamo(String direccionPrestamo) { this.direccionPrestamo = direccionPrestamo; }

    public String getCelularPrestamo() { return celularPrestamo; }
    public void setCelularPrestamo(String celularPrestamo) { this.celularPrestamo = celularPrestamo; }

    public void insertar() {
        System.out.println("Insertar prestamo...");
    }
    
    public void modificar() {
        System.out.println("Modificar prestamo...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar prestamo...");
    }
    
    public Iterator<Prestamo> listar() {
        return new ArrayList<Prestamo>().iterator();
    }
    
    public Iterator<Prestamo> buscar(String busqueda) {
        return new ArrayList<Prestamo>().iterator();
    }
}
