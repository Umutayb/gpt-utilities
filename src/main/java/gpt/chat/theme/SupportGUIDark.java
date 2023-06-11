package gpt.chat.theme;

import api_assured.Caller;
import gpt.api.GPT;
import gpt.chat.BufferAnimation;
import gpt.chat.ChatGUI;
import gpt.chat.Server;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import utils.TextParser;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Data
public class SupportGUIDark implements ChatGUI {
    private JButton sendButton;
    private JFrame supportPanel;
    private JPanel loadingAnimation = new BufferAnimation.AnimationPanel();
    private JScrollPane chatOverviewScrollPane;
    private JScrollPane messageInputScrollPane;
    private JTextPane chatOverviewPanel = new JTextPane();
    private JTextArea messageInputPanel = new JTextArea();
    private String oldMsg;
    private Thread read;
    private String serverName;
    private int PORT;
    private String name;
    private BufferedReader input;
    private PrintWriter output;
    private Socket server;
    private final List<Message> messages = new ArrayList<>();
    private String modelName;
    private Double temperature;
    private GPT gpt;
    private String responderName;
    private String userName;
    private String chatTitle;
    private TextParser textParser = new TextParser();
    private JTextPane codeView;

    public void startServer(){
        Thread serverThread = new Thread(() -> {
            try {new Server(12345).run();}
            catch (IOException e) {throw new RuntimeException(e);}
        });
        serverThread.start();
    }

    @SuppressWarnings("unused")
    public SupportGUIDark(GPT gpt) {
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.7;
        this.gpt = gpt;
        this.userName = "User";
        this.responderName = "ChatGPT";
        this.chatTitle = "Chat";

        Caller.keepLogs(false);
        startServer();
        startSupportGUI();
    }

    @SuppressWarnings("unused")
    public SupportGUIDark(
            List<String> prompts,
            String modelName,
            double temperature,
            GPT gpt,
            String userName,
            String responderName,
            String chatTitle
    ) {
        this.modelName = modelName;
        this.temperature = temperature;
        this.gpt = gpt;
        this.chatTitle = chatTitle;
        this.responderName = responderName;
        this.userName = userName;

        Caller.keepLogs(false);
        startServer();
        startSupportGUI();
        for (String prompt:prompts) messages.add(new Message("system", prompt));
    }

    public void setColors() {
        supportPanel.getContentPane().setBackground(new Color(46 , 46 ,52));
        supportPanel.setBackground(new Color(46 , 46 ,52));

        chatOverviewPanel.setBackground(new Color(67 , 69 ,74));
        messageInputPanel.setBackground(new Color(67 , 69 ,74));

        sendButton.setBackground(new Color(67 , 69 ,74));
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);


    }

    public void setScrollPane() {
        chatOverviewScrollPane.setBackground(new Color(46 , 46 ,52));
        chatOverviewScrollPane.setBorder(BorderFactory.createLineBorder(new Color(46 , 46 ,52)));
        chatOverviewScrollPane.getVerticalScrollBar().setBackground(new Color(67 , 69 ,74));
        chatOverviewScrollPane.getHorizontalScrollBar().setBackground(new Color(67 , 69 ,74));

        messageInputScrollPane.setBackground(new Color(46 , 46 ,52));
        messageInputScrollPane.setBorder(BorderFactory.createLineBorder(new Color(46 , 46 ,52)));
        messageInputScrollPane.getHorizontalScrollBar().setBackground(new Color(67 , 69 ,74));
        messageInputScrollPane.getVerticalScrollBar().setBackground(new Color(67 , 69 ,74));

        chatOverviewScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private static final Color THUMB_COLOR = new Color(46 , 46 ,52);
            private static final Color TRACK_COLOR = new Color(67 , 69 ,74);

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(THUMB_COLOR);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TRACK_COLOR);
                g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });

        messageInputScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private static final Color THUMB_COLOR = new Color(46 , 46 ,52);
            private static final Color TRACK_COLOR = new Color(67 , 69 ,74);

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(THUMB_COLOR);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(TRACK_COLOR);
                g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
    }

    public void setFonts() {
        String fontfamily = "SansSerif";
        Font font = new Font(fontfamily, Font.PLAIN, 14);
        chatOverviewPanel.setFont(font);
        messageInputPanel.setFont(font);
        messageInputPanel.setLineWrap(true);
        sendButton.setFont(font);

        //Font Color
        supportPanel.setForeground(new Color(222, 221, 228));
        chatOverviewPanel.setForeground(new Color(222, 221, 228));
        messageInputPanel.setForeground(new Color(222, 221, 228));
        sendButton.setForeground(new Color(222, 221, 228));

        chatOverviewPanel.setEditable(false);
    }

    public void startSupportGUI() {
        try {
            supportPanel = new JFrame(chatTitle);
            supportPanel.getContentPane().setLayout(null);
            supportPanel.setSize(700, 500);
            supportPanel.setResizable(false);
            supportPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Chat panel
            chatOverviewPanel.setBounds(15, 15, 665, 320);
            chatOverviewPanel.setMargin(new Insets(6, 6, 6, 6));
            chatOverviewScrollPane = new JScrollPane(chatOverviewPanel);
            chatOverviewScrollPane.setBounds(15, 15, 670, 320);
            chatOverviewScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            chatOverviewPanel.setContentType("text/html");
            chatOverviewPanel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

            // Field message user input
            messageInputPanel.setBounds(0, 350, 290, 50);
            messageInputPanel.setMargin(new Insets(6, 6, 6, 6));
            messageInputScrollPane = new JScrollPane(messageInputPanel);
            messageInputScrollPane.setBounds(15, 345, 555, 110);

            // Send button
            sendButton = new JButton("Send");
            sendButton.setBounds(580, 346, 105, 108);

            messageInputPanel.addKeyListener(new KeyAdapter() {
                // Send message on Enter
                public void keyPressed(KeyEvent e) {

                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        sendMessage();
                    }

                    // Get last message typed
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        String currentMessage = messageInputPanel.getText().trim();
                        messageInputPanel.setText(oldMsg);
                        oldMsg = currentMessage;
                    }

                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        String currentMessage = messageInputPanel.getText().trim();
                        messageInputPanel.setText(oldMsg);
                        oldMsg = currentMessage;
                    }

                }
            });

            // Send button click action
            sendButton.addActionListener(ae -> sendMessage());

            supportPanel.add(chatOverviewScrollPane);
            supportPanel.setVisible(true);

            // Chat panel initial message
            appendToPane(chatOverviewPanel,
                    "<b>Welcome to " + chatTitle + ", please ask your questions!</b>"
            );

            // Default server specifications
            name = "User";
            String port = "12345";
            serverName = "localhost";
            PORT = Integer.parseInt(port);

            server = new Socket(serverName, PORT);
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter(server.getOutputStream(), true);

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }

            // Create new read thread
            read = new Read();
            read.start();

            supportPanel.add(sendButton, JLayeredPane.DEFAULT_LAYER);
            supportPanel.add(messageInputScrollPane, JLayeredPane.DEFAULT_LAYER);
            supportPanel.revalidate();
            supportPanel.repaint();

            loadingAnimation.setLocation(0,0);
            loadingAnimation.setBounds(supportPanel.getBounds());
            loadingAnimation.setPreferredSize(supportPanel.getPreferredSize());
            loadingAnimation.setVisible(false);
            supportPanel.setGlassPane(loadingAnimation);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        setFonts();
        setColors();
        setScrollPane();
    }

    public void sendMessage() {
        try {
            String message = messageInputPanel.getText().trim();
            if (message.equals("")) return;
            oldMsg = message;
            output.println("<b><span style='color:#c86730'>" + userName + ": </span></b>" + message); //HexCode

            messages.add(new Message("user", message));
            messageInputPanel.requestFocus();
            messageInputPanel.setText(null);

            messageGPT();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    public void messageGPT() {
        sendButton.setEnabled(false);
        messageInputPanel.setEnabled(false);
        loadingAnimation.setVisible(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                gptResponse();
                return null;
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                // you should also call get() on the worker allowing
                // you to capture and handle all exceptions it might throw
                messageInputPanel.setEnabled(true);
                sendButton.setEnabled(true);
                loadingAnimation.setVisible(false);
            }
        });

        worker.execute();  // run the worker
    }

    public void gptResponse() {
        try {
            MessageResponse messageResponse;
            if (messages.size()!=0)
                messageResponse = gpt.sendMessage(new MessageModel(modelName, messages, temperature));
            else
                messageResponse = gpt.sendMessage(
                        new MessageModel(
                                modelName,
                                List.of(new Message("user", "Hello!")),
                                temperature
                        )
                );
            messages.add(messageResponse.getChoices().get(0).getMessage());
            String message = messageResponse.getChoices().get(0).getMessage().getContent();
            if (message.contains("´´´")) {
                String code = textParser.parse("´´´","´´´",message);
                //CodeBlockExample.formatCode(codeView, code);
                output.println("<b><span style='color:#B46C30'>" + responderName + ": </span></b>" + message + "\n" + code);
            }

            output.println("<b><span style='color:#49984d'>" + responderName + ": </span></b>" + message); //HexCode
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }

    }

    public void appendToPane(JTextPane textPane, String message){
        HTMLDocument doc = (HTMLDocument)textPane.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)textPane.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
            textPane.setCaretPosition(doc.getLength());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    class Read extends Thread {
        public void run() {
            String message;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    message = input.readLine();
                    if(message != null) appendToPane(chatOverviewPanel, message);
                }
                catch (IOException ex) {
                    System.err.println("Failed to parse incoming message");
                }
            }
        }
    }
}