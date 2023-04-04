package gpt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {

    String model;
    List<Message> messages;
    Double temperature;

    /**
     *
     * @param message specifies the initial text of the prompt.
     */

    public MessageModel(String engine, List<Message> message) {
        this.messages = message;
        this.model = engine;
        this.temperature = 0.7;
    }

}
