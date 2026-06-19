package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Ticket {
    
    private int idTicket;
    private String nombreTicket;
    private String documentoTicket;
    private String direccionTicket;
    private String celularTicket;
    
    public Ticket() {}

    public int getIdTicket() { return idTicket; }
    public void setIdTicket(int idTicket) { this.idTicket = idTicket; }

    public String getNombreTicket() { return nombreTicket; }
    public void setNombreTicket(String nombreTicket) { this.nombreTicket = nombreTicket; }

    public String getDocumentoTicket() { return documentoTicket; }
    public void setDocumentoTicket(String documentoTicket) { this.documentoTicket = documentoTicket; }

    public String getDireccionTicket() { return direccionTicket; }
    public void setDireccionTicket(String direccionTicket) { this.direccionTicket = direccionTicket; }

    public String getCelularTicket() { return celularTicket; }
    public void setCelularTicket(String celularTicket) { this.celularTicket = celularTicket; }

    public void insertar() {
        System.out.println("Insertar ticket...");
    }
    
    public void modificar() {
        System.out.println("Modificar ticket...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar ticket...");
    }
    
    public Iterator<Ticket> listar() {
        return new ArrayList<Ticket>().iterator();
    }
    
    public Iterator<Ticket> buscar(String busqueda) {
        return new ArrayList<Ticket>().iterator();
    }
}
