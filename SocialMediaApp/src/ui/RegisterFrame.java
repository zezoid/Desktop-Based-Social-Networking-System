package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public RegisterFrame() {

        setTitle("SocialApp · Register");
        setSize(380, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
        card.setPreferredSize(new Dimension(320, 380));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtUser = new JTextField();
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));

        JTextField txtEmail = new JTextField();
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton btnRegister = new JButton("Sign Up");
        btnRegister.setBackground(new Color(0, 149, 246));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton btnBack = new JButton("Back to login");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(new Color(0, 149, 246));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegister.addActionListener(_ -> {

            if (txtUser.getText().isBlank()
                    || txtEmail.getText().isBlank()
                    || txtPass.getPassword().length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "All fields are required",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean ok = new UserDAO().register(
                    txtUser.getText().trim(),
                    txtEmail.getText().trim(),
                    new String(txtPass.getPassword())
            );

            if (ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Username or email already exists",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBack.addActionListener(_ -> {
            dispose();
            new LoginFrame();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(txtUser);
        card.add(Box.createVerticalStrut(12));
        card.add(txtEmail);
        card.add(Box.createVerticalStrut(12));
        card.add(txtPass);
        card.add(Box.createVerticalStrut(18));
        card.add(btnRegister);
        card.add(Box.createVerticalStrut(14));
        card.add(btnBack);

        add(card);
        setVisible(true);
    }
}
