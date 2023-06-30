package gpt.chat.ui;

import gpt.models.Message;
import javax.swing.*;
import java.util.List;

public interface ChatGUI {

    void startServer();
    void startSupportGUI();
    void sendMessage();
    void messageGPT();
    void gptResponse();
    void appendToPane(JTextPane textPane, String message, Boolean isBlock);
    ChatGUI setTemperature(double temperature);
    ChatGUI setModelName(String modelName);
    ChatGUI setResponderName(String responderName);
    ChatGUI setChatTitle(String chatTitle);
    ChatGUI setUserName(String userName);
    ChatGUI setMessages(List<Message> messages);

    default ChatGUI start(){
        startServer();
        startSupportGUI();
        return this;
    }
}
