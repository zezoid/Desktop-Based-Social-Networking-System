package ui;

import dao.MessageDAO;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {

    private JTextArea chatArea;
    private JTextField input;
    private MessageDAO dao;

    private int me;
    private int other;

    public ChatFrame(int me, int other, String username) {

        this.me = me;
        this.other = other;
        dao = new MessageDAO();

        setTitle("Chat with " + username);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /* ===== CHAT AREA ===== */
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, BorderLayout.CENTER);

        /* ===== INPUT ===== */
        JPanel bottom = new JPanel(new BorderLayout());

        input = new JTextField();
        JButton send = new JButton("Send");

        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        /* ===== ACTION ===== */
        send.addActionListener(_ -> sendMessage());

        /* ===== LOAD ===== */
        loadChat();

        /* ===== AUTO REFRESH ===== */
        new javax.swing.Timer(1000, e -> loadChat()).start();

        setVisible(true);
    }

    /* ================= SEND ================= */
    private void sendMessage() {

        String text = input.getText().trim();

        if (text.isEmpty()) return;

        dao.send(me, other, text);

        input.setText("");

        loadChat(); // refresh immediately
    }

    /* ================= LOAD ================= */
    private void loadChat() {

        chatArea.setText("");

        for (String msg : dao.chat(me, other)) {
            chatArea.append(msg + "\n");
        }

        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}