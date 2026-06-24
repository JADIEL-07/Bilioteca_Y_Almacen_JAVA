package modelo;

public class EstadisticasDashboard {
    private int totalElementos;
    private int disponibles;
    private int prestados;
    private int mantenimiento;
    private int reservasActivas;

    public EstadisticasDashboard() {
        this.totalElementos = 0;
        this.disponibles = 0;
        this.prestados = 0;
        this.mantenimiento = 0;
        this.reservasActivas = 0;
    }

    public EstadisticasDashboard(int totalElementos, int disponibles, int prestados, int mantenimiento, int reservasActivas) {
        this.totalElementos = totalElementos;
        this.disponibles = disponibles;
        this.prestados = prestados;
        this.mantenimiento = mantenimiento;
        this.reservasActivas = reservasActivas;
    }

    public int getTotalElementos() {
        return totalElementos;
    }

    public void setTotalElementos(int totalElementos) {
        this.totalElementos = totalElementos;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getPrestados() {
        return prestados;
    }

    public void setPrestados(int prestados) {
        this.prestados = prestados;
    }

    public int getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(int mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public int getReservasActivas() {
        return reservasActivas;
    }

    public void setReservasActivas(int reservasActivas) {
        this.reservasActivas = reservasActivas;
    }
}
