# Gpt-Utilities

[![Maven Central](https://img.shields.io/maven-central/v/io.github.umutayb/Gpt-Utilities?color=brightgreen&label=Gpt-Utilities)](https://mvnrepository.com/artifact/io.github.umutayb/Gpt-Utilities/latest)

This library provides seamless integration with ChatGPT API in Java.

## Features

- Send messages to ChatGPT and get responses.
- Easy to integrate with any Java application.
- Supports customized ChatGPT configurations.

### Installation

To use Gpt-Utilities in your Maven project, add the following dependency to your pom.xml file:
```xml
<dependency>
    <groupId>com.github.umutayb</groupId>
    <artifactId>gpt-utilities</artifactId>
    <version>0.0.x</version>
</dependency>
```

To use Gpt-Utilities in your Gradle project, add the following dependency to your build.gradle file:
```
dependencies {
    implementation 'com.github.umutayb:gpt-utilities:0.0.x'
}

```
After updating your project, you are ready to chat!
___
### Usage

To integrate, instantiate GPT class and pass the token information in the constructor.

````java
import gpt.api.GPT;
import gpt.models.*;
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
    public void sendPrompt(List<Message> messages) {
        MessageModel messageModel = new MessageModel(gptModel, messages);
        MessageResponse messageResponse = gpt.sendMessage(messageModel);
        gpt.log.new Info("Waiting for the answer");
        gpt.log.new Info(messageResponse.getChoices().get(0).getMessage().getContent());
    }

}
 ````