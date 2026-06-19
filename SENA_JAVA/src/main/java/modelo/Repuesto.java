package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Repuesto {
    
    private int idRepuesto;
    private String nombreRepuesto;
    private String documentoRepuesto;
    private String direccionRepuesto;
    private String celularRepuesto;
    
    public Repuesto() {}

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public String getNombreRepuesto() { return nombreRepuesto; }
    public void setNombreRepuesto(String nombreRepuesto) { this.nombreRepuesto = nombreRepuesto; }

    public String getDocumentoRepuesto() { return documentoRepuesto; }
    public void setDocumentoRepuesto(String documentoRepuesto) { this.documentoRepuesto = documentoRepuesto; }

    public String getDireccionRepuesto() { return direccionRepuesto; }
    public void setDireccionRepuesto(String direccionRepuesto) { this.direccionRepuesto = direccionRepuesto; }

    public String getCelularRepuesto() { return celularRepuesto; }
    public void setCelularRepuesto(String celularRepuesto) { this.celularRepuesto = celularRepuesto; }

    public void insertar() {
        System.out.println("Insertar repuesto...");
    }
    
    public void modificar() {
        System.out.println("Modificar repuesto...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar repuesto...");
    }
    
    public Iterator<Repuesto> listar() {
        return new ArrayList<Repuesto>().iterator();
    }
    
    public Iterator<Repuesto> buscar(String busqueda) {
        return new ArrayList<Repuesto>().iterator();
    }
}
