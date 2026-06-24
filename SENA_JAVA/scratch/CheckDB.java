import java.sql.*;
import java.util.*;

public class CheckDB {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca_db", "postgres", "admin");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[] { "TABLE" });
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("TABLE: " + tableName);
                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    System.out.println("  - " + columns.getString("COLUMN_NAME") + " (" + columns.getString("TYPE_NAME") + ")");
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
