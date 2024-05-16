import api_assured.Caller;
import api_assured.exceptions.FailedCallException;
import context.ContextStore;
import gpt.api.GPT;
import gpt.chat.ui.theme.SupportGUIDark;
import gpt.chat.ui.theme.SupportGUILight;
import gpt.enums.ContentType;
import gpt.models.content.Content;
import gpt.models.message.MessageResponse;
import gpt.models.message.multicontent.MultiContentMessageModel;
import gpt.models.message.multicontent.MultiMessageRequest;
import gpt.utilities.DataGenerator;
import models.CollectionOfIsbnModel;
import models.Pet;
import models.User;
import models.Librarian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.FileUtilities;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AppTest {

    public static void main(String[] args) {
        ContextStore.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(ContextStore.get("token"));
        Caller.keepLogs(false);
        SupportGUILight gui = new SupportGUILight(gpt);
        SupportGUIDark guiDark = new SupportGUIDark(gpt);
        guiDark.start();
    }

    static GPT gpt;
    static DataGenerator generator;

    @Before
    public void loadProperties(){
        ContextStore.loadProperties("src/test/resources/test.properties");
        gpt = new GPT(ContextStore.get("token"));
        generator = new DataGenerator(gpt);
    }

    @Test
    public void dataGeneratorTest() throws InterruptedException {
        int count = 0;
        Pet pet = null;
        do {
            try {
                pet = generator.instantiate(Pet.class, "id");
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process or token is out of credit. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
        Assert.assertNull("The Id field is not null!", pet.getId());
    }

    @Test
    public void dataGeneratorPetTest() throws InterruptedException {
        int count = 0;
        Pet pet = null;
        do {
            try {
                pet = generator.instantiate(Pet.class, "id");
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process or token is out of credit. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
        assert pet != null;
        Assert.assertNull("The Id field is not null!", pet.getId());
    }

    @Test
    public void dataGeneratorUserTest() throws InterruptedException {
        int count = 0;
        User user = null;
        do {
            try {
                user = generator.instantiate(User.class);
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process or token is out of credit. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
        assert user != null;
        Arrays.stream(user.getClass().getFields()).iterator().forEachRemaining(field -> Assert.assertNotNull(field.getName() + " field is null!", field));
    }

    @Test
    public void dataGeneratorIsbnTest() throws InterruptedException {
        int count = 0;
        CollectionOfIsbnModel isbnModel = null;
        do {
            try {
                isbnModel = generator.instantiate(CollectionOfIsbnModel.class);
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process or token is out of credit. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
        assert isbnModel != null;
        Arrays.stream(isbnModel.getClass().getFields()).iterator().forEachRemaining(Assert::assertNotNull);
    }

    @Test
    public void dataGeneratorBookTest() throws InterruptedException {
        int count = 0;
        Librarian librarian = null;
        do {
            try {
                librarian = generator.instantiate(Librarian.class);
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process or token is out of credit. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
        assert librarian != null;
        Arrays.stream(librarian.getClass().getFields()).iterator().forEachRemaining(field -> Assert.assertNotNull(field.getName() + " field is null!", field));
    }

    @Test
    public void imageRecognitionTest() throws InterruptedException, IOException {
        Content textContent = new Content(ContentType.text, "Write a test case for the following window.");
        Content imageContent = new Content(ContentType.image_url, FileUtilities.getEncodedString("src/test/resources/file/LightMode.png"));

        MultiContentMessageModel message = new MultiContentMessageModel("user", textContent, imageContent);

        MultiMessageRequest messageModel = new MultiMessageRequest("gpt-4o", List.of(message));
        MessageResponse response = gpt.sendMessage(messageModel);
        gpt.log.info(response.getChoices().get(0).getMessage().getContent());
        Assert.assertTrue("The response does not contain the keyword 'Chat'", response.getChoices().get(0).getMessage().getContent().contains("Chat"));
        gpt.log.success("'imageRecognitionTest()' test pass!");
    }

    @Test
    public void openAIFunctionsTest(){

    }
}
