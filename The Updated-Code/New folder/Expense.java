package ExpenceTracker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
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

        // Welcome Panel with Static Text
        JPanel welcomePanel = new JPanel();
        welcomePanel.setPreferredSize(new Dimension(1000, 50));
        welcomePanel.setBackground(new Color(30, 144, 255)); // Dodger Blue
        JLabel welcomeLabel = new JLabel("Track Your Spending with Ease!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);

        add(welcomePanel, BorderLayout.NORTH);

        // Main Card Panel
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        cardPanel.setPreferredSize(new Dimension(1000, 700));
        cardPanel.setBackground(new Color(245, 245, 245)); // Light Gray

        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Custom Panel with Gradient Background
    class GradientPanel extends JPanel {
        public GradientPanel() {
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, new Color(135, 206, 250), 0, getHeight(), new Color(240, 248, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JPanel createLoginPanel() {
        GradientPanel panel = new GradientPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Mobile Number
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mobileLabel.setForeground(new Color(25, 25, 112));
        panel.add(mobileLabel, gbc);

        mobileField = new JTextField(20);
        mobileField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mobileField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        mobileField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(mobileField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(25, 25, 112));
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        passwordField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        loginButton = createStyledButton("Login", new Color(30, 144, 255));
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        registerButton = createStyledButton("Register", new Color(46, 139, 87));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "register"));
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(220, 20, 60));
        gbc.gridy = 4;
        panel.add(messageLabel, gbc);

        // Info Text Area
        infoTextArea = new JTextArea(6, 32);
        infoTextArea.setText("Login: Enter your mobile number and password to access the system.\n\n"
                + "Register: Create a new account by entering your name, mobile number, and password.\n\n"
                + "Ensure your mobile number is unique and your password is secure.");
        infoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoTextArea.setEditable(false);
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setBackground(new Color(245, 245, 245));
        infoTextArea.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Custom ScrollBar UI
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(30, 144, 255);
                this.trackColor = new Color(245, 245, 245);
                this.thumbHighlightColor = new Color(66, 165, 245);
                this.thumbDarkShadowColor = new Color(30, 144, 255);
                this.thumbLightShadowColor = new Color(30, 144, 255);
                this.trackHighlightColor = new Color(245, 245, 245);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        gbc.gridy = 5;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        GradientPanel panel = new GradientPanel();
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Name
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameLabel.setForeground(new Color(25, 25, 112));
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        nameField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Mobile Number
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mobileLabel.setForeground(new Color(25, 25, 112));
        panel.add(mobileLabel, gbc);

        registerMobileField = new JTextField(20);
        registerMobileField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        registerMobileField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        registerMobileField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(registerMobileField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(25, 25, 112));
        panel.add(passwordLabel, gbc);

        registerPasswordField = new JPasswordField(20);
        registerPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        registerPasswordField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        registerPasswordField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(registerPasswordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmLabel.setForeground(new Color(25, 25, 112));
        panel.add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmPasswordField.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));
        confirmPasswordField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        submitButton = createStyledButton("Submit", new Color(30, 144, 255));
        submitButton.addActionListener(e -> handleRegistration(
                nameField.getText(),
                registerMobileField.getText(),
                new String(registerPasswordField.getPassword()),
                new String(confirmPasswordField.getPassword())
        ));
        buttonPanel.add(submitButton);

        cancelButton = createStyledButton("Cancel", new Color(169, 169, 169));
        cancelButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Message Label
        registerMessageLabel = new JLabel("");
        registerMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerMessageLabel.setForeground(new Color(220, 20, 60));
        gbc.gridy = 6;
        panel.add(registerMessageLabel, gbc);

        // Info Text Area
        JTextArea registerInfoArea = new JTextArea(5, 32);
        registerInfoArea.setText("Registration Info:\n"
                + "Enter your name, mobile number, and a secure password.\n"
                + "Ensure passwords match exactly.\n\n"
                + "Your mobile number must be unique.");
        registerInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerInfoArea.setEditable(false);
        registerInfoArea.setLineWrap(true);
        registerInfoArea.setWrapStyleWord(true);
        registerInfoArea.setBackground(new Color(245, 245, 245));
        registerInfoArea.setBorder(new LineBorder(new Color(135, 206, 250), 1, true));

        JScrollPane scrollPane = new JScrollPane(registerInfoArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Custom ScrollBar UI
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(30, 144, 255);
                this.trackColor = new Color(245, 245, 245);
                this.thumbHighlightColor = new Color(66, 165, 245);
                this.thumbDarkShadowColor = new Color(30, 144, 255);
                this.thumbLightShadowColor = new Color(30, 144, 255);
                this.trackHighlightColor = new Color(245, 245, 245);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        gbc.gridy = 7;
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new LineBorder(bgColor, 1, true));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));

        // Hover Effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void handleRegistration(String name, String mobile, String password, String confirmPassword) {
        if (name.isEmpty() || mobile.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            registerMessageLabel.setForeground(new Color(220, 20, 60));
            registerMessageLabel.setText("Registration failed. Check all fields.");
        } else {
            String hashedPassword = hashPassword(password);
            try (Connection conn = DBConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM users WHERE mobile = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, mobile);
                    ResultSet resultSet = checkStmt.executeQuery();
                    resultSet.next();
                    if (resultSet.getInt(1) > 0) {
                        registerMessageLabel.setForeground(new Color(220, 20, 60));
                        registerMessageLabel.setText("Mobile number already registered.");
                        return;
                    }
                }

                String insertQuery = "INSERT INTO users (name, mobile, password) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, mobile);
                    insertStmt.setString(3, hashedPassword);
                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        registerMessageLabel.setForeground(new Color(34, 139, 34));
                        registerMessageLabel.setText("Registration successful.");
                        JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
                        cardLayout.show(cardPanel, "login");
                    } else {
                        registerMessageLabel.setForeground(new Color(220, 20, 60));
                        registerMessageLabel.setText("Error saving to database.");
                    }
                }
            } catch (SQLException e) {
                registerMessageLabel.setForeground(new Color(220, 20, 60));
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
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String mobile = mobileField.getText();
            String password = new String(passwordField.getPassword());
            try (Connection conn = DBConnection.getConnection()) {
                String loginQuery = "SELECT * FROM users WHERE mobile = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(loginQuery)) {
                    stmt.setString(1, mobile);
                    stmt.setString(2, hashPassword(password));
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        String mobileNumber = resultSet.getString("mobile");
                        messageLabel.setForeground(new Color(34, 139, 34));
                        messageLabel.setText("Login successful!");
                        openDashboard(mobileNumber); // Pass mobile number
                    } else {
                        messageLabel.setForeground(new Color(220, 20, 60));
                        messageLabel.setText("Invalid mobile or password.");
                    }
                }
            } catch (SQLException ex) {
                messageLabel.setForeground(new Color(220, 20, 60));
                messageLabel.setText("Database error: " + ex.getMessage());
            }
        }
    }

    private JTextArea summaryTextArea;
    private JTextField budgetField;

    public void openDashboard(String mobile) {
        // Retrieve the user's name for display
        String userName = mobile; // Default to mobile if name retrieval fails
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT name FROM users WHERE mobile = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, mobile);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    userName = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching user name: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JFrame dashboardFrame = new JFrame("Expense Tracker Dashboard");
        dashboardFrame.setSize(1000, 700);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setLayout(new BorderLayout());
        dashboardFrame.getContentPane().setBackground(new Color(245, 245, 245));

        // Header with ScrollingBanner
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel welcomeLabel = new JLabel("Welcome, " + userName + "! Expense Management Made Easy");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        dashboardFrame.add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JLabel appLabel = new JLabel("Expense Tracker");
        appLabel.setForeground(Color.WHITE);
        appLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(appLabel);
        sidebar.add(Box.createVerticalStrut(20));

        String[] menuItems = {"Add Expense", "View/Search", "Monthly Summary", "Reports", "Logout"};
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(245, 245, 245));

        // Pass mobile as username to all panels
        JPanel addExpensePanel = createAddExpensePanel(mobile);
        JPanel viewExpensesPanel = createViewExpensesPanel(mobile);
        JPanel summaryPanel = createSummaryPanel(mobile);
        JPanel reportPanel = createReportPanel(mobile);

        contentPanel.add(addExpensePanel, "Add Expense");
        contentPanel.add(viewExpensesPanel, "View/Search");
        contentPanel.add(summaryPanel, "Monthly Summary");
        contentPanel.add(reportPanel, "Reports");

        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setFocusPainted(false);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            button.setBackground(new Color(30, 30, 30));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            button.setOpaque(true);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(100, 100, 100));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(30, 30, 30));
                }
            });

            button.addActionListener(e -> {
                if (item.equals("Logout")) {
                    dashboardFrame.dispose();
                } else {
                    CardLayout cl = (CardLayout) (contentPanel.getLayout());
                    cl.show(contentPanel, item);
                }
            });

            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(10));
        }

        dashboardFrame.add(sidebar, BorderLayout.WEST);
        dashboardFrame.add(contentPanel, BorderLayout.CENTER);
        dashboardFrame.setVisible(true);
    }

    private JPanel createAddExpensePanel(String username) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Color fieldColor = new Color(255, 255, 255);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        JTextField dateField = new JTextField(15);
        dateField.setBackground(fieldColor);
        dateField.setFont(fieldFont);
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JTextField categoryField = new JTextField(15);
        categoryField.setBackground(fieldColor);
        categoryField.setFont(fieldFont);
        categoryField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JTextField amountField = new JTextField(15);
        amountField.setBackground(fieldColor);
        amountField.setFont(fieldFont);
        amountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JTextField descriptionField = new JTextField(15);
        descriptionField.setBackground(fieldColor);
        descriptionField.setFont(fieldFont);
        descriptionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton addButton = new JButton("Add Expense");
        addButton.setBackground(new Color(33, 150, 243));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setOpaque(true);
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                addButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                addButton.setBackground(new Color(33, 150, 243));
            }
        });

        JLabel dateLabel = new JLabel("Date (DD-MM-YYYY):");
        dateLabel.setFont(labelFont);
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(labelFont);
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(labelFont);
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(labelFont);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(dateLabel, gbc);
        gbc.gridx = 1; panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(categoryLabel, gbc);
        gbc.gridx = 1; panel.add(categoryField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(amountLabel, gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(descriptionLabel, gbc);
        gbc.gridx = 1; panel.add(descriptionField, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(addButton, gbc);

        addButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String category = categoryField.getText().trim();
            String amountText = amountField.getText().trim();
            String description = descriptionField.getText().trim();

            if (date.isEmpty() || category.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String formattedDate;
            try {
                if (date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    String[] dateParts = date.split("/");
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);
                    formattedDate = String.format("%04d-%02d-%02d", year, month, day);
                } else if (date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                    formattedDate = date;
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid date format. Use YYYY-MM-DD or DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Invalid date format. Use YYYY-MM-DD or DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(panel, "Amount must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String query = "INSERT INTO expenses (date, category, amount, description, username) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, formattedDate);
                    stmt.setString(2, category);
                    stmt.setDouble(3, amount);
                    stmt.setString(4, description);
                    stmt.setString(5, username);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(panel, "Expense added successfully!");
                    dateField.setText("");
                    categoryField.setText("");
                    amountField.setText("");
                    descriptionField.setText("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void searchExpenses(String username, DefaultTableModel tableModel, String keyword) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM expenses WHERE username = ? AND (category LIKE ? OR description LIKE ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, "%" + keyword + "%");
                stmt.setString(3, "%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();
                tableModel.setRowCount(0);
                while (rs.next()) {
                    String date = rs.getString("date");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{date, category, amount, description});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createViewExpensesPanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Date", "Category", "Amount", "Description"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(25);
        expenseTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        expenseTable.setGridColor(new Color(200, 200, 200));

        JScrollPane tableScrollPane = new JScrollPane(expenseTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(33, 150, 243));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(new Color(33, 150, 243));
            }
        });
        searchButton.addActionListener(e -> searchExpenses(username, tableModel, searchField.getText()));

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        panel.add(topPanel, BorderLayout.NORTH);

        JButton loadExpensesButton = new JButton("Load All Expenses");
        loadExpensesButton.setBackground(new Color(33, 150, 243));
        loadExpensesButton.setForeground(Color.WHITE);
        loadExpensesButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadExpensesButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        loadExpensesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadExpensesButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadExpensesButton.setBackground(new Color(33, 150, 243));
            }
        });
        loadExpensesButton.addActionListener(e -> {
            tableModel.setRowCount(0);
            loadExpenses(username, tableModel);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(loadExpensesButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadExpenses(String username, DefaultTableModel tableModel) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM expenses WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                tableModel.setRowCount(0);
                while (rs.next()) {
                    String date = rs.getString("date");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    String description = rs.getString("description");
                    tableModel.addRow(new Object[]{date, category, amount, description});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createSummaryPanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        budgetField = new JTextField(10);
        budgetField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        budgetField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Load budget from database
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT budget FROM budgets WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    budgetField.setText(String.valueOf(rs.getDouble("budget")));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching budget: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        JButton setBudgetButton = new JButton("Set Budget");
        setBudgetButton.setBackground(new Color(33, 150, 243));
        setBudgetButton.setForeground(Color.WHITE);
        setBudgetButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBudgetButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        setBudgetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBudgetButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBudgetButton.setBackground(new Color(33, 150, 243));
            }
        });
        setBudgetButton.addActionListener(e -> {
            String budgetStr = budgetField.getText().trim();
            try {
                double budget = Double.parseDouble(budgetStr);
                if (budget <= 0) {
                    JOptionPane.showMessageDialog(panel, "Budget must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection conn = DBConnection.getConnection()) {
                    String query = "INSERT INTO budgets (username, budget) VALUES (?, ?) ON DUPLICATE KEY UPDATE budget = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, username);
                        stmt.setDouble(2, budget);
                        stmt.setDouble(3, budget);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(panel, "Budget set successfully!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error setting budget: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid budget. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton summaryButton = new JButton("View Monthly Summary");
        summaryButton.setBackground(new Color(33, 150, 243));
        summaryButton.setForeground(Color.WHITE);
        summaryButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        summaryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                summaryButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                summaryButton.setBackground(new Color(33, 150, 243));
            }
        });
        summaryButton.addActionListener(e -> showMonthlySummary(username));

        topPanel.add(new JLabel("Monthly Budget: ₹"));
        topPanel.add(budgetField);
        topPanel.add(setBudgetButton);
        topPanel.add(summaryButton);
        panel.add(topPanel, BorderLayout.NORTH);

        summaryTextArea = new JTextArea(10, 40);
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summaryTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane scrollPane = new JScrollPane(summaryTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showMonthlySummary(String username) {
        double totalSpent = 0;
        StringBuilder summary = new StringBuilder("Monthly Summary:\n\n");

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch expenses
            String query = "SELECT SUM(amount) AS totalSpent, category FROM expenses WHERE username = ? AND MONTH(date) = MONTH(CURRENT_DATE()) GROUP BY category";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String category = rs.getString("category");
                    double amount = rs.getDouble("totalSpent");
                    summary.append(category).append(": ₹").append(String.format("%.2f", amount)).append("\n");
                    totalSpent += amount;
                }
            }

            // Fetch budget
            double budget = 0;
            String budgetQuery = "SELECT budget FROM budgets WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(budgetQuery)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    budget = rs.getDouble("budget");
                }
            }

            summary.append("\nTotal Spent: ₹").append(String.format("%.2f", totalSpent));

            if (budget > 0) {
                double remaining = budget - totalSpent;
                summary.append("\nBudget: ₹").append(String.format("%.2f", budget))
                       .append("\nRemaining: ₹").append(String.format("%s%.2f", remaining < 0 ? "-" : "", Math.abs(remaining)));
                if (remaining < 0) {
                    summary.append("\nWarning: You have overspent your budget!");
                }
            } else {
                summary.append("\n(No budget set)");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        summaryTextArea.setText(summary.toString());
    }

    private JPanel createReportPanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 245));
        JLabel reportTypeLabel = new JLabel("Select Report Type:");
        reportTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] reportTypes = {"Monthly", "Weekly", "Yearly", "Daily"};
        JComboBox<String> reportTypeComboBox = new JComboBox<>(reportTypes);
        reportTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportTypeComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JTextArea reportArea = new JTextArea(15, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(new Color(33, 150, 243));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        generateButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        generateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                generateButton.setBackground(new Color(66, 165, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                generateButton.setBackground(new Color(33, 150, 243));
            }
        });
        generateButton.addActionListener(e -> generateReport(username, reportTypeComboBox.getSelectedItem().toString(), reportArea));

        topPanel.add(reportTypeLabel);
        topPanel.add(reportTypeComboBox);
        topPanel.add(generateButton);

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void generateReport(String username, String reportType, JTextArea reportArea) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "";
            String groupByField = "";

            switch (reportType) {
                case "Monthly":
                    query = "SELECT DATE_FORMAT(date, '%Y-%m') AS period, date, category, amount, description " +
                            "FROM expenses WHERE username = ? ORDER BY period, date";
                    groupByField = "period";
                    break;
                case "Weekly":
                    query = "SELECT CONCAT(YEAR(date), '-Week ', WEEK(date)) AS period, date, category, amount, description " +
                            "FROM expenses WHERE username = ? ORDER BY YEAR(date), WEEK(date), date";
                    groupByField = "period";
                    break;
                case "Yearly":
                    query = "SELECT YEAR(date) AS period, date, category, amount, description " +
                            "FROM expenses WHERE username = ? ORDER BY period, date";
                    groupByField = "period";
                    break;
                case "Daily":
                    query = "SELECT DATE(date) AS period, date, category, amount, description " +
                            "FROM expenses WHERE username = ? ORDER BY period, date";
                    groupByField = "period";
                    break;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                StringBuilder report = new StringBuilder(reportType + " Report:\n\n");
                String currentPeriod = null;
                double periodTotal = 0.0;
                boolean hasExpenses = false;

                while (rs.next()) {
                    String period = rs.getString("period");
                    String date = rs.getString("date");
                    String category = rs.getString("category");
                    double amount = rs.getDouble("amount");
                    String description = rs.getString("description") != null ? rs.getString("description") : "No description";

                    if (currentPeriod == null || !period.equals(currentPeriod)) {
                        if (currentPeriod != null) {
                            report.append("Total: ₹").append(String.format("%.2f", periodTotal)).append("\n\n");
                        }
                        currentPeriod = period;
                        periodTotal = 0.0;
                        report.append(reportType).append(": ").append(period).append("\n");
                        report.append("----------------------------------------\n");
                    }

                    periodTotal += amount;
                    hasExpenses = true;
                    report.append("Date: ").append(date)
                          .append(" | Category: ").append(category)
                          .append(" | Amount: ₹").append(String.format("%.2f", amount))
                          .append(" | Description: ").append(description)
                          .append("\n");
                }

                if (hasExpenses) {
                    report.append("Total: ₹").append(String.format("%.2f", periodTotal)).append("\n");
                } else {
                    report.append("No expenses found for this report type.\n");
                }

                reportArea.setText(report.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     
    
    class ScrollingBanner extends JPanel {
        private String message;
        private int x;
        private Timer timer;

        public ScrollingBanner(String message) {
            this.message = message;
            this.x = 800;
            setPreferredSize(new Dimension(800, 40));
            setBackground(new Color(230, 240, 255));
            timer = new Timer(20, e -> {
                x -= 2;
                if (x + getFontMetrics(getFont()).stringWidth(message) < 0) {
                    x = getWidth();
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0, 102, 204));
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString(message, x, 25);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenceTrackerProject());
    }
}
