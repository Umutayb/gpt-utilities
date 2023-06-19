package gpt.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FunctionMessage {
    String role;
    String name;
    String content;
}
