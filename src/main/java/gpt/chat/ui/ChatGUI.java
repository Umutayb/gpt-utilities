package gpt.chat.ui;

import gpt.chat.CustomTextArea;

import javax.swing.*;

public interface ChatGUI {

    void startServer();
    void startSupportGUI();
    void sendMessage();
    void messageGPT();
    void gptResponse();
    void appendToPane(JTextPane textPane, String message, Boolean isBlock);

}
