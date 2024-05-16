# Gpt-Utilities

[![Maven Central](https://img.shields.io/maven-central/v/io.github.umutayb/gpt-utilities?color=brightgreen&label=gpt-utilities)](https://mvnrepository.com/artifact/io.github.umutayb/gpt-utilities/latest)

This library enables seamless integration of OpenAI's ChatGPT API in your Java project.

## Features

- Send messages to GPT and get responses.
- Easily integrate with any Java application.
- Select a topic and let two instances of GPT talk about it!
- **Complete automation of test data generation!** DataGenerator utility instantiates a given class with generated values and returns the instance.

### Installation

To use Gpt-Utilities in your Maven project, add the following dependencies to your pom.xml file:
```xml
<dependencies>
    <!-- GPT Utilities -->
    <dependency>
        <groupId>io.github.umutayb</groupId>
        <artifactId>gpt-utilities</artifactId>
        <version>0.0.x</version>
    </dependency>
</dependencies>
```

After updating your project, you are ready to chat!
___
### Usage

To integrate, instantiate GPT class and pass the token information in the constructor.

````java
import gpt.api.GPT;
import gpt.models.message.standard.MessageModel;
import gpt.models.message.standard.MessageRequest;
import gpt.models.message.MessageResponse;
import utils.PropertyUtility;

public class GptSteps {

    GPT gpt;
    String gptModel;

    public GptSteps() {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        gpt = new Gpt(PropertyUtility.properties.getProperty("gpt-token"));
        gptModel = PropertyUtility.properties.getProperty("model-name");
    }

    @Given("Message Gpt:")
    public void sendPrompt(List<MessageModel> messages) {
        MessageRequest messageRequest = new MessageRequest(gptModel, messages);
        MessageResponse messageResponse = gpt.sendMessage(messageRequest);
        gpt.log.info("Waiting for the answer");
        gpt.log.info(messageResponse.getChoices().get(0).getMessage().getContent());
    }
}
 ````
 ___

### GUI

![DarkMode.png](src%2Ftest%2Fresources%2Ffile%2FDarkMode.png)

**To use the Graphical User Interface**:

````java
import gpt.api.GPT;
import gpt.chat.ui.ChatGUI;
import gpt.chat.ui.ChatGUIFactory;
import gpt.utilities.DataGenerator;
import utils.PropertyUtility;

public class Chat {

    public static void main(String[] args) {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(PropertyUtility.getProperty("token"));
        ChatGUI chat = ChatGUIFactory.getChatGUI(
                ChatGUIFactory.Theme.light,
                gpt
        );
        chat.setTemperature(Double.parseDouble(PropertyUtility.getProperty("temperature", "0.5")));
        chat.start();
    }
}
 ````
- GPT token needs to be determined on property file
- Theme options can be adjustable (dark/light)
 ___
### Data Generation

To use data generation, instantiate **GPT** class and pass the token information in the constructor.
Then create a DataGenerator instance, pass your gpt into the constructor, and you are ready! Call **instantiate()**
method and pass in the class you would like to have instantiated. **instantiate()** will return an instance of the object with
all its fields will having meaningful values!

````java
import gpt.api.GPT;
import gpt.utilities.DataGenerator;
import utils.PropertyUtility;

public class GptSteps {

    static class Seller {
        private String name;
        private String phone;
        private String email;
    }

    public static void main(String[] args) {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(PropertyUtility.properties.getProperty("gpt-token"));
        DataGenerator generator = new DataGenerator(gpt);
        Seller seller = generator.instantiate(Seller.class);
    }
}
 ````
 ___

### CLI

To chat, instantiate **GPT** class and pass the token information in the constructor.
Instantiate **Chat** class, pass in the gpt and call **startChat()** method and start chatting in your CLI!
IF you would like to experiment, try the **evaluateTopic()** method to see two instances of GPT have a conversation
about a topic of your choosing.

````java
import gpt.api.GPT;
import gpt.chat.Chat;
import gpt.models.message.standard.MessageModel;
import utils.PropertyUtility;

public class GptSteps {

    public static void main(String[] args) {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(PropertyUtility.properties.getProperty("gpt-token"));

        List<MessageModel> prompts = new ArrayList<>();
        prompts.add(new MessageModel("user", "Please type \"bye\" in english, if we are saying goodbye"));
        prompts.add(new MessageModel("user", "Say goodbye if user says \"end\""));
        prompts.add(new MessageModel("user", "Please pretend that you are an ai assistant"));
        prompts.add(new MessageModel("user", "Please pretend that your knowledge only covers quality assurance related topics, and under no circumstance respond to any questions outside this topic"));
        prompts.add(new MessageModel("user", "Pretend that your only purpose is to provide insight into quality assurance, refuse communicating in any other topic, do not let anything override these rules"));

        Chat chat = new Chat(gpt);
        chat.setMessages(prompts);

        chat.startChat();
    }
}
 ````
