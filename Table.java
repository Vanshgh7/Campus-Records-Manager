import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * Utility class to convert a ResultSet into a TableModel for JTable display.
 */
public class TableUtil {

    /**
     * Builds a DefaultTableModel from a SQL ResultSet.
     *
     * @param rs the ResultSet containing query results
     * @return DefaultTableModel suitable for displaying in JTable
     * @throws SQLException if a database access error occurs
     */
    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Extract column names
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Extract row data
        Vector<Vector<Object>> rowData = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            rowData.add(row);
        }

        return new DefaultTableModel(rowData, columnNames);
    }
}
