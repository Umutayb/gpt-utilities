package gpt.chat;

import api_assured.Caller;
import api_assured.exceptions.FailedCallException;
import gpt.api.GPT;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
@AllArgsConstructor
public class Chat {

    private List<String> prompts;
    private List<Message> messages = new ArrayList<>();
    private MessageModel messageModel;
    private String modelName;
    private Double temperature;
    private GPT gpt;

    public Chat(List<String> prompts, GPT gpt){
        this.prompts = prompts;
        this.gpt = gpt;
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.5;

        Caller.keepLogs(true);
        for (String prompt:prompts) messages.add(new Message("user", prompt));
        this.prompts = new ArrayList<>(); //Flushing prompts after adding them to messages, preventing duplicate prompts
    }

    public Chat(GPT gpt){
        this.gpt = gpt;
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.5;
    }

    /**
     * This method initiates a chat between the user and the GPT model.
     * It prompts the user to enter a message and sends it to the model for a response.
     * The conversation continues until the user or the model responds with "bye".
     * If the conversation reaches the conversation limit (100 messages), it will stop and prompt the user to start a new conversation.
     *
     */
    public void startChat(){
        Scanner scanner = new Scanner(System.in);
        startChat(scanner);
        scanner.close();
    }

    /**
     * This method initiates a chat between the user and the GPT model.
     * It prompts the user to enter a message and sends it to the model for a response.
     * The conversation continues until the user or the model responds with "bye".
     * If the conversation reaches the conversation limit (100 messages), it will stop and prompt the user to start a new conversation.
     *
     * @param scanner Scanner object to receive user input
     */
    public void startChat(Scanner scanner){
        int conversationCounter = 0;
        int conversationLimit = 100;
        boolean retainChat = true;
        do {
            if (prompts != null && prompts.size() > 0 ) for (String prompt:prompts) messages.add(new Message("user", prompt));
            if (conversationCounter == 0) gpt.log.new Info("Enter your message (press Enter on an empty line to finish):");
            else  gpt.log.new Info("Your answer: ");
            StringBuilder prompt = new StringBuilder();
            String input;
            while (scanner.hasNextLine() && !(input = scanner.nextLine()).isEmpty()) prompt.append(input).append("\n");
            messages.add(new Message("user", prompt.toString()));
            gpt.log.new Info("Waiting for GPT...");
            try {
                MessageResponse messageResponse = gpt.sendMessage(new MessageModel(modelName, messages, temperature));
                Message message = messageResponse.getChoices().get(0).getMessage();
                messages.add(new Message(message.getRole(), message.getContent()));
                gpt.log.new Info(messageResponse.getChoices().get(0).getMessage().getContent());
                if (messageResponse.getChoices().get(0).getMessage().getContent().contains("bye")) break;
            }
            catch (FailedCallException failedCall){
                gpt.log.new Warning("Please make sure you have a valid token!");
                return;
            }

            conversationCounter++;
            if (conversationCounter >= conversationLimit) {
                retainChat = false;
                gpt.log.new Warning("Reached limited number of messages! Please start a new conversation.");
            }
        }
        while (retainChat);
    }

    /**
     * This method evaluates a given topic by having two GPT AI models talk about it.
     *
     * @param topic the topic to evaluate
     */
    public void evaluateTopic(String topic){
        GPT responder = new GPT(gpt.getToken());
        gpt.log.new Info("Evaluating " + topic + " topic...");
        List<Message> messages = new ArrayList<>();
        List<Message> responses = new ArrayList<>();
        String modelName = "gpt-3.5-turbo";
        double temperature = 0.5;
        String initialPrompt = "Lets talk about " + topic;
        messages.add(new Message("user", initialPrompt));
        do {
            String prompt = gpt.sendMessage(new MessageModel(modelName, messages, temperature)).getChoices().get(0).getMessage().getContent();
            messages.add(new Message("assistant", prompt));
            responses.add(new Message("user", prompt));
            gpt.log.new Info("Initiator: " + prompt);
            String response = responder.sendMessage(new MessageModel(modelName, responses, temperature)).getChoices().get(0).getMessage().getContent();
            messages.add(new Message("user", response));
            responses.add(new Message("assistant", response));
            responder.log.new Info("Responder: " + response);
            if (response.contains("bye") || prompt.contains("bye") || response.contains("Have a great day") || prompt.contains("Have a great day") ) break;
        }
        while (true);
    }
}
