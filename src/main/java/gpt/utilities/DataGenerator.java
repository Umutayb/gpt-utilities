package gpt.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gpt.api.GPT;
import gpt.exceptions.GptUtilityException;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import java.lang.reflect.*;
import java.util.*;

@Data
public class DataGenerator {

    private ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Message> messages = new ArrayList<>();
    private MessageModel messageModel;
    private List<String> prompts;
    private Double temperature;
    private boolean printResult;
    private String modelName;
    private GPT gpt;

    public DataGenerator(GPT gpt) {
        this.gpt = gpt;
        this.modelName = "gpt-3.5-turbo";
        this.temperature = 0.8;
        this.printResult = true;

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

    public <T> T instantiate(Class<T> type) {
        try {
            T instance = generateInstanceOf(type);
            String json = objectWriter.writeValueAsString(instance);
            if (printResult)
                gpt.log.new Info(
                        "An instance of " + type.getSimpleName() + " object has been created as: \n" + json
                );
            return instance;
        }
        catch (JsonProcessingException | NoSuchFieldException | ClassNotFoundException | GptUtilityException exception){
            exception.printStackTrace();
            return null;
        }
    }

    private <T> T generateInstanceOf(Class<T> type) throws NoSuchFieldException, JsonProcessingException, ClassNotFoundException, GptUtilityException {
        JsonObject json = getJsonObject(type, new JsonObject());
        this.messages.add(new Message("user", "JSON: " + json));
        MessageResponse messageResponse = gpt.sendMessage(
                new MessageModel(this.modelName, this.messages, this.temperature)
        );
        String response = messageResponse.getChoices().get(0).getMessage().getContent();
        if (response.startsWith("JSON:")) response = response.replace("JSON:", "").trim();
        return objectMapper.readValue(response, type);
    }

    private <T> JsonObject getJsonObject(Class<T> clazz, JsonObject json) throws NoSuchFieldException, ClassNotFoundException, GptUtilityException {
        List<Field> fields = Arrays.stream(clazz.getFields()).toList();
        if (fields.size() == 0) throw new GptUtilityException("Please make sure fields of " + clazz.getSimpleName() + " class are set to public.");
        for (Field field:fields) {
            boolean isMember = field.getType().isMemberClass();
            boolean isList = isOfType(field, "List");
            if (!isList && !isMember)
                json.addProperty(field.getName(), field.getType().getName());
            else if (!isList)
                json.add(field.getName(), getJsonObject(clazz.getField(field.getName()).getType(), new JsonObject()));
            else if (isMemberList(clazz, field))
                json.add(field.getName(), getJsonArray(field));
            if (isList && isPrimitive(field)){
                JsonArray array = new JsonArray();
                List<String> list = List.of(getTypeName(field));
                for (String jsonObject : list) array.add(jsonObject);
                json.add(field.getName(), array);
            }
        }
        return json;
    }

    private boolean isOfType(Field field, String expectedType){
        return field.getType().getTypeName().contains(expectedType);
    }

    private JsonArray getJsonArray(Field field) throws ClassNotFoundException, NoSuchFieldException, GptUtilityException {
        JsonArray array = new JsonArray();
        List<JsonObject> list = List.of(
                getJsonObject(Class.forName(
                                ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName()
                        ),
                        new JsonObject()
                )
        );
        for (JsonObject jsonObject : list) array.add(jsonObject);
        return array;
    }

    private <T> boolean isMemberList(Class<T> clazz, Field field){
        List<Field> fields = List.of(clazz.getFields());
        return fields.stream().anyMatch(
                subField -> subField.getGenericType().getTypeName().equals(field.getGenericType().getTypeName())
        );
    }

    private boolean isPrimitive(Field field){
        return switch (getTypeName(field)) {
            case "java.lang.Integer",
                    "java.lang.Boolean",
                    "java.lang.Char",
                    "java.lang.Double",
                    "java.lang.Long",
                    "java.lang.Short",
                    "java.lang.Byte",
                    "java.lang.String"
                    -> true;
            default -> false;
        };
    }

    private String getTypeName(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return type.getActualTypeArguments()[0].getTypeName();
    }
}

