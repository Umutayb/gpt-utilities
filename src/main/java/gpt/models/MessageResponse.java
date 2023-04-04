package gpt.models;

import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
public class MessageResponse {

    String id;
    String object;
    Double created;
    String model;
    Usage usage;
    List<Choices> choices;

    @Data
    public static class Usage{
        Double prompt_tokens;
        Double completion_tokens;
        Double total_tokens;
    }

    @Data
    public static class Choices {
        Message message;
        String finish_reason;
        Double index;
    }

}
