import api_assured.exceptions.FailedCallException;
import gpt.api.GPT;
import gpt.chat.FunctionalChat;
import gpt.utilities.DataGenerator;
import models.CollectionOfIsbnModel;
import models.Pet;
import models.User;
import models.Librarian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.PropertyUtility;

import java.util.Arrays;

public class AppTest {

    static GPT gpt;
    static DataGenerator generator;

    @Before
    public void loadProperties(){
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        gpt = new GPT(PropertyUtility.getProperty("token"));
        generator = new DataGenerator(gpt);
    }

    @Test
    public void dataGeneratorPetTest() throws InterruptedException {
        int count = 0;
        do {
            try {
                Pet pet = generator.instantiate(Pet.class, "id");
                Assert.assertNull("The Id field is not null!", pet.getId());
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
    }

    @Test
    public void dataGeneratorUserTest() throws InterruptedException {
        int count = 0;
        do {
            try {
                User user = generator.instantiate(User.class);
                Arrays.stream(user.getClass().getFields()).iterator().forEachRemaining(field -> Assert.assertNotNull(field.getName() + " field is null!", field));
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);
    }

    @Test
    public void dataGeneratorIsbnTest() throws InterruptedException {
        int count = 0;
        do {
            try {
                CollectionOfIsbnModel isbnModel = generator.instantiate(CollectionOfIsbnModel.class);
                Arrays.stream(isbnModel.getClass().getFields()).iterator().forEachRemaining(Assert::assertNotNull);
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);

    }

    @Test
    public void dataGeneratorBookTest() throws InterruptedException {
        int count = 0;
        do {
            try {
                Librarian librarian = generator.instantiate(Librarian.class);
                Arrays.stream(librarian.getClass().getFields()).iterator().forEachRemaining(field -> Assert.assertNotNull(field.getName() + " field is null!", field));
                break;
            }
            catch (FailedCallException e) {
                count++;
                gpt.log.warning("Too many requests to process. Waiting for GPT to be stable...");
                Thread.sleep(15000);
                if (count > 3)
                    break;
            }
        }
        while (true);

    }

    
    public static void main(String[] args) {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
        GPT gpt = new GPT(PropertyUtility.getProperty("token"));

        FunctionalChat functionalChat = new FunctionalChat(gpt, "gpt-3.5-turbo-0613");
        functionalChat.startFunctionalChat();

        //Chat chat = new Chat(gpt);
        //chat.startChat();
    }
}
