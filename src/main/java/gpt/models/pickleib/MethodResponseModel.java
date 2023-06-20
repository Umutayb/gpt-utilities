package gpt.models.pickleib;

import lombok.Data;

import java.util.List;

@Data
public class MethodResponseModel {

    String _id;
    String method;
    String packageName;
    String className;
    String methodName;
    String methodDescription;
    String returnType;
    List<Inputs> inputs;
    String returnDescription;
    String innerMethod;

    @Data
    public static class Inputs{
        String name;
        String type;
        String description;

    }

}
