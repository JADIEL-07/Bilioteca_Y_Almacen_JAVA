package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Pool de conexiones liviano para evitar crear una nueva conexión en cada query.
 * Tamaño del pool: 5 conexiones reutilizables.
 */
public class ConexionBD {

    private static final String URL     = "jdbc:postgresql://194.163.142.34:5437/biblioteca_db";
    private static final String USUARIO = "Jadiel_Zz";
    private static final String CLAVE   = "12872Jadiel#";

    private static final int POOL_SIZE = 5;
    private static final BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(POOL_SIZE);

    // Instancia singleton
    private static final ConexionBD INSTANCE = new ConexionBD();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                pool.offer(crearNuevaConexion());
            }
            System.out.println("Pool de " + POOL_SIZE + " conexiones listo.");
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
     * Obtiene una conexión del pool. Si el pool está vacío crea una nueva temporal.
     * Siempre devuelve la conexión con releaseConnection() en el bloque finally.
     */
    public Connection getConnection() throws SQLException {
        Connection con = pool.poll();
        if (con == null || con.isClosed()) {
            con = crearNuevaConexion();
        } else {
            try {
                if (!con.isValid(1)) {
                    con = crearNuevaConexion();
                }
            } catch (SQLException e) {
                con = crearNuevaConexion();
            }
        }
        return con;
    }

    /**
     * Devuelve la conexión al pool para reutilizarla.
     * Llama a este método en el bloque finally después de usar getConnection().
     */
    public void releaseConnection(Connection con) {
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    pool.offer(con);
                }
            } catch (SQLException e) {
                // Descartar conexión inválida
            }
        }
    }

    private static Connection crearNuevaConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }

    public static void desconectar() {
        for (Connection c : pool) {
            try { if (c != null && !c.isClosed()) c.close(); } catch (SQLException ignored) {}
        }
        pool.clear();
    }
}
