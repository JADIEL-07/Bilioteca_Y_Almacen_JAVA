package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Reserva {
    
    private int idReserva;
    private String nombreReserva;
    private String documentoReserva;
    private String direccionReserva;
    private String celularReserva;
    
    public Reserva() {}

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public String getNombreReserva() { return nombreReserva; }
    public void setNombreReserva(String nombreReserva) { this.nombreReserva = nombreReserva; }

    public String getDocumentoReserva() { return documentoReserva; }
    public void setDocumentoReserva(String documentoReserva) { this.documentoReserva = documentoReserva; }

    public String getDireccionReserva() { return direccionReserva; }
    public void setDireccionReserva(String direccionReserva) { this.direccionReserva = direccionReserva; }

    public String getCelularReserva() { return celularReserva; }
    public void setCelularReserva(String celularReserva) { this.celularReserva = celularReserva; }

    public void insertar() {
        System.out.println("Insertar reserva...");
    }
    
    public void modificar() {
        System.out.println("Modificar reserva...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar reserva...");
    }
    
    public Iterator<Reserva> listar() {
        return new ArrayList<Reserva>().iterator();
    }
    
    public Iterator<Reserva> buscar(String busqueda) {
        return new ArrayList<Reserva>().iterator();
    }
}
