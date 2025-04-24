import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExpenceTrackerProject extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public ExpenceTrackerProject() {
        setTitle("Login Page");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        add(BorderLayout.CENTER, panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.equals("admin") && pass.equals("password")) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Login successful!");
            JOptionPane.showMessageDialog(this, "Welcome, " + user + "!");
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid username or password.");
        }
    }

    public static void main(String[] args) {
        new ExpenceTrackerProject();
    }
}

