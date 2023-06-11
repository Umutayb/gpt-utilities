package gpt.chat;

import gpt.models.MessageResponse;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class CodeBlockExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the JTextPane
            JTextPane textPane = new JTextPane();
            textPane.setEditable(false);

            // Set the font for code blocks
            Font codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            textPane.setFont(codeFont);

            // Set the default style for the JTextPane
            Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setFontFamily(defaultStyle, codeFont.getFamily());
            StyleConstants.setFontSize(defaultStyle, codeFont.getSize());

            // Create a custom StyledDocument
            StyledDocument doc = new DefaultStyledDocument() {
                @Override
                public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                    // Check if the inserted text starts with triple backticks
                    if (str.startsWith("```")) {
                        // Create a custom style for code blocks
                        Style codeStyle = addStyle("CodeStyle", defaultStyle);
                        StyleConstants.setBold(codeStyle, true);
                        StyleConstants.setForeground(codeStyle, Color.BLUE);

                        // Insert the code block with the custom style
                        super.insertString(offset, str, codeStyle);
                    } else {
                        // Insert the regular text with the default style
                        super.insertString(offset, str, defaultStyle);
                    }
                }
            };

            // Set the custom StyledDocument to the JTextPane
            textPane.setStyledDocument(doc);

            // Add the JTextPane to a JScrollPane
            JScrollPane scrollPane = new JScrollPane(textPane);

            // Create a JFrame to display the JTextPane
            JFrame frame = new JFrame("Code Block Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(scrollPane);
            frame.setSize(400, 300);
            frame.setVisible(true);

            // Insert some text with code blocks into the JTextPane
            try {
                doc.insertString(0, "Regular text\n", null);
                doc.insertString(doc.getLength(), "```\nSystem.out.println(\"Hello, World!\");\n```\n", null);
                doc.insertString(doc.getLength(), "More regular text\n", null);
                doc.insertString(doc.getLength(), "```\nint x = 5;\nint y = 10;\nint sum = x + y;\nSystem.out.println(sum);\n```\n", null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }
}
