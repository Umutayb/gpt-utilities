package gpt.chat;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JTextPane {

    public TextEditor() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Text Editor Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Input text
            String text = "Here is some regular text.\n\n''' public static void main(String[] args) {\n" +
                    "        Scanner scanner = new Scanner(System.in);\n" +
                    "\n" +
                    "        System.out.print(\"Enter a positive integer: \");\n" +
                    "        int number = scanner.nextInt();\n" +
                    "\n" +
                    "        if (number < 0) {\n" +
                    "            System.out.println(\"Error: Entered number should be positive.\");\n" +
                    "        } else {\n" +
                    "            long factorial = calculateFactorial(number);\n" +
                    "            System.out.println(\"The factorial of \" + number + \" is: \" + factorial);\n" +
                    "        }\n" +
                    "\n" +
                    "        scanner.close();\n" +
                    "    }\n" +
                    "\n" +
                    "    private static long calculateFactorial(int number) {\n" +
                    "        if (number == 0 || number == 1) {\n" +
                    "            return 1;\n" +
                    "        } else {\n" +
                    "            long factorial = 1;\n" +
                    "            for (int i = 2; i <= number; i++) {\n" +
                    "                factorial *= i;\n" +
                    "            }\n" +
                    "            return factorial;\n" +
                    "        }\n" +
                    "    } '''\n\nHere is some more regular text.\n";

            // Parse text
            String[] parts = text.split("'''");
            for (int i = 0; i < parts.length; i++) {
                if (i % 2 == 0) {
                    // Regular text
                    JTextPane textPane = new JTextPane();
                    textPane.setText(parts[i]);
                    panel.add(textPane);
                } else {
                    // Code block
                    RSyntaxTextArea textArea = new RSyntaxTextArea();
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                    textArea.setCodeFoldingEnabled(true);
                    textArea.setText(parts[i]);
                    textArea.setBackground(Color.darkGray);
                    textArea.setForeground(Color.white);
                    textArea.setEditable(false);
                    textArea.setHighlightCurrentLine(false);
                    textArea.setEnabled(false);
                    RTextScrollPane codeScrollPane = new RTextScrollPane(textArea);
                    codeScrollPane.setBackground(Color.darkGray);
                    codeScrollPane.setFoldIndicatorEnabled(true);
                    panel.add(codeScrollPane);
                }
            }

            frame.add(new JScrollPane(panel));
            frame.setSize(500, 500);
            frame.setVisible(true);
        });

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Text Editor Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Input text
            String text = "Here is some regular text.\n\n''' public static void main(String[] args) {\n" +
                    "        Scanner scanner = new Scanner(System.in);\n" +
                    "\n" +
                    "        System.out.print(\"Enter a positive integer: \");\n" +
                    "        int number = scanner.nextInt();\n" +
                    "\n" +
                    "        if (number < 0) {\n" +
                    "            System.out.println(\"Error: Entered number should be positive.\");\n" +
                    "        } else {\n" +
                    "            long factorial = calculateFactorial(number);\n" +
                    "            System.out.println(\"The factorial of \" + number + \" is: \" + factorial);\n" +
                    "        }\n" +
                    "\n" +
                    "        scanner.close();\n" +
                    "    }\n" +
                    "\n" +
                    "    private static long calculateFactorial(int number) {\n" +
                    "        if (number == 0 || number == 1) {\n" +
                    "            return 1;\n" +
                    "        } else {\n" +
                    "            long factorial = 1;\n" +
                    "            for (int i = 2; i <= number; i++) {\n" +
                    "                factorial *= i;\n" +
                    "            }\n" +
                    "            return factorial;\n" +
                    "        }\n" +
                    "    } '''\n\nHere is some more regular text.\n";

            // Parse text
            String[] parts = text.split("'''");
            for (int i = 0; i < parts.length; i++) {
                if (i % 2 == 0) {
                    // Regular text
                    JTextPane textPane = new JTextPane();
                    textPane.setText(parts[i]);
                    panel.add(textPane);
                } else {
                    // Code block
                    RSyntaxTextArea textArea = new RSyntaxTextArea();
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                    textArea.setCodeFoldingEnabled(true);
                    textArea.setText(parts[i]);
                    textArea.setBackground(Color.darkGray);
                    textArea.setForeground(Color.white);
                    textArea.setEditable(false);
                    textArea.setHighlightCurrentLine(false);
                    textArea.setEnabled(false);
                    RTextScrollPane codeScrollPane = new RTextScrollPane(textArea);
                    codeScrollPane.setBackground(Color.darkGray);
                    codeScrollPane.setFoldIndicatorEnabled(true);
                    panel.add(codeScrollPane);
                }
            }

            frame.add(new JScrollPane(panel));
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}