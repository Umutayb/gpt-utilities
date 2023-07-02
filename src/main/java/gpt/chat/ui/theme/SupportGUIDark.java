package gpt.chat.ui.theme;

import api_assured.Caller;
import gpt.api.GPT;
import gpt.chat.server.Server;
import gpt.chat.ui.BufferAnimation;
import gpt.chat.ui.ChatGUI;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import utils.TextParser;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class SupportGUIDark implements ChatGUI {  //TODO: Make styling dynamically read from a json
    public JTextPane chatOverviewPanel = new JTextPane();
    public JTextArea messageInputPanel = new JTextArea();
    public List<Message> messages = new ArrayList<>();
    public TextParser textParser = new TextParser();
    public JScrollPane chatOverviewScrollPane;
    public JScrollPane messageInputScrollPane;
    public JPanel loadingAnimation;
    public BufferedReader input;
    public String responderName;
    public JFrame supportPanel;
    public PrintWriter output;
    public Double temperature;
    public JTextPane codeView;
    public JButton sendButton;
    public String serverName;
    public String modelName;
    public String chatTitle;
    public String userName;
    public Socket server;
    public String oldMsg;
    public String name;
    public Thread read;
    public int PORT;
    public GPT gpt;

    public void startServer(){
        Thread serverThread = new Thread(() -> {
            try {new Server(12345).run();}
            catch (IOException e) {throw new RuntimeException(e);}
        });
        serverThread.start();
    }

    public SupportGUIDark setTemperature(double temperature){
        this.temperature = temperature;
        return this;
    }

    public SupportGUIDark setModelName(String modelName){
        this.modelName = modelName;
        return this;
    }

    public SupportGUIDark setResponderName(String responderName){
        this.responderName = responderName;
        return this;
    }

    public SupportGUIDark setChatTitle(String chatTitle){
        this.chatTitle = chatTitle;
        return this;
    }

    public SupportGUIDark setUserName(String userName){
        this.userName = userName;
        return this;
    }

    public SupportGUIDark setMessages(List<Message> messages){
        this.messages = messages;
        return this;
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
        for (String prompt:prompts) messages.add(new Message("system", prompt));
    }

    public void setUpColors(Color primary, Color secondary) {
        supportPanel.getContentPane().setBackground(primary);
        supportPanel.setBackground(primary);

        chatOverviewPanel.setBackground(secondary);
        messageInputPanel.setBackground(secondary);

        sendButton.setBackground(secondary);
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);

        chatOverviewScrollPane.setBackground(primary);
        chatOverviewScrollPane.setBorder(BorderFactory.createLineBorder(primary));
        chatOverviewScrollPane.getVerticalScrollBar().setBackground(secondary);
        chatOverviewScrollPane.getHorizontalScrollBar().setBackground(secondary);

        messageInputScrollPane.setBackground(primary);
        messageInputScrollPane.setBorder(BorderFactory.createLineBorder(primary));
        messageInputScrollPane.getHorizontalScrollBar().setBackground(secondary);
        messageInputScrollPane.getVerticalScrollBar().setBackground(secondary);

    }

    public void setUpScrollBars(Color thumbColors, Color trackColors) {
        chatOverviewScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColors);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColors);
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

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColors);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColors);
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

    public void setUpFonts(String fontFamily, Color fontColor, Color selectionColor, int fontSize) {
        Font font = new Font(fontFamily, Font.PLAIN, fontSize);
        supportPanel.setForeground(fontColor);
        chatOverviewPanel.setFont(font);
        chatOverviewPanel.setForeground(fontColor);
        chatOverviewPanel.setSelectionColor(selectionColor);
        chatOverviewPanel.setSelectedTextColor(fontColor);
        chatOverviewPanel.setEditable(false);
        messageInputPanel.setFont(font);
        messageInputPanel.setLineWrap(true);
        messageInputPanel.setForeground(fontColor);
        messageInputPanel.setSelectionColor(selectionColor);
        messageInputPanel.setSelectedTextColor(fontColor);
        sendButton.setFont(font);
        sendButton.setForeground(fontColor);
    }

    public void lookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public JFrame setUpSupportPanel() {
        supportPanel = new JFrame(chatTitle);
        supportPanel.getContentPane().setLayout(null);
        supportPanel.setSize(700, 500);
        supportPanel.setResizable(false);
        supportPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        supportPanel.setVisible(true);
        return supportPanel;
    }

    public void addToSupportPanel() {
        supportPanel.add(setUpChatOverviewScrollPane());
        supportPanel.add(setUpSendButton(), JLayeredPane.DEFAULT_LAYER);
        // Field message user input
        supportPanel.add(setUpMessageInputScrollPane(setUpMessageInputPanel()), JLayeredPane.DEFAULT_LAYER);
        supportPanel.setGlassPane(setUpLoadAnimation());
    }

    public JTextPane setUpChatOverviewPanel() {
        chatOverviewPanel.setBounds(15, 15, 665, 320);
        chatOverviewPanel.setMargin(new Insets(6, 6, 6, 6));
        chatOverviewPanel.setContentType("text/html");
        chatOverviewPanel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        return chatOverviewPanel;
    }

    public JScrollPane setUpChatOverviewScrollPane() {
        chatOverviewScrollPane = new JScrollPane(setUpChatOverviewPanel());
        chatOverviewScrollPane.setBounds(15, 15, 670, 320);
        chatOverviewScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return chatOverviewScrollPane;
    }

    public JTextArea setUpMessageInputPanel(){
        messageInputPanel.setBounds(0, 350, 290, 50);
        messageInputPanel.setMargin(new Insets(6, 6, 6, 6));
        return messageInputPanel;
    }

    public JScrollPane setUpMessageInputScrollPane(JTextArea messageInputPanel){
        messageInputScrollPane = new JScrollPane(messageInputPanel);
        messageInputScrollPane.setBounds(15, 345, 555, 110);
        return messageInputScrollPane;
    }

    public void setUpServer() {
        try {
            name = "User";
            String port = "12345";
            serverName = "localhost";
            PORT = Integer.parseInt(port);

            server = new Socket(serverName, PORT);
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter(server.getOutputStream(), true);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpCodeBlock() {
        //TextEditor.setCodeBlock(supportPanel, chatOverviewPanel);
    }

    public JButton setUpSendButton(){
        sendButton = new JButton("Send");
        sendButton.setBounds(580, 346, 105, 108);
        sendButton.addActionListener(ae -> sendMessage());
        return sendButton;
    }

    public JPanel setUpLoadAnimation() {
        loadingAnimation = new BufferAnimation.AnimationPanel();
        loadingAnimation.setLocation(0,0);
        loadingAnimation.setBounds(supportPanel.getBounds());
        loadingAnimation.setPreferredSize(supportPanel.getPreferredSize());
        loadingAnimation.setVisible(false);
        return loadingAnimation;
    }

    public void setUpKeyListeners() {
        setUpMessageInputPanel().addKeyListener(new KeyAdapter() {
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
    }

    public void createReadThread() {
        read = new Read();
        read.start();
    }

    public void startSupportGUI() {
        setUpSupportPanel();
        //Chat panel
        setUpChatOverviewPanel();
        //KeyListeners
        setUpKeyListeners();
        //Send button action
        setUpSendButton();
        //Placings to main panel
        addToSupportPanel();
        //Chat panel initial message
        appendToPane(setUpChatOverviewPanel(),
                "<b>Welcome to " + chatTitle + ", please ask your questions!</b>", false
        );
        //Default server specifications
        setUpServer();
        //Sets look and feel
        lookAndFeel();
        //Create new read thread
        createReadThread();
        //Revalidates/repaints main panel
        supportPanel.revalidate();
        supportPanel.repaint();
        //Adjusts scrollbars/fonts/colors
        setUpScrollBars(new Color(46 , 46 ,52), new Color(67 , 69 ,74));
        setUpFonts("SansSerif", new Color(222, 221, 228), new Color(46 , 46 ,52), 14);
        setUpColors(new Color(46 , 46 ,52), new Color(67 , 69 ,74));
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

            output.println("<b><span style='color:#49984d'>" + responderName + ": </span></b>" + message); //HexCode
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }

    }

    public void appendToPane(JTextPane textPane, String message, Boolean isBlock){
        HTMLDocument doc = (HTMLDocument) textPane.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit) textPane.getEditorKit();
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
            boolean isBLock = false;
            while(!Thread.currentThread().isInterrupted()){
                try {
                    message = input.readLine();
                    if(message != null){
                        if (message.contains("```")||message.contains("'''")) isBLock = !isBLock;
                        appendToPane(chatOverviewPanel, message, isBLock);
                    }
                    //System.out.println("MESSAGE:\n" + message);

                }
                catch (IOException ex) {
                    System.err.println("Failed to parse incoming message");
                }
            }
        }
    }
}