package gpt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor @Data
public class FunctionMessageModel {
    String model;
    List<FunctionMessage> messages;
    Double temperature;
}
