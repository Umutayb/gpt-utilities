package gpt.models.message.standard;

import gpt.models.message.BaseMessageRequest;
import gpt.models.message.MessageModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class MessageRequest extends BaseMessageRequest<MessageModel> {

    /**
     *
     * @param message specifies the initial text of the prompt.
     */
    public MessageRequest(String engine, List<MessageModel> message) {
        super(engine, message, 0.7);
    }

    /**
     *
     * @param messages specifies the initial text of the prompt.
     */
    public MessageRequest(String engine, List<MessageModel> messages, double temperature) {
        super(engine, messages, temperature);
    }
}
