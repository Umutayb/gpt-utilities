package gpt.models.message.standard;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class MessageModel extends gpt.models.message.MessageModel {
    String content;

    public MessageModel(String content, String role){
        super(role);
        this.content = content;
    }
}
