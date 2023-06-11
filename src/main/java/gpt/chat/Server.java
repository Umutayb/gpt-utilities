package gpt.chat;

import utils.TextParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private final int port;
    private static List<User> clients = new ArrayList<>();
    ServerSocket server;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void run() throws IOException {
        server = new ServerSocket(port) {
            protected void finalize() throws IOException {
                this.close();
            }
        };
        System.out.println("Port 12345 is now open.");

        do {
            // accepts a new client
            Socket client = server.accept();

            // create new User
            User newUser = new User(client);

            // add newUser message to list
            this.clients.add(newUser);

            // create a new thread for newUser incoming messages handling
            new Thread(new UserHandler(this, newUser)).start();
        }
        while (true);
    }

    // delete a user from the list
    public void removeUser(User user){
        this.clients.remove(user);
    }

    // send incoming msg to all Users
    public static void broadcastMessages(String msg) {
        for (User client : clients) {
            client.getOutStream().println(msg);
        }
    }

    // send list of clients to all Users
    public static void broadcastAllUsers(){
        clients.get(0).getOutStream().println(clients.get(0));
    }
}

class UserHandler implements Runnable {

    private final Server server;
    private final User user;
    private TextParser textParser = new TextParser();

    public UserHandler(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    public void run() {
        String message;
        Scanner sc = new Scanner(this.user.getInputStream());
        while (sc.hasNextLine()) {
            message = sc.nextLine();
            String codeBlock = textParser.parse("´´´", "´´´", message);
            // smiley
            //message = message.replace(":)", "<img src='http://4.bp.blogspot.com/-ZgtYQpXq0Yo/UZEDl_PJLhI/AAAAAAAADnk/2pgkDG-nlGs/s1600/facebook-smiley-face-for-comments.png'>");
            server.broadcastMessages(message);
        }
        // end of Thread
        server.removeUser(user);
        this.server.broadcastAllUsers();
        sc.close();
    }
}

class User {
    private final PrintStream streamOut;
    private final InputStream streamIn;

    public User(Socket client) throws IOException {
        this.streamOut = new PrintStream(client.getOutputStream());
        this.streamIn = client.getInputStream();
    }

    public PrintStream getOutStream(){
        return this.streamOut;
    }

    public InputStream getInputStream(){
        return this.streamIn;
    }
}
