package ui;

import dao.ProfileDAO;
import model.UserProfile;

import javax.swing.*;

public class ProfileFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public ProfileFrame(int userId) {

        UserProfile p = new ProfileDAO().getProfile(userId);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        area.setText(
                "Username: " + p.username + "\n" +
                "Email: " + p.email + "\n\n" +
                "Posts: " + p.posts + "\n" +
                "Followers: " + p.followers + "\n" +
                "Following: " + p.following
        );

        add(new JScrollPane(area));
        setTitle("Profile");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
