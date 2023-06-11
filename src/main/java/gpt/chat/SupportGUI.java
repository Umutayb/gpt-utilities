package gpt.chat;

import api_assured.Caller;
import gpt.api.GPT;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
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
public class SupportGUI implements ChatGUI {
    private JButton sendButton;
    private JFrame supportPanel;
    private JPanel loadingAnimation = new BufferAnimation.AnimationPanel();
    private JScrollPane chatOverviewScrollPanel;
    private JScrollPane messageInputScrollPanel;
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

    public void startServer(){
        Thread serverThread = new Thread(() -> {
            try {new Server(12345).run();}
            catch (IOException e) {throw new RuntimeException(e);}
        });
        serverThread.start();
    }

    @SuppressWarnings("unused")
    public SupportGUI(GPT gpt) {
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.7;
        this.gpt = gpt;
        this.userName = "User";
        this.responderName = "ChatGPT";

        Caller.keepLogs(false);
        startServer();
        startSupportGUI();
    }

    @SuppressWarnings("unused")
    public SupportGUI(
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

        new ColorTitleBar();

        supportPanel.getContentPane().setBackground(new Color(46 , 46 ,52));
        supportPanel.setBackground(new Color(46 , 46 ,52));

        chatOverviewPanel.setBackground(new Color(67 , 69 ,74));
        messageInputPanel.setBackground(new Color(67 , 69 ,74));

        chatOverviewScrollPanel.setBackground(new Color(46 , 46 ,52));
        chatOverviewScrollPanel.setBorder(BorderFactory.createLineBorder(new Color(46 , 46 ,52)));
        chatOverviewScrollPanel.getVerticalScrollBar().setBackground(new Color(67 , 69 ,74));
        chatOverviewScrollPanel.getHorizontalScrollBar().setBackground(new Color(67 , 69 ,74));

        messageInputScrollPanel.setBackground(new Color(46 , 46 ,52));
        messageInputScrollPanel.setBorder(BorderFactory.createLineBorder(new Color(46 , 46 ,52)));
        messageInputScrollPanel.getHorizontalScrollBar().setBackground(new Color(67 , 69 ,74));
        messageInputScrollPanel.getVerticalScrollBar().setBackground(new Color(67 , 69 ,74));

        sendButton.setBackground(new Color(67 , 69 ,74));
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);

        chatOverviewScrollPanel.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(46 , 46 ,52);
            }
        });

        messageInputScrollPanel.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(46 , 46 ,52);
            }
        });

    }

    public void setFonts() {
        String fontfamily = "JetBrains Mono";
        Font font = new Font(fontfamily, Font.PLAIN, 15);
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
            chatOverviewPanel.setBounds(15, 15, 670, 320);
            chatOverviewPanel.setMargin(new Insets(6, 6, 6, 6));
            chatOverviewScrollPanel = new JScrollPane(chatOverviewPanel);
            chatOverviewScrollPanel.setBounds(15, 15, 670, 320);
            chatOverviewScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            chatOverviewPanel.setContentType("text/html");
            chatOverviewPanel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

            // Field message user input
            messageInputPanel.setBounds(0, 350, 290, 50);
            messageInputPanel.setMargin(new Insets(6, 6, 6, 6));
            messageInputScrollPanel = new JScrollPane(messageInputPanel);
            messageInputScrollPanel.setBounds(15, 345, 555, 110);

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

            supportPanel.add(chatOverviewScrollPanel);
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
            supportPanel.add(messageInputScrollPanel, JLayeredPane.DEFAULT_LAYER);
            supportPanel.revalidate();
            supportPanel.repaint();


            loadingAnimation.setLocation(0,0);
            loadingAnimation.setBounds(supportPanel.getBounds());
            loadingAnimation.setPreferredSize(supportPanel.getPreferredSize());
            loadingAnimation.setSize(supportPanel.getSize());
            loadingAnimation.setVisible(false);
            supportPanel.setGlassPane(loadingAnimation);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        setFonts();
        setColors();
    }

    public void sendMessage() {
        try {
            String message = messageInputPanel.getText().trim();
            if (message.equals("")) return;
            oldMsg = message;
            output.println("<b><span style='color:#57975C'>" + userName + ": </span></b>" + message); //HexCode

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
            output.println("<b><span style='color:#B46C30'>" + responderName + ": </span></b>" + message); //HexCode
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
