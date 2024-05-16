package gpt.models.message.multicontent;

import gpt.models.message.BaseMessageRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiMessageRequest extends BaseMessageRequest<MultiContentMessageModel> {
    String model;
    List<MultiContentMessageModel> messages;
    Double temperature;

    /**
     *
     * @param messages specifies the initial text of the prompt.
     */
    public MultiMessageRequest(String model, List<MultiContentMessageModel> messages) {
        super(model, messages, 0.7);
    }
}
