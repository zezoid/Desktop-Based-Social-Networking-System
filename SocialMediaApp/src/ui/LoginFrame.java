package ui;

import dao.UserDAO;
import model.User;
import util.SchemaInitializer;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public LoginFrame() {

        setTitle("SocialApp · Login");
        setSize(380, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
        card.setPreferredSize(new Dimension(320, 340));

        JLabel title = new JLabel("SocialApp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtUser = new JTextField();
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));

        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setOpaque(false);
        showPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPass.addActionListener(e ->
                txtPass.setEchoChar(showPass.isSelected() ? (char) 0 : '•')
        );

        JButton btnLogin = new JButton("Log In");
        btnLogin.setBackground(new Color(0, 149, 246));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton btnRegister = new JButton("Create new account");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(new Color(0, 149, 246));
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(_ -> {
            User user = new UserDAO().login(
                    txtUser.getText().trim(),
                    new String(txtPass.getPassword())
            );

            if (user != null) {
                dispose();
                new DashboardFrame(user);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnRegister.addActionListener(_ -> {
            dispose();
            new RegisterFrame();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(22));
        card.add(txtUser);
        card.add(Box.createVerticalStrut(12));
        card.add(txtPass);
        card.add(showPass);
        card.add(Box.createVerticalStrut(16));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(14));
        card.add(btnRegister);

        add(card);
        setVisible(true);
    }

    public static void main(String[] args) {
        SchemaInitializer.createTables();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
