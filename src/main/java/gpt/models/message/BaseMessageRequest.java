package gpt.models.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BaseMessageRequest<MessageType> {
    String model;
    List<MessageType> messages;
    Double temperature;
}
