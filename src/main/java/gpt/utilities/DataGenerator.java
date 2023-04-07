package gpt.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gpt.api.GPT;
import gpt.models.Message;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;
import utils.TextParser;

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
            System.out.println(json);
            if (printResult)
                gpt.log.new Info(
                        "An instance of " + type.getSimpleName() + " object has been created as: \n" + json
                );
            return instance;
        }
        catch (JsonProcessingException | NoSuchFieldException | ClassNotFoundException exception){
            exception.printStackTrace();
            return null;
        }
    }

    private <T> T generateInstanceOf(Class<T> type) throws NoSuchFieldException, JsonProcessingException, ClassNotFoundException {
        DataGenerator dataGenerator = new DataGenerator(gpt);
        JsonObject json = getJsonObject(type, new JsonObject());
        System.out.println(json);
        dataGenerator.messages.add(new Message("user", "JSON: " + json));
        MessageResponse messageResponse = gpt.sendMessage(
                new MessageModel(dataGenerator.modelName, dataGenerator.messages, dataGenerator.temperature)
        );
        String response = messageResponse.getChoices().get(0).getMessage().getContent();
        System.out.println(response);
        if (response.startsWith("JSON:")) response = response.replace("JSON:", "").trim();
        return objectMapper.readValue(response, type);
    }

    private <T> JsonObject getJsonObject(Class<T> clazz, JsonObject json) throws NoSuchFieldException, ClassNotFoundException {
        List<Field> fields = Arrays.stream(clazz.getFields()).toList();
        for (Field field:fields) {
            System.out.println("Field type: " + field.getType().isInterface());
            System.out.println("Field : " + field.getType());
            if (!field.getType().isInterface() && !listFieldTypePrimitive(field))
                json.add(field.getName(), getJsonObject(clazz.getField(field.getName()).getType(), new JsonObject()));
            else if (field.getType().isInterface()) {
                if (!listFieldTypeString(field) && isMemberList(clazz, field)){
                    JsonArray array = new JsonArray();
                    List<JsonObject> list = List.of(
                            getJsonObject(Class.forName(
                                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName()
                                    ),
                                    new JsonObject()
                            )
                    );
                    for (JsonObject jsonObject : list) array.add(jsonObject);
                    json.add(field.getName(), array);
                }
                else if (!listFieldTypeString(field) && listFieldTypePrimitive(field)){
                    System.out.println("WHAT " + field.getGenericType().getTypeName());
                    json.addProperty(field.getName(), field.getGenericType().getTypeName());
                }
                else if (listFieldTypeString(field)){
                    JsonArray array = new JsonArray();
                    List<JsonElement> list = List.of(
                            getJsonObject(Class.forName(
                                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName()
                                    ),
                                    new JsonObject()
                            ).getAsJsonPrimitive()
                    );

                    for (JsonElement jsonObject : list) array.add(jsonObject);
                    json.add(field.getName(), array);
                }


            }
            else
                json.addProperty(field.getName(), field.getType().getName());
        }
        return json;
    }

    private boolean listFieldTypeString(Field field){
        ParameterizedType type = ((ParameterizedType) field.getGenericType());
        return type.getActualTypeArguments()[0].getTypeName().contains("java.lang.String");
    }

    private <T> boolean listFieldTypePrimitive(Field field) {
        try {
            ParameterizedType pValueType = ((ParameterizedType) field.getGenericType());
            TextParser parser = new TextParser();
            System.out.println("HERE: " + pValueType.getActualTypeArguments()[0].getTypeName());
            String className = parser.parse("java.lang.", "]", pValueType.getActualTypeArguments()[0].getTypeName());
            switch (className){
                case "Integer", "Boolean", "Char", "Double", "Long", "Short", "Byte":
                    return true;
                default:
                    return false;
            }
        }catch (Exception exception){
            return true;
        }
    }

    private <T> boolean isMemberList(Class<T> clazz, Field field){
        List<Field> fields = List.of(clazz.getFields());
        return fields.stream().anyMatch(
                subField -> subField.getGenericType().getTypeName().equals(field.getGenericType().getTypeName())
        );
    }


    private <T> JsonArray getJsonObject(List<Class<T>> types, JsonArray jsonArray) throws NoSuchFieldException {
        for (Class<T> type:types) {

        }

        return jsonArray;
    }
}

