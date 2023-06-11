package gpt.chat;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class CustomTextArea extends JTextPane {

    public CustomTextArea(){
        // Parse text
        String[] parts = this.getText().split("'''");
        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                // Regular text
                JTextPane textPane = new JTextPane();
                textPane.setText(parts[i]);
                this.add(textPane);
            } else {
                // Code block
                RSyntaxTextArea textArea = new RSyntaxTextArea();
                textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                textArea.setCodeFoldingEnabled(true);
                textArea.setText(parts[i]);
                this.add(new RTextScrollPane(textArea));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Text Editor Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CustomTextArea panel = new CustomTextArea();

            frame.add(new JScrollPane(panel));
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}
