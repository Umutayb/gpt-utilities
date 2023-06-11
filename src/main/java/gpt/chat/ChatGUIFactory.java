package gpt.chat;

import gpt.api.GPT;
import gpt.chat.theme.SupportGUIDark;
import gpt.chat.theme.SupportGUILight;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public class ChatGUIFactory {

    @Getter
    public enum Theme {
        dark,
        light;
        public static Theme getTheme(String themeName){
            return Theme.valueOf(Objects.requireNonNull(Arrays.stream(Theme.class.getFields()).filter(field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                return fieldName.equalsIgnoreCase(themeName);
            }).findAny().orElse(null)).getName());
        }
    }

    public static ChatGUI getChatGUI(Theme theme, GPT gpt){
        switch (theme){
            case dark -> {return new SupportGUIDark(gpt);}
            case light -> {return new SupportGUILight(gpt);}
            default -> {return new SupportGUIDark(gpt);}
        }
    }
}
