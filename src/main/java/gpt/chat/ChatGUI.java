package gpt.chat;

import javax.swing.*;

public interface ChatGUI {

    void startServer();

    void startSupportGUI();

    void sendMessage();
    void gptResponse();

    void appendToPane(JTextPane textPane, String message);
}
