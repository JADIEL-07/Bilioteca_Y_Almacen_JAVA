package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class Item {
    
    private int idItem;
    private String nombreItem;
    private String documentoItem;
    private String direccionItem;
    private String celularItem;
    
    public Item() {}

    public int getIdItem() { return idItem; }
    public void setIdItem(int idItem) { this.idItem = idItem; }

    public String getNombreItem() { return nombreItem; }
    public void setNombreItem(String nombreItem) { this.nombreItem = nombreItem; }

    public String getDocumentoItem() { return documentoItem; }
    public void setDocumentoItem(String documentoItem) { this.documentoItem = documentoItem; }

    public String getDireccionItem() { return direccionItem; }
    public void setDireccionItem(String direccionItem) { this.direccionItem = direccionItem; }

    public String getCelularItem() { return celularItem; }
    public void setCelularItem(String celularItem) { this.celularItem = celularItem; }

    public void insertar() {
        System.out.println("Insertar item...");
    }
    
    public void modificar() {
        System.out.println("Modificar item...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar item...");
    }
    
    public Iterator<Item> listar() {
        return new ArrayList<Item>().iterator();
    }
    
    public Iterator<Item> buscar(String busqueda) {
        return new ArrayList<Item>().iterator();
    }
}
