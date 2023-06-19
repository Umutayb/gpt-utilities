package gpt.chat;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class CustomChatBox extends JTextPane {


    public CustomChatBox(String message, Boolean isBlock) {
        System.out.println(message + " : " + isBlock);
        boolean isBLock = false;
        JTextPane textArea = new JTextPane();
        if(message != null){
            if (message.contains("```")||message.contains("'''")) isBLock = !isBLock;{
            }
        }
        // Parse text
        if (!isBlock){
            // Regular text
            JTextPane textPane = new JTextPane();
            textPane.setText(message);
            textArea.add(textPane);
        }
        else {
            // Code block
            RSyntaxTextArea syntaxTextArea = new RSyntaxTextArea();
            syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            syntaxTextArea.setCodeFoldingEnabled(true);
            syntaxTextArea.setText(message.replace("```", ""));
            RTextScrollPane syntaxAreaScrollPane = new RTextScrollPane(syntaxTextArea);
            syntaxAreaScrollPane.setVisible(true);
            syntaxAreaScrollPane.repaint();
            textArea.add(syntaxAreaScrollPane);
            textArea.revalidate();

        }

    }

    public JTextPane updatePane(String message, Boolean isBlock){
        System.out.println(message + " : " + isBlock);
        JTextPane textArea = new JTextPane();
        // Parse text
        if (!isBlock){
            // Regular text
            JTextPane textPane = new JTextPane();
            textPane.setText(message);
            textArea.add(textPane);
        }
        else {
            // Code block
            RSyntaxTextArea syntaxTextArea = new RSyntaxTextArea();
            syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            syntaxTextArea.setCodeFoldingEnabled(true);
            syntaxTextArea.setText(message.replace("```", ""));
            RTextScrollPane syntaxAreaScrollPane = new RTextScrollPane(syntaxTextArea);
            syntaxAreaScrollPane.setVisible(true);
            syntaxAreaScrollPane.repaint();
            textArea.add(syntaxAreaScrollPane);
            textArea.revalidate();

        }
        return textArea;
    }
}
