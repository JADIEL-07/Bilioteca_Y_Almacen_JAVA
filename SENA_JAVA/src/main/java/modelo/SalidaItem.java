package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class SalidaItem {
    
    private int idSalidaItem;
    private String nombreSalidaItem;
    private String documentoSalidaItem;
    private String direccionSalidaItem;
    private String celularSalidaItem;
    
    public SalidaItem() {}

    public int getIdSalidaItem() { return idSalidaItem; }
    public void setIdSalidaItem(int idSalidaItem) { this.idSalidaItem = idSalidaItem; }

    public String getNombreSalidaItem() { return nombreSalidaItem; }
    public void setNombreSalidaItem(String nombreSalidaItem) { this.nombreSalidaItem = nombreSalidaItem; }

    public String getDocumentoSalidaItem() { return documentoSalidaItem; }
    public void setDocumentoSalidaItem(String documentoSalidaItem) { this.documentoSalidaItem = documentoSalidaItem; }

    public String getDireccionSalidaItem() { return direccionSalidaItem; }
    public void setDireccionSalidaItem(String direccionSalidaItem) { this.direccionSalidaItem = direccionSalidaItem; }

    public String getCelularSalidaItem() { return celularSalidaItem; }
    public void setCelularSalidaItem(String celularSalidaItem) { this.celularSalidaItem = celularSalidaItem; }

    public void insertar() {
        System.out.println("Insertar salidaitem...");
    }
    
    public void modificar() {
        System.out.println("Modificar salidaitem...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar salidaitem...");
    }
    
    public Iterator<SalidaItem> listar() {
        return new ArrayList<SalidaItem>().iterator();
    }
    
    public Iterator<SalidaItem> buscar(String busqueda) {
        return new ArrayList<SalidaItem>().iterator();
    }
}
