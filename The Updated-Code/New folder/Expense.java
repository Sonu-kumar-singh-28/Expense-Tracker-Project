package ExpenceTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExpenceTrackerProject extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField mobileField, registerMobileField, nameField;
    private JPasswordField passwordField, registerPasswordField, confirmPasswordField;
    private JButton loginButton, registerButton, submitButton, cancelButton;
    private JLabel messageLabel, registerMessageLabel;
    private JTextArea infoTextArea;
    private JPanel cardPanel, loginPanel, registerPanel;
    private CardLayout cardLayout;

    public ExpenceTrackerProject() {
        setTitle("Expense Tracker");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel welcomePanel = new JPanel() {
            int x = 1000;
            Timer timer;

            {
                setPreferredSize(new Dimension(1000, 40));
                setBackground(new Color(230, 240, 255));
                timer = new Timer(20, e -> {
                    x -= 2;
                    if (x + getFontMetrics(getFont()).stringWidth("Track Your Spending with Ease!") < 0) {
                        x = getWidth();
                    }
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(75, 0, 130));
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Track Your Spending with Ease!", x, 25);
            }
        };

        add(welcomePanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(registerPanel, "Register");

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                System.out.println("Background image not found. Using solid color.");
            }
            setLayout(new GridBagLayout());
            setBackground(new Color(240, 248, 255));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new BackgroundPanel("/images/login_bg.jpg");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Mobile Number:"), gbc);

        mobileField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(mobileField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(148, 0, 211));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 3;
        panel.add(messageLabel, gbc);

        infoTextArea = new JTextArea(6, 32);
        infoTextArea.setText("Login: Enter your mobile number and password to access the system.\n\n"
                + "Register: Create a new account by entering your name, mobile number and password.");
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setForeground(new Color(0, 0, 128));

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(75, 0, 130);
                this.trackColor = new Color(230, 240, 255);
            }
        });

        gbc.gridy = 4;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 248, 255));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Mobile Number:"), gbc);

        registerMobileField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(registerMobileField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);

        registerPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(registerPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Confirm Password:"), gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(panel.getBackground());

        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(46, 139, 87));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> handleRegistration(
                nameField.getText(),
                registerMobileField.getText(),
                new String(registerPasswordField.getPassword()),
                new String(confirmPasswordField.getPassword())
        ));
        buttonPanel.add(submitButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(169, 169, 169));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        registerMessageLabel = new JLabel("");
        registerMessageLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        panel.add(registerMessageLabel, gbc);

        JTextArea registerInfoArea = new JTextArea(5, 32);
        registerInfoArea.setText("Registration Info:\n"
                + "Enter your name, mobile number, and a secure password.\n"
                + "Ensure passwords match exactly.");
        registerInfoArea.setEditable(false);
        registerInfoArea.setLineWrap(true);
        registerInfoArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(registerInfoArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        gbc.gridy = 6;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private void handleRegistration(String name, String mobile, String password, String confirmPassword) {
        if (name.isEmpty() || mobile.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            registerMessageLabel.setForeground(Color.RED);
            registerMessageLabel.setText("Registration failed. Check all fields.");
        } else {
            // Hash password before saving it
            String hashedPassword = hashPassword(password);

            // Check if the mobile number already exists
            try (Connection conn = DBConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM users WHERE mobilenumber = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, mobile);
                    ResultSet resultSet = checkStmt.executeQuery();
                    resultSet.next();
                    if (resultSet.getInt(1) > 0) {
                        registerMessageLabel.setForeground(Color.RED);
                        registerMessageLabel.setText("Mobile number already registered.");
                        return;
                    }
                }

                // Insert new user into the database
                String insertQuery = "INSERT INTO users (name, mobilenumber, password) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, mobile);
                    insertStmt.setString(3, hashedPassword);

                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        registerMessageLabel.setForeground(Color.GREEN);
                        registerMessageLabel.setText("Registration successful.");

                        nameField.setText("");
                        registerMobileField.setText("");
                        registerPasswordField.setText("");
                        confirmPasswordField.setText("");

                        JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
                        cardLayout.show(cardPanel, "Login");
                    } else {
                        registerMessageLabel.setForeground(Color.RED);
                        registerMessageLabel.setText("Error saving to database.");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                registerMessageLabel.setForeground(Color.RED);
                registerMessageLabel.setText("Database error: " + e.getMessage());
            }
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String mobile = mobileField.getText();
            String password = new String(passwordField.getPassword());

            // Check credentials against the database
            try (Connection conn = DBConnection.getConnection()) {
                String loginQuery = "SELECT * FROM users WHERE mobilenumber = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(loginQuery)) {
                    stmt.setString(1, mobile);
                    stmt.setString(2, hashPassword(password)); // Compare hashed passwords

                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        messageLabel.setForeground(Color.GREEN);
                        messageLabel.setText("Login successful!");
                        openDashboard();
                    } else {
                        messageLabel.setForeground(Color.RED);
                        messageLabel.setText("Invalid mobile or password.");
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Database error: " + ex.getMessage());
            }
        }
    }

    private void openDashboard() {
        JFrame dashboardFrame = new JFrame("Expense Tracker Dashboard");
        dashboardFrame.setSize(800, 600);
        dashboardFrame.setLocationRelativeTo(this);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new ExpenceTrackerProject();
    }
}
// sirf login page and after successfull login dashboard 
