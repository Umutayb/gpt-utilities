package gpt.utilities;

import api_assured.Caller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonObject;
import gpt.api.GPT;
import gpt.exceptions.GptUtilityException;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import utils.ReflectionUtilities;

import java.util.*;

@Data
public class DataGenerator {

    private ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private ReflectionUtilities reflectionUtilities = new ReflectionUtilities();
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Message> messages = new ArrayList<>();
    private MessageModel messageModel;
    private List<String> prompts;
    private boolean printResult;
    private Double temperature;
    private String modelName;
    static boolean keepLogs;
    private GPT gpt;

    public DataGenerator(GPT gpt) {
        this.gpt = gpt;
        modelName = "gpt-3.5-turbo";
        this.temperature = 0.8;
        this.printResult = true;
        Caller.keepLogs(false);

        messages.add(new Message("user",
                "Please recreate the following json with randomised, creative and unique values that are meaningful with respect to the field names. "  +
                "Do not skip any field."  +
                "While generating values, always prioritise given value types over value names"  +
                "Response should only contain the json itself!"
        ));
    }


    public DataGenerator(GPT gpt, String modelName, double temperature, boolean printResult, List<Message> messages) {
        this.gpt = gpt;
        this.modelName = modelName;
        this.temperature = temperature;
        this.printResult = printResult;
        this.messages = messages;
    }

    /**
     * Instantiates a given class with generated values
     * @param clazz target class
     * @return generated instance
     * @param <T> target class type
     */
    public <T> T instantiate(Class<T> clazz) {
        try {
            String jsonString = generateFieldData(clazz);
            gpt.log.new Info("Instantiating " + clazz.getSimpleName() + " object with generated data...");
            T instance = objectMapper.readValue(jsonString, clazz);
            String outputJson = objectWriter.writeValueAsString(instance);
            if (printResult)
                gpt.log.new Info(
                        "An instance of " + clazz.getSimpleName() + " object has been instantiated as: \n" + outputJson
                );
            return instance;
        }
        catch (JsonProcessingException | NoSuchFieldException | ClassNotFoundException | GptUtilityException exception){
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generates fields of a given class
     * @param clazz target class
     * @return 
     * @param <T>
     * @throws NoSuchFieldException
     * @throws JsonProcessingException
     * @throws ClassNotFoundException
     * @throws GptUtilityException
     */
    private <T> String generateFieldData(Class<T> clazz) throws NoSuchFieldException, JsonProcessingException, ClassNotFoundException, GptUtilityException {
        gpt.log.new Info("Generating data for the " + clazz.getSimpleName() + " class...");
        JsonObject json = reflectionUtilities.getJsonObject(clazz, new JsonObject());
        this.messages.add(new Message("user", "JSON: " + json));
        MessageResponse messageResponse = gpt.sendMessage(
                new MessageModel(this.modelName, this.messages, this.temperature)
        );
        String response = messageResponse.getChoices().get(0).getMessage().getContent();
        if (response.startsWith("JSON:")) response = response.replace("JSON:", "").trim();
        return response;
    }

    public void keepsLogs(boolean keepLogs) {
        Caller.keepLogs(keepLogs);
    }

}