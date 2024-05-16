package gpt.models.content;

import gpt.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Content {
    String type;
    String text;
    Content.ImageUrl image_url;

    public Content(ContentType type, String content) {
        this.type = type.name();
        switch (type){
            case image_url -> image_url = new Content.ImageUrl("data:image/jpeg;base64,{" + content + "}");
            case text -> text = content;
        }
    }

    @Data
    @AllArgsConstructor
    public static class ImageUrl {
        String url;
    }
}
