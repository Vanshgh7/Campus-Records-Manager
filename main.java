import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Launch the Student Management GUI
        SwingUtilities.invokeLater(() -> {
            StudentAppGUI appWindow = new StudentAppGUI();
            appWindow.pack();
            appWindow.setVisible(true);
            appWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
