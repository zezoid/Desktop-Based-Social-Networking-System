package ui;

import dao.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DashboardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final User user;

    /* ================= STATE ================= */
    private enum ViewMode { MOBILE, DESKTOP }
    private ViewMode viewMode = ViewMode.DESKTOP;

    private enum Theme { LIGHT, DARK }
    private Theme theme = Theme.LIGHT;

    /* ================= UI ================= */
    private JPanel topBar;
    private JPanel feedPanel;
    private JPanel wrapper;
    private JScrollPane scroll;

    /* ================= ICON HELPERS ================= */
    private JButton createTopIcon(String icon) {
        JButton b = new JButton(icon);
        Dimension size = new Dimension(44, 44);
        b.setPreferredSize(size);
        b.setMinimumSize(size);
        b.setMaximumSize(size);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        b.setMargin(new Insets(0,0,0,0));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton icon(String txt) {
        JButton b = new JButton(txt);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /* ================= CONSTRUCTOR ================= */
    public DashboardFrame(User user) {
        this.user = user;

        setTitle("SocialApp");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ================= TOP BAR ================= */
        topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("SocialApp");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actionBar = new JPanel();
        actionBar.setLayout(new BoxLayout(actionBar, BoxLayout.X_AXIS));
        actionBar.setOpaque(false);

        JButton btnPost    = createTopIcon("➕");
        JButton btnSearch  = createTopIcon("🔍");
        JButton btnProfile = createTopIcon("👤");
        JButton btnMode    = createTopIcon("📱");
        JButton btnTheme   = createTopIcon("🌙");
        JButton btnLogout  = createTopIcon("⏻");

        actionBar.add(btnPost);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(btnSearch);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(btnProfile);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(btnMode);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(btnTheme);
        actionBar.add(Box.createHorizontalStrut(10));
        actionBar.add(btnLogout);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(actionBar, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        /* ================= FEED ================= */
        feedPanel = new JPanel();
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));
        feedPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrapper = new JPanel(new GridBagLayout());
        wrapper.add(feedPanel);

        scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        /* ================= ACTIONS ================= */
        btnPost.addActionListener(_ -> createPost());
        btnSearch.addActionListener(_ -> searchUser());
        btnProfile.addActionListener(_ -> new UserProfileFrame(user.getId(), user.getId()));

        btnMode.addActionListener(_ -> {
            viewMode = viewMode == ViewMode.DESKTOP ? ViewMode.MOBILE : ViewMode.DESKTOP;
            btnMode.setText(viewMode == ViewMode.MOBILE ? "🖥️" : "📱");
            applyViewMode();
            updateFeedLayout();
            loadFeed();
        });

        btnTheme.addActionListener(_ -> {
            theme = theme == Theme.LIGHT ? Theme.DARK : Theme.LIGHT;
            applyTheme();
        });

        applyViewMode();
        updateFeedLayout();
        loadFeed();
        applyTheme();
        setVisible(true);
        btnLogout.addActionListener(_ -> logout());
    }
    

    /* ================= VIEW MODE ================= */
    private void applyViewMode() {
        if (viewMode == ViewMode.MOBILE) {
            int w = 420;
            setSize(w, (int)(w * 16.0 / 9.0));
            setResizable(false);
        } else {
            int w = 960;
            setSize(w, (int)(w * 10.0 / 16.0));
            setResizable(true);
        }
        setLocationRelativeTo(null);
    }

    private void updateFeedLayout() {
        if (viewMode == ViewMode.MOBILE) {
            feedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            feedPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        } else {
            feedPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
            feedPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        }
    }

    /* ================= THEME ================= */
    private void applyTheme() {

        boolean dark = theme == Theme.DARK;

        Color bg   = dark ? new Color(18,18,18) : new Color(250,250,250);
        Color card = dark ? new Color(30,30,30) : Color.WHITE;
        Color text = dark ? new Color(230,230,230) : new Color(38,38,38);

        getContentPane().setBackground(bg);
        topBar.setBackground(card);
        wrapper.setBackground(bg);
        feedPanel.setBackground(bg);
        scroll.getViewport().setBackground(bg);

        for (Component c : feedPanel.getComponents()) {
            if (c instanceof JPanel panel) {
                panel.setBackground(card);
                for (Component cc : panel.getComponents()) {
                    if (cc instanceof JLabel l) l.setForeground(text);
                    if (cc instanceof JTextArea t) {
                        t.setForeground(text);
                        t.setCaretColor(text);
                    }
                }
            }
        }
        repaint();
    }

    /* ================= IMAGE ================= */
    private JLabel image(String path) {
        ImageIcon icon = new ImageIcon(path);
        int w = Math.max(feedPanel.getWidth() - 40, 320);
        int h = (int)((double)w / icon.getIconWidth() * icon.getIconHeight());

        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        JLabel lbl = new JLabel(new ImageIcon(img));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        lbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                fullscreen(path);
            }
        });
        return lbl;
    }

    private void fullscreen(String path) {
        JDialog d = new JDialog(this, true);
        d.setUndecorated(true);
        d.setBackground(Color.BLACK);

        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        d.setSize(s);

        ImageIcon i = new ImageIcon(path);
        Image img = i.getImage().getScaledInstance(
                s.width, s.height, Image.SCALE_SMOOTH);

        JLabel l = new JLabel(new ImageIcon(img));
        l.setHorizontalAlignment(JLabel.CENTER);
        l.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                d.dispose();
            }
        });

        d.add(l);
        d.setVisible(true);
    }

    /* ================= LOAD FEED ================= */
    private void loadFeed() {

        feedPanel.removeAll();

        PostDAO postDAO = new PostDAO();
        LikeDAO likeDAO = new LikeDAO();
        CommentDAO commentDAO = new CommentDAO();

        for (Post p : postDAO.feed(user.getId())) {

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(12,14,12,14));

            /* ===== HEADER ===== */
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            JLabel userLbl = new JLabel(p.getUsername());
            userLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            userLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            userLbl.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new UserProfileFrame(user.getId(), p.getUserId());
                }
            });
            header.add(userLbl, BorderLayout.WEST);

            if (p.getUserId() == user.getId()) {
                JPanel owner = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                owner.setOpaque(false);

                JButton edit = icon("✏️");
                JButton del  = icon("🗑️");

                edit.addActionListener(_ -> editPost(p));
                del.addActionListener(_ -> {
                    postDAO.deletePost(p.getId(), user.getId());
                    loadFeed();
                });

                owner.add(edit);
                owner.add(del);
                header.add(owner, BorderLayout.EAST);
            }

            card.add(header);

            /* ===== CONTENT ===== */
            JTextArea text = new JTextArea(p.getContent());
            text.setEditable(false);
            text.setOpaque(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            card.add(text);

            /* ===== MEDIA ===== */
            if (p.hasMedia()) {
                if (p.isImage()) card.add(image(p.getMediaPath()));
                if (p.isVideo()) {
                    JButton play = new JButton("▶ Play video");
                    play.addActionListener(_ -> {
                        try {
                            Desktop.getDesktop().open(new File(p.getMediaPath()));
                        } catch (Exception ignored) {}
                    });
                    card.add(play);
                }
            }

            /* ===== ACTION BAR ===== */
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            actions.setOpaque(false);

            JButton likeBtn = icon("❤");
            if (likeDAO.hasLiked(user.getId(), p.getId()))
                likeBtn.setForeground(Color.RED);

            JLabel likeCount =
                    new JLabel(String.valueOf(likeDAO.getLikeCount(p.getId())));

            JButton commentBtn = icon("💬");
            int cc = commentDAO.getCommentCount(p.getId());
            JLabel commentCount = new JLabel(String.valueOf(cc));
            commentCount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            likeBtn.addActionListener(_ -> {
                likeDAO.toggleLike(user.getId(), p.getId());
                loadFeed();
            });

            commentBtn.addActionListener(_ -> {
                String t = JOptionPane.showInputDialog(this, "Comment");
                if (t != null && !t.isBlank()) {
                    commentDAO.add(user.getId(), p.getId(), t);
                    loadFeed();
                }
            });

            if (cc > 0) {
                commentCount.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        showComments(p.getId());
                    }
                });
            }

            actions.add(likeBtn);
            actions.add(likeCount);
            actions.add(commentBtn);
            actions.add(commentCount);

            card.add(actions);

            feedPanel.add(card);
            feedPanel.add(Box.createVerticalStrut(14));
        }

        feedPanel.revalidate();
        feedPanel.repaint();
        applyTheme();
    }

    /* ================= COMMENTS ================= */
    private void showComments(int postId) {

        JDialog d = new JDialog(this, "Comments", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        CommentDAO dao = new CommentDAO();

        for (Comment c : dao.getCommentsWithOwner(postId)) {

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel l = new JLabel(c.getUsername() + ": " + c.getText());
            row.add(l);

            // comment owner OR post owner can delete
            if (c.getUserId() == user.getId()
                    || dao.isPostOwner(postId, user.getId())) {

                JButton del = icon("🗑️");
                del.addActionListener(_ -> {
                    dao.deleteComment(c.getCommentId(), user.getId());
                    d.dispose();
                    loadFeed();
                });
                row.add(del);
            }
            p.add(row);
        }

        d.add(new JScrollPane(p));
        d.setVisible(true);
    }

    /* ================= EDIT POST ================= */
    private void editPost(Post p) {

        JTextArea area = new JTextArea(p.getContent(), 5, 20);

        if (JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(area),
                "Edit Post",
                JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION) {

            new PostDAO().updatePost(
                    p.getId(),
                    user.getId(),
                    area.getText()
            );
            loadFeed();
        }
    }

    /* ================= CREATE POST ================= */
    private void createPost() {

        JTextArea area = new JTextArea(5, 20);
        JFileChooser chooser = new JFileChooser();

        if (JOptionPane.showConfirmDialog(
                this,
                new Object[]{"Post:", new JScrollPane(area), chooser},
                "Create Post",
                JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) return;

        String path = chooser.getSelectedFile() == null
                ? null
                : chooser.getSelectedFile().getAbsolutePath();

        new PostDAO().create(
                user.getId(),
                area.getText(),
                path,
                path != null && path.endsWith(".mp4") ? "VIDEO" : "IMAGE"
        );
        loadFeed();
    }

    /* ================= SEARCH ================= */
    private void searchUser() {
        String u = JOptionPane.showInputDialog(this, "Username");
        if (u == null || u.isBlank()) return;

        Integer id = new UserDAO().getUserId(u.trim());
        if (id == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }
        new UserProfileFrame(user.getId(), id);
    }
    /* ================= LOGOUT ================= */
    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // close Dashboard
            new LoginFrame(); // open login again
        }
    }
}
