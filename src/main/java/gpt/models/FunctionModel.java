package gpt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor @Data
public class FunctionModel {

    String model;
    List<FunctionMessage> messages;
    List<Function> functions;
}
