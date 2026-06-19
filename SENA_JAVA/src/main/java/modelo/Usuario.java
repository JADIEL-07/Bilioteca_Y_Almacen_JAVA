package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Usuario {
    
    private int idUsuario;
    private String nombreUsuario;
    private String documentoUsuario;
    private String direccionUsuario;
    private String celularUsuario;
    
    public Usuario() {}

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getDocumentoUsuario() { return documentoUsuario; }
    public void setDocumentoUsuario(String documentoUsuario) { this.documentoUsuario = documentoUsuario; }

    public String getDireccionUsuario() { return direccionUsuario; }
    public void setDireccionUsuario(String direccionUsuario) { this.direccionUsuario = direccionUsuario; }

    public String getCelularUsuario() { return celularUsuario; }
    public void setCelularUsuario(String celularUsuario) { this.celularUsuario = celularUsuario; }

    public void insertar() {
        System.out.println("Insertar usuario...");
    }
    
    public void modificar() {
        System.out.println("Modificar usuario...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar usuario...");
    }
    
    public Iterator<Usuario> listar() {
        return new ArrayList<Usuario>().iterator();
    }
    
    public Iterator<Usuario> buscar(String busqueda) {
        return new ArrayList<Usuario>().iterator();
    }
}
