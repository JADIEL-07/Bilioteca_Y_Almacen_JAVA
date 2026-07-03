import modelo.ConexionBD;
import java.sql.Connection;
import java.sql.Statement;

public class OptimizarDB {
    public static void main(String[] args) {
        try {
            Connection con = ConexionBD.getInstance().getConnection();
            Statement stmt = con.createStatement();
             
            System.out.println("Creando índices en la base de datos remota...");
            
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_items_deleted ON items(is_deleted);");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_items_code ON items(code);");
            
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_loans_status ON loans(status);");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_loan_details_item ON loan_details(item_id);");
            
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_reservations_status ON reservations(status);");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_reservations_item ON reservations(item_id);");
            
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_maintenance_status ON maintenance(status);");
            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_maintenance_item ON maintenance(item_id);");

            System.out.println("Índices creados exitosamente!");
            
            stmt.close();
            ConexionBD.getInstance().releaseConnection(con);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
