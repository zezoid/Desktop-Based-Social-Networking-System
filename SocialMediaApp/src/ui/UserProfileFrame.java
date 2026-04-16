package ui;

import dao.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class UserProfileFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int me;
    private final int profileUserId;

    public UserProfileFrame(int me, int profileUserId) {

        this.me = me;
        this.profileUserId = profileUserId;

        ProfileDAO profileDAO = new ProfileDAO();
        FollowDAO followDAO = new FollowDAO();
        PostDAO postDAO = new PostDAO();
        LikeDAO likeDAO = new LikeDAO();
        CommentDAO commentDAO = new CommentDAO();

        UserProfile p = profileDAO.getProfile(profileUserId);
        boolean following = profileDAO.isFollowing(me, profileUserId);

        setTitle(p.username + "'s Profile");
        setSize(420, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /* ================= HEADER ================= */
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblUser = new JLabel(p.username);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel stats = new JLabel(
                "Posts: " + p.posts +
                "   Followers: " + p.followers +
                "   Following: " + p.following
        );

        JButton btnFollow = new JButton(following ? "Unfollow" : "Follow");
        if (me == profileUserId) btnFollow.setVisible(false);

        btnFollow.addActionListener(_ -> {
            if (following) {
                followDAO.unfollow(me, profileUserId);
            } else {
                followDAO.follow(me, profileUserId);
            }
            dispose();
            new UserProfileFrame(me, profileUserId);
        });
        JButton btnMessage = new JButton("Message");
        btnMessage.addActionListener(_ ->
        new ChatFrame(me, profileUserId, p.username)
    );

        header.add(lblUser);
        header.add(Box.createVerticalStrut(6));
        header.add(stats);
        header.add(Box.createVerticalStrut(8));
        header.add(btnFollow);
        header.add(btnMessage);
        if (me == profileUserId) btnMessage.setVisible(false);
        

        add(header, BorderLayout.NORTH);

        /* ================= POSTS GRID ================= */
        JPanel grid = new JPanel(new GridLayout(0, 3, 8, 8));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        for (Post post : postDAO.getUserPosts(profileUserId)) {

            JPanel cell = new JPanel(new BorderLayout());
            cell.setPreferredSize(new Dimension(120, 120));
            cell.setBackground(Color.LIGHT_GRAY);
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            /* ---- IMAGE ---- */
            if (post.hasMedia() && post.isImage()) {
                ImageIcon icon = new ImageIcon(post.getMediaPath());
                Image img = icon.getImage().getScaledInstance(
                        120, 120, Image.SCALE_SMOOTH
                );
                cell.add(new JLabel(new ImageIcon(img)), BorderLayout.CENTER);
            }
            /* ---- VIDEO ---- */
            else if (post.hasMedia() && post.isVideo()) {
                JLabel video = new JLabel("▶", JLabel.CENTER);
                video.setFont(new Font("Segoe UI", Font.BOLD, 36));
                cell.add(video, BorderLayout.CENTER);
            }
            /* ---- TEXT ---- */
            else {
                JLabel text = new JLabel(
                        "<html><center>" + post.getContent() + "</center></html>",
                        JLabel.CENTER
                );
                cell.add(text, BorderLayout.CENTER);
            }

            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    openPostDialog(post, likeDAO, commentDAO);
                }
            });

            grid.add(cell);
        }

        setVisible(true);
    }

    /* ================= POST VIEWER ================= */
    private void openPostDialog(Post post, LikeDAO likeDAO, CommentDAO commentDAO) {

        JDialog d = new JDialog(this, true);
        d.setSize(420, 500);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (post.hasMedia() && post.isImage()) {
            ImageIcon icon = new ImageIcon(post.getMediaPath());
            Image img = icon.getImage().getScaledInstance(
                    380, -1, Image.SCALE_SMOOTH
            );
            content.add(new JLabel(new ImageIcon(img)));
        }

        if (post.getContent() != null && !post.getContent().isBlank()) {
            JTextArea text = new JTextArea(post.getContent());
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEditable(false);
            content.add(Box.createVerticalStrut(6));
            content.add(text);
        }

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(new JLabel("❤ " + likeDAO.getLikeCount(post.getId())));
        actions.add(new JLabel("💬 " + commentDAO.getCommentCount(post.getId())));

        content.add(Box.createVerticalStrut(6));
        content.add(actions);

        JButton commentsBtn = new JButton("View Comments");
        commentsBtn.addActionListener(_ -> showComments(post.getId()));

        d.add(new JScrollPane(content), BorderLayout.CENTER);
        d.add(commentsBtn, BorderLayout.SOUTH);

        d.setVisible(true);
    }

    /* ================= COMMENTS ================= */
    private void showComments(int postId) {

        CommentDAO dao = new CommentDAO();

        JDialog d = new JDialog(this, "Comments", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Comment c : dao.getCommentsWithOwner(postId)) {

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lbl = new JLabel(c.getUsername() + ": " + c.getText());
            row.add(lbl);

            // ✅ comment owner OR post owner can delete
            if (c.getUserId() == me || profileUserId == me) {
                JButton del = new JButton("🗑️");
                del.setBorderPainted(false);
                del.setContentAreaFilled(false);
                del.addActionListener(_ -> {
                    dao.deleteComment(c.getCommentId(), me);
                    d.dispose();
                    showComments(postId);
                });
                row.add(del);
            }

            panel.add(row);
        }

        d.add(new JScrollPane(panel));
        d.setVisible(true);
    }
}
