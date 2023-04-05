package gpt.api;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import okhttp3.Headers;
import retrofit2.Call;
import utils.StringUtilities;

public class GPT extends ApiUtilities {

    GptServices gptServices;

    /**
     * Instantiate GPT with a given bearer token
     *
     * @param token bearer token for gpt interaction
     */
    public GPT(String token) {
        gptServices = new ServiceGenerator(
                new Headers.Builder().add("Authorization","Bearer " + token).build()
        ).generate(GptServices.class);
    }

    /**
     * Sends message to ChatGPT
     *
     * @param messageModel containing model name, messages and temperature information (creativity score)
     * @return a response model containing response and usage information
     */
    public MessageResponse sendMessage(MessageModel messageModel) {
        log.new Info("Messaging GPT model " + strUtils.highlighted(StringUtilities.Color.BLUE, messageModel.getModel()));
        Call<MessageResponse> messageCall = gptServices.sendMessage(messageModel);
        return perform(messageCall, true , false);
    }
}
