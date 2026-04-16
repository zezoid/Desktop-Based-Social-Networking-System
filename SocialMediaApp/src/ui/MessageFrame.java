package ui;

import dao.MessageDAO;

import javax.swing.*;
import java.awt.*;

public class MessageFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MessageFrame(int me, int you) {

        JTextArea chat = new JTextArea();
        chat.setEditable(false);

        JTextField msg = new JTextField();
        JButton send = new JButton("Send");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(msg, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(new JScrollPane(chat), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        send.addActionListener(_ -> {
            new MessageDAO().send(me, you, msg.getText());
            msg.setText("");
        });

        setTitle("Direct Message");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
