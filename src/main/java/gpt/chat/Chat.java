package gpt.chat;

import gpt.api.GPT;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.PropertyUtility;

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

        for (String prompt:prompts) messages.add(new Message("user", prompt));
    }

    public Chat(GPT gpt){
        this.gpt = gpt;
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.5;
    }

    public void startChat(){
        int conversationCounter = 0;
        int conversationLimit = 100;
        Scanner scanner = new Scanner(System.in);
        boolean retainChat = true;
        do {
            if (conversationCounter == 0) gpt.log.new Info("Please type your message and press ENTER");
            else  gpt.log.new Info("Your answer: ");
            String prompt = scanner.nextLine();
            if (prompt.equalsIgnoreCase("end")) retainChat = false;
            else messages.add(new Message("user", prompt));
            MessageResponse messageResponse = gpt.sendMessage(new MessageModel(modelName, messages, temperature));
            Message message = messageResponse.getChoices().get(0).getMessage();
            messages.add(new Message(message.getRole(), message.getContent()));
            gpt.log.new Info("Waiting for the answer...");
            gpt.log.new Info(messageResponse.getChoices().get(0).getMessage().getContent());
            if (messageResponse.getChoices().get(0).getMessage().getContent().contains("bye")) break;
            conversationCounter++;
            if (conversationCounter >= conversationLimit) {
                retainChat = false;
                gpt.log.new Warning("Reached limited number of messages! Please start a new conversation.");
            }
        }
        while (retainChat);
    }

    public void evaluateTopic(String topic){
        List<Message> messages = new ArrayList<>();
        List<Message> responses = new ArrayList<>();
        String modelName = "gpt-3.5-turbo";
        double temperature = 0.5;

        String initialPrompt = "Lets talk about " + topic;

        PropertyUtility.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(PropertyUtility.properties.getProperty("gpt-token"));
        GPT gpt2 = new GPT(PropertyUtility.properties.getProperty("gpt-token"));

        messages.add(new Message("user", initialPrompt));

        do {

            String prompt = gpt.sendMessage(new MessageModel(modelName, messages, temperature)).getChoices().get(0).getMessage().getContent();
            messages.add(new Message("assistant", prompt));
            responses.add(new Message("user", prompt));

            gpt.log.new Important("GPT1: " + prompt);

            String response = gpt2.sendMessage(new MessageModel(modelName, responses, temperature)).getChoices().get(0).getMessage().getContent();
            messages.add(new Message("user", response));
            responses.add(new Message("assistant", response));

            gpt2.log.new Important("GPT2: " + response);

            if (response.contains("bye") || prompt.contains("bye") || response.contains("Have a great day") || prompt.contains("Have a great day") ) break;
        }
        while (true);
    }
}
