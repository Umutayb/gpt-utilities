package gpt.models.message;

import lombok.Data;

@Data
public class MessageModel {
    public String role;
    public Object content;

    public MessageModel(String role, Object content){
        this.role = role;
        this.content = content;
    }

    public MessageModel(String role){
        this.role = role;
    }
}
