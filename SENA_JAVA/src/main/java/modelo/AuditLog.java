package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class AuditLog {
    
    private int idAuditLog;
    private String nombreAuditLog;
    private String documentoAuditLog;
    private String direccionAuditLog;
    private String celularAuditLog;
    
    public AuditLog() {}

    public int getIdAuditLog() { return idAuditLog; }
    public void setIdAuditLog(int idAuditLog) { this.idAuditLog = idAuditLog; }

    public String getNombreAuditLog() { return nombreAuditLog; }
    public void setNombreAuditLog(String nombreAuditLog) { this.nombreAuditLog = nombreAuditLog; }

    public String getDocumentoAuditLog() { return documentoAuditLog; }
    public void setDocumentoAuditLog(String documentoAuditLog) { this.documentoAuditLog = documentoAuditLog; }

    public String getDireccionAuditLog() { return direccionAuditLog; }
    public void setDireccionAuditLog(String direccionAuditLog) { this.direccionAuditLog = direccionAuditLog; }

    public String getCelularAuditLog() { return celularAuditLog; }
    public void setCelularAuditLog(String celularAuditLog) { this.celularAuditLog = celularAuditLog; }

    public void insertar() {
        System.out.println("Insertar auditlog...");
    }
    
    public void modificar() {
        System.out.println("Modificar auditlog...");
    }
    
    public void eliminar() {
        System.out.println("Eliminar auditlog...");
    }
    
    public Iterator<AuditLog> listar() {
        return new ArrayList<AuditLog>().iterator();
    }
    
    public Iterator<AuditLog> buscar(String busqueda) {
        return new ArrayList<AuditLog>().iterator();
    }
}
