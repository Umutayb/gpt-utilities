package gpt.models.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Function <Type> {
    String name;
    Parameter<Type> parameters;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter <Type> {
        String type;
        Type parameters;
        List<String> required;
    }
}
