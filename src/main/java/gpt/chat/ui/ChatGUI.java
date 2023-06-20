package gpt.chat.ui;

import javax.swing.*;

public interface ChatGUI {

    void startServer();
    void startSupportGUI();
    void sendMessageToGPT();
    void messageGPT();
    void gptResponse();
    void appendToPane(JTextPane textPane, String message, Boolean isBlock);

}
