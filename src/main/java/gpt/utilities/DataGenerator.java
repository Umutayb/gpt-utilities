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
import utils.MappingUtilities;
import utils.reflection.ReflectionUtilities;
import java.util.*;

import static utils.TextParser.parse;

@Data
@SuppressWarnings("unused")
public class DataGenerator {

    private ObjectMapper objectMapper = MappingUtilities.Json.mapper;
    private List<Message> messages = new ArrayList<>();
    private ObjectWriter objectWriter;
    private MessageModel messageModel;
    private List<String> prompts;
    private boolean printResult;
    private Double temperature;
    private String modelName;
    static boolean keepLogs;
    private GPT gpt;

    /**
     * Class representing a data generator that uses GPT to generate data with default parameters.
     *
     * @param gpt The GPT instance to use for data generation.
     */
    public DataGenerator(GPT gpt) {
        this.gpt = gpt;
        modelName = "gpt-3.5-turbo";
        this.temperature = 0.8;
        this.printResult = true;
        Caller.keepLogs(false);

        prepareObjectMapper();

        messages.add(new Message("user",
                "Please recreate the following json with randomised, creative and unique values that are meaningful with respect to the field names. "  +
                "Do not skip any field"  +
                "While generating values, always prioritise given value types over value names"  +
                "Respond only with the recreated json" +
                "Recreated json should have identical field names with the original json"
        ));
    }

    /**
     * Class representing a data generator that uses GPT to generate data based on given parameters.
     *
     * @param gpt The GPT instance to use for data generation.
     * @param modelName The name of the GPT model to use for data generation.
     * @param temperature The temperature to use for data generation.
     * @param printResult Whether to print the generated data to the console.
     * @param messages The list of messages to use for data generation.
     */
    public DataGenerator(GPT gpt, String modelName, double temperature, boolean printResult, List<Message> messages) {
        this.gpt = gpt;
        this.modelName = modelName;
        this.temperature = temperature;
        this.printResult = printResult;
        this.messages = messages;

        prepareObjectMapper();
    }

    /**
     * Instantiates an object of the given class and returns it.
     *
     * @param clazz           the class of the object to be instantiated
     * @param fieldExceptions an optional array of field names to be excluded from instantiation
     * @param <T>             the type of the object to be instantiated
     * @return an instance of the given class
     */
    public <T> T instantiate(Class<T> clazz, String... fieldExceptions) {
        int attemptCounter = 0;
        do {
            try {
                String jsonString = generateFieldData(clazz, fieldExceptions);
                gpt.log.info("Instantiating " + clazz.getSimpleName() + " object with generated data...");
                T instance = objectMapper.readValue(jsonString, clazz);
                String outputJson = objectWriter.writeValueAsString(instance);
                if (printResult)
                    gpt.log.info(
                            "An instance of " + clazz.getSimpleName() + " object has been instantiated as: \n" + outputJson
                    );
                return instance;
            }
            catch (JsonProcessingException | NoSuchFieldException | ClassNotFoundException | GptUtilityException exception) {
                attemptCounter++;
                if (attemptCounter > 5) {
                    gpt.log.error("Failed to instantiate " + clazz.getSimpleName() + " class!", exception);
                    return null;
            }
                else gpt.log.warning("Failed to instantiate " + clazz.getSimpleName() + " class. Trying again...");
            }
        }
        while (true);
    }

    /**
     * Generates fields of a given class.
     * This function takes a class as input and generates the fields of that class. The fields
     * are returned as an array of strings. Each string represents a field in the format "accessModifier
     * dataType fieldName." The access modifier can be "public", "private", or "protected". The data type
     * can be any valid data type in the language. The field name is the name of the field as defined in
     * the class.
     *
     * @param <T> The type of the class for which to generate fields.
     * @param clazz The class for which to generate fields.
     * @throws NoSuchFieldException if the class does not have any fields.
     * @throws JsonProcessingException if there is an error processing the class.
     * @throws ClassNotFoundException if the class cannot be found.
     * @return An array of strings representing the fields of the class in the format "accessModifier
     * dataType fieldName".
     */
    private <T> String generateFieldData(Class<T> clazz, String... exceptions) throws NoSuchFieldException, JsonProcessingException, ClassNotFoundException, GptUtilityException {
        gpt.log.info("Generating data for the " + clazz.getSimpleName() + " class...");
        JsonObject json = ReflectionUtilities.getJsonObject(clazz, new JsonObject(), exceptions);
        this.messages.add(new Message("user", clazz.getSimpleName() + " class JSON: " + json));
        MessageResponse messageResponse = gpt.sendMessage(
                new MessageModel(this.modelName, this.messages, this.temperature)
        );
        String response = messageResponse.getChoices().get(0).getMessage().getContent();
        if (!response.startsWith("{")) response = "{" + parse("{", null, response);
        return response;
    }

    /**
     * Sets whether to keep logs.
     *
     * @param keepLogs true if logs should be kept, false otherwise
     */
    public void keepsLogs(boolean keepLogs) {
        Caller.keepLogs(keepLogs);
    }

    private void prepareObjectMapper() {
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }
}