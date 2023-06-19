package gpt.chat;

import api_assured.Caller;
import api_assured.exceptions.FailedCallException;
import gpt.api.GPT;
import gpt.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
@AllArgsConstructor
@SuppressWarnings("unused")
public class FunctionalChat {

    private List<String> prompts;
    private List<Message> messages = new ArrayList<>();
    private List<FunctionMessage> functionMessages = new ArrayList<>();
    private MessageModel messageModel;
    private String modelName;
    private Double temperature;
    private List<Function> function;
    private GPT gpt;
    private String functionCall;
    private FunctionMessage functionMessage;

    public FunctionalChat(List<String> prompts, GPT gpt){
        this.prompts = prompts;
        this.gpt = gpt;
        this.modelName = "gpt-3.5-turbo-0613";
        this.temperature = 0.5;
        this.function = List.of(setFunction());
        this.functionCall = "auto";

        Caller.keepLogs(true);
        for (String prompt:prompts) messages.add(new Message("user", prompt));
        this.prompts = new ArrayList<>(); //Flushing prompts after adding them to messages, preventing duplicate prompts
    }

    public FunctionalChat(GPT gpt, String modelName){
        this.gpt = gpt;
        this.modelName = modelName;
        this.temperature = 0.5;
        this.function = List.of(setFunction());
        this.functionCall = "auto";
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

    public void startFunctionalChat(){
        Scanner scanner = new Scanner(System.in);
        startFunctionChat(scanner);
        scanner.close();
    }

    public Function setFunction() {
        Function function1 = new Function();
        Function.Parameters parameters1 = new Function.Parameters();
        Function.Parameters.Properties properties1 = new Function.Parameters.Properties();
        Function.Parameters.Properties.Query query1 = new Function.Parameters.Properties.Query();


        query1.setType("string");
        query1.setDescription(
                "Suggest articles related to the question"
        );
        properties1.setQuery(query1);

        parameters1.setType("object");
        parameters1.setProperties(properties1);
        parameters1.setRequired(List.of(String.valueOf(query1)));

        function1.setParameters(parameters1);
        function1.setDescription("Use this function to suggest articles based on the user questions, find articles related to the context and suggest");
        function1.setName("articler");

        return function1;
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
            if (conversationCounter == 0) gpt.log.info("Enter your message (press Enter on an empty line to finish):");
            else  gpt.log.info("Your answer: ");
            StringBuilder prompt = new StringBuilder();
            String input;
            while (scanner.hasNextLine() && !(input = scanner.nextLine()).isEmpty()) prompt.append(input).append("\n");
            messages.add(new Message("user", prompt.toString()));
            gpt.log.info("Waiting for GPT...");
            try {
                MessageResponse messageResponse = gpt.sendMessage(new MessageModel(modelName, messages, temperature));
                Message message = messageResponse.getChoices().get(0).getMessage();
                messages.add(new Message(message.getRole(), message.getContent()));
                gpt.log.info(messageResponse.getChoices().get(0).getMessage().getContent());
                if (messageResponse.getChoices().get(0).getMessage().getContent().contains("bye")) break;
            }
            catch (FailedCallException failedCall){
                gpt.log.warning("Please make sure you have a valid token!");
                return;
            }

            conversationCounter++;
            if (conversationCounter >= conversationLimit) {
                retainChat = false;
                gpt.log.warning("Reached limited number of messages! Please start a new conversation.");
            }
        }
        while (retainChat);
    }


    public void startFunctionChat(Scanner scanner){
        int conversationCounter = 0;
        int conversationLimit = 100;
        boolean retainChat = true;
        do {
            if (prompts != null && prompts.size() > 0 ) for (String prompt:prompts) messages.add(new Message("user", prompt));
            if (conversationCounter == 0) gpt.log.info("Enter your message (press Enter on an empty line to finish):");
            else  gpt.log.info("Your answer: ");
            StringBuilder prompt = new StringBuilder();
            String input;
            while (scanner.hasNextLine() && !(input = scanner.nextLine()).isEmpty()) prompt.append(input).append("\n");
            functionMessages.add(new FunctionMessage("user", null, prompt.toString()));
            gpt.log.info("Waiting for GPT...");
            try {
                MessageResponse messageResponse = gpt.sendFunctionMessage(new FunctionModel(modelName, functionMessages, function));
                Message message = messageResponse.getChoices().get(0).getMessage();

                if (message.getContent() != null) {
                    messages.add(new Message(message.getRole(), message.getContent()));
                    gpt.log.info(messageResponse.getChoices().get(0).getMessage().getContent());
                }
                else if (messageResponse.getChoices().get(0).getFinish_reason().equals("function_call")){
                    functionMessages.add(new FunctionMessage("function",  setFunction().getName(), setFunction().getDescription()));
                    MessageResponse messageResponse2 = gpt.sendFunctionMessage(new FunctionModel(modelName, functionMessages, function));
                    gpt.log.info(messageResponse2.getChoices().get(0).getMessage().getContent());
                }
                else if (messageResponse.getChoices().get(0).getMessage().getContent().contains("bye")) break;
            }
            catch (FailedCallException failedCall){
                gpt.log.warning("Please make sure you have a valid token!");
                return;
            }

            conversationCounter++;
            if (conversationCounter >= conversationLimit) {
                retainChat = false;
                gpt.log.warning("Reached limited number of messages! Please start a new conversation.");
            }
        }
        while (retainChat);
    }

}
