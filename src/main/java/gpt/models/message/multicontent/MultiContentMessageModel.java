package gpt.models.message.multicontent;

import gpt.models.content.Content;
import gpt.models.message.MessageModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiContentMessageModel extends MessageModel {
    List<Content> content;
    int max_tokens;

    public MultiContentMessageModel(String role, int max_tokens, Content... contents){
        super(role);
        this.content = List.of(contents);
        this.max_tokens = max_tokens;
    }

    public MultiContentMessageModel(String role, Content... contents){
        super(role);
        this.content = List.of(contents);
    }
}
