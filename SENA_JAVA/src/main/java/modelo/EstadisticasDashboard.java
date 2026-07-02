package modelo;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasDashboard {
    private int totalElementos;
    private int disponibles;
    private int prestados;
    private int mantenimiento;
    private int reservasActivas;
    private List<String> prestamosPorMes;
    private List<String> proximasDevoluciones;
    private List<String> actividadReciente;

    public EstadisticasDashboard() {
        this.totalElementos = 0;
        this.disponibles = 0;
        this.prestados = 0;
        this.mantenimiento = 0;
        this.reservasActivas = 0;
        this.prestamosPorMes = new ArrayList<>();
        this.proximasDevoluciones = new ArrayList<>();
        this.actividadReciente = new ArrayList<>();
    }

    public int getTotalElementos() { return totalElementos; }
    public void setTotalElementos(int v) { this.totalElementos = v; }
    public int getDisponibles() { return disponibles; }
    public void setDisponibles(int v) { this.disponibles = v; }
    public int getPrestados() { return prestados; }
    public void setPrestados(int v) { this.prestados = v; }
    public int getMantenimiento() { return mantenimiento; }
    public void setMantenimiento(int v) { this.mantenimiento = v; }
    public int getReservasActivas() { return reservasActivas; }
    public void setReservasActivas(int v) { this.reservasActivas = v; }
    public List<String> getPrestamosPorMes() { return prestamosPorMes; }
    public void setPrestamosPorMes(List<String> v) { this.prestamosPorMes = v; }
    public List<String> getProximasDevoluciones() { return proximasDevoluciones; }
    public void setProximasDevoluciones(List<String> v) { this.proximasDevoluciones = v; }
    public List<String> getActividadReciente() { return actividadReciente; }
    public void setActividadReciente(List<String> v) { this.actividadReciente = v; }
}
