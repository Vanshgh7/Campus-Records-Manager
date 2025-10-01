import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class StudentAppGUI extends JFrame implements ActionListener {

    // ---------------- GUI Components ----------------
    private final JLabel lblStudentId, lblFirstName, lblLastName, lblMajor, lblPhone, lblGPA, lblDOB;
    private final JTextField txtStudentId, txtFirstName, txtLastName, txtMajor, txtPhone, txtGPA, txtDOB;
    private final JButton btnAdd, btnDisplay, btnSort, btnSearch, btnModify;

    private Statement stmt;

    public StudentAppGUI() {
        JFrame frame = new JFrame("Student Database Management");
        JPanel panel = new JPanel();

        // Initialize labels
        lblStudentId = new JLabel("Student ID:");
        lblFirstName = new JLabel("First Name:");
        lblLastName = new JLabel("Last Name:");
        lblMajor = new JLabel("Major:");
        lblPhone = new JLabel("Phone:");
        lblGPA = new JLabel("GPA:");
        lblDOB = new JLabel("Date of Birth (yyyy-mm-dd):");

        // Initialize text fields
        txtStudentId = new JTextField(10);
        txtFirstName = new JTextField(10);
        txtLastName = new JTextField(10);
        txtMajor = new JTextField(10);
        txtPhone = new JTextField(10);
        txtGPA = new JTextField(10);
        txtDOB = new JTextField(10);

        // Initialize buttons
        btnAdd = new JButton("Add");
        btnDisplay = new JButton("Display");
        btnSort = new JButton("Sort");
        btnSearch = new JButton("Search");
        btnModify = new JButton("Modify");

        // Add action listeners
        btnAdd.addActionListener(this);
        btnDisplay.addActionListener(this);
        btnSort.addActionListener(this);
        btnSearch.addActionListener(this);
        btnModify.addActionListener(this);

        // Add components to panel
        panel.add(lblStudentId);
        panel.add(txtStudentId);
        panel.add(lblFirstName);
        panel.add(txtFirstName);
        panel.add(lblLastName);
        panel.add(txtLastName);
        panel.add(lblMajor);
        panel.add(txtMajor);
        panel.add(lblPhone);
        panel.add(txtPhone);
        panel.add(lblGPA);
        panel.add(txtGPA);
        panel.add(lblDOB);
        panel.add(txtDOB);

        panel.add(btnAdd);
        panel.add(btnDisplay);
        panel.add(btnSort);
        panel.add(btnSearch);
        panel.add(btnModify);

        // Final frame settings
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Establish database connection
        dbConnect db = new dbConnect();
        Connection conn;
        try {
            conn = db.getConnection();
            stmt = conn.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Database connection failed!");
            return;
        }

        Table tableUtil = new Table();

        // ---------------- ADD STUDENT ----------------
        if (e.getSource() == btnAdd) {
            String sql = "INSERT INTO sdata VALUES('" + txtStudentId.getText() + "', '"
                    + txtFirstName.getText() + "', '" + txtLastName.getText() + "', '" + txtMajor.getText()
                    + "', '" + txtPhone.getText() + "', '" + txtGPA.getText() + "', '" + txtDOB.getText() + "')";
            try {
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Student added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Failed to add student. Please check inputs.");
            }
        }

        // ---------------- DISPLAY STUDENTS ----------------
        else if (e.getSource() == btnDisplay) {
            displayTable("SELECT * FROM sdata", tableUtil);
        }

        // ---------------- SORT STUDENTS ----------------
        else if (e.getSource() == btnSort) {
            String[] options = {"First Name", "Last Name", "Major"};
            int choice = JOptionPane.showOptionDialog(null, "Sort by:", "Sort", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            String sql = switch (choice) {
                case 0 -> "SELECT * FROM sdata ORDER BY first_name";
                case 1 -> "SELECT * FROM sdata ORDER BY last_name";
                case 2 -> "SELECT * FROM sdata ORDER BY major";
                default -> "";
            };
            displayTable(sql, tableUtil);
        }

        // ---------------- SEARCH STUDENTS ----------------
        else if (e.getSource() == btnSearch) {
            String[] options = {"Student ID", "Last Name", "Major"};
            int choice = JOptionPane.showOptionDialog(null, "Search by:", "Search", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            String column = switch (choice) {
                case 0 -> "student_id";
                case 1 -> "last_name";
                case 2 -> "major";
                default -> "";
            };

            String term = JOptionPane.showInputDialog("Enter search term:");
            displayTable("SELECT * FROM sdata WHERE " + column + " LIKE '%" + term + "%'", tableUtil);
        }

        // ---------------- MODIFY STUDENT ----------------
        else if (e.getSource() == btnModify) {
            String studentId = JOptionPane.showInputDialog("Enter student ID:");
            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM sdata WHERE student_id = '" + studentId + "'");
                if (rs.next()) {
                    String[] fields = {"First Name", "Last Name", "Major", "Phone", "GPA", "Date of Birth"};
                    int fieldChoice = JOptionPane.showOptionDialog(null, "Select field to modify:", "Modify",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, fields, fields[0]);

                    String column = switch (fieldChoice) {
                        case 0 -> "first_name";
                        case 1 -> "last_name";
                        case 2 -> "major";
                        case 3 -> "phone";
                        case 4 -> "gpa";
                        case 5 -> "date_of_birth";
                        default -> "";
                    };

                    String newValue = JOptionPane.showInputDialog("Enter new value:");
                    stmt.executeUpdate("UPDATE sdata SET " + column + " = '" + newValue + "' WHERE student_id = '" + studentId + "'");
                    JOptionPane.showMessageDialog(null, "Student data updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Student not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ---------------- HELPER METHOD TO DISPLAY TABLE ----------------
    private void displayTable(String sqlQuery, Table tableUtil) {
        try {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            JTable table = new JTable(tableUtil.buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching data.");
        }
    }
}
