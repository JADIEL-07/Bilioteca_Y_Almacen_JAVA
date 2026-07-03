package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Pool de conexiones liviano y optimizado para alto rendimiento y concurrencia.
 * Evita la creación excesiva de conexiones y fugas de memoria.
 */
public class ConexionBD {

    private static final String URL     = "jdbc:postgresql://194.163.142.34:5437/biblioteca_db";
    private static final String USUARIO = "Jadiel_Zz";
    private static final String CLAVE   = "12872Jadiel#";

    private static final int POOL_SIZE = 10; // Aumentado a 10 para soportar mejor la carga
    private static final BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(POOL_SIZE);

    // Instancia singleton
    private static final ConexionBD INSTANCE = new ConexionBD();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.offer(crearNuevaConexion());
            }
            System.out.println("Pool de " + POOL_SIZE + " conexiones inicializado.");
        } catch (Exception ex) {
            System.err.println("Error al inicializar pool de conexiones: " + ex.getMessage());
        }
    }

    private ConexionBD() {}

    public static ConexionBD getInstance() {
        return INSTANCE;
    }

    /** @deprecated Usar getInstance().getConnection() */
    public static Connection conexion = null;

    /**
     * Obtiene una conexión del pool de manera segura.
     * Espera hasta 5 segundos si el pool está vacío.
     */
    public Connection getConnection() throws SQLException {
        try {
            // Esperar hasta 5 segundos por una conexión disponible
            Connection con = pool.poll(5, TimeUnit.SECONDS);
            
            if (con != null) {
                // Validar la conexión (1 segundo timeout)
                if (con.isClosed() || !con.isValid(1)) {
                    // Si estaba rota o cerrada, creamos una nueva para reemplazarla
                    return crearNuevaConexion();
                }
                return con;
            } else {
                // Timeout: el pool está a su máxima capacidad y ocupado
                System.err.println("[ConexionBD] Advertencia: Pool saturado. Creando conexión temporal.");
                return crearNuevaConexion(); // Conexión de emergencia
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Hilo interrumpido esperando conexión.", e);
        }
    }

    /**
     * Devuelve la conexión al pool para reutilizarla.
     * Si la conexión era temporal (excedía el POOL_SIZE), se cierra de forma segura.
     */
    public void releaseConnection(Connection con) {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    // offer() retorna false si la cola (pool) está llena
                    if (!pool.offer(con)) {
                        con.close(); // Si el pool está lleno, es una conexión temporal. La cerramos.
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar o liberar conexión: " + e.getMessage());
            }
        }
    }

    private static Connection crearNuevaConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static void desconectar() {
        for (Connection c : pool) {
            try { 
                if (c != null && !c.isClosed()) {
                    c.close(); 
                }
            } catch (SQLException ignored) {}
        }
        pool.clear();
    }
}
