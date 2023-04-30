package gpt.utilities;

import api_assured.Caller;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import gpt.api.GPT;
import gpt.exceptions.GptUtilityException;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import utils.FileUtilities;
import utils.ReflectionUtilities;
import utils.TextParser;

import java.util.ArrayList;
import java.util.List;

@Data
@SuppressWarnings("unused")
public class NameGenerator {

    private ReflectionUtilities reflectionUtilities = new ReflectionUtilities();
    private TextParser parser = new TextParser();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter;
    private List<Message> messages = new ArrayList<>();
    private MessageModel messageModel;
    private List<String> prompts;
    private boolean printResult;

    public FileUtilities.Json fileUtils = new FileUtilities.Json();
    private Double temperature;
    private String modelName;
    static boolean keepLogs;
    private GPT gpt;

    /**
     * Class representing a data generator that uses GPT to generate data with default parameters.
     *
     * @param gpt The GPT instance to use for data generation.
     */
    public NameGenerator(GPT gpt) {
        this.gpt = gpt;
        modelName = "gpt-3.5-turbo";
        this.temperature = 0.8;
        this.printResult = true;
        Caller.keepLogs(false);

        prepareObjectMapper();

        messages.add(new Message("user",
                "Please generate web element names that are respect to the given json and strict to the instructions down below " +
                        "Inspect the given url and use context to generate unique names " +
                        "Always prioritize context " +
                        "Do not include pages 'name' to the 'elementName' " +
                        "Do not include 'tagName' to the 'elementName' " +
                        "Don't give the same suffix or prefix for all names " +
                        "The name element always should be in this format: 'elementName': 'generated name' " +
                        "The generated name must be always in camel case format " +
                        "Respond only with the recreated json with selectors and elementName section " +
                        "If there is not an 'elementName' section, create for the related selectors " +
                        "Recreated json should have identical attributes and values with the original json "
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
    public NameGenerator(GPT gpt, String modelName, double temperature, boolean printResult, List<Message> messages) {
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
     * @param fieldExceptions an optional array of field names to be excluded from instantiation
     * @param <T>             the type of the object to be instantiated
     * @return an instance of the given class
     */
    public <T> String generateName(String jsonPath, String url, String... fieldExceptions) {
        try {
            String jsonString = getJsonData(jsonPath, url, fieldExceptions);
            gpt.log.new Info("Generating name...");
            String outputJson = objectWriter.writeValueAsString(jsonString);
            if (printResult)
                gpt.log.new Info(
                        "Generated names for the given elements");
            return jsonString;
        }
        catch (JsonProcessingException | NoSuchFieldException | ClassNotFoundException | GptUtilityException exception){
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generates fields of a given class.
     * This function takes a class as input and generates the fields of that class. The fields
     * are returned as an array of strings. Each string represents a field in the format "accessModifier
     * dataType fieldName". The access modifier can be "public", "private", or "protected". The data type
     * can be any valid data type in the language. The field name is the name of the field as defined in
     * the class.
     *
     * @param <T> The type of the class for which to generate fields.
     * @throws NoSuchFieldException if the class does not have any fields.
     * @throws JsonProcessingException if there is an error processing the class.
     * @throws ClassNotFoundException if the class cannot be found.
     * @return An array of strings representing the fields of the class in the format "accessModifier
     * dataType fieldName".
     */
    private <T> String getJsonData(String path, String url, String... exceptions) throws NoSuchFieldException, JsonProcessingException, ClassNotFoundException, GptUtilityException {
        gpt.log.new Info("Generating data...");
        JsonObject json = fileUtils.parseJsonFile(path);
        this.messages.add(new Message("user", "JSON: " + json + "\n" + "Website: " + url));
        MessageResponse messageResponse = gpt.sendMessage(
                new MessageModel(this.modelName, this.messages, this.temperature)
        );
        return messageResponse.getChoices().get(0).getMessage().getContent();
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
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NONE);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }
}