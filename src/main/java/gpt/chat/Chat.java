package gpt.chat;

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
            gpt.log.new Info("Please type your message and press ENTER");
            String prompt = scanner.nextLine();
            if (prompt.equalsIgnoreCase("end")) retainChat = false;
            else messages.add(new Message("user", prompt));
            MessageResponse messageResponse = gpt.sendMessage(new MessageModel(modelName, messages, temperature));
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
}
