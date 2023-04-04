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

    public GPT(String token) {
        gptServices = new ServiceGenerator(
                new Headers.Builder().add("Authorization","Bearer " + token).build()
        ).generate(GptServices.class);
    }

    public MessageResponse sendMessage(MessageModel messageModel) {
        log.new Info("Messaging GPT model " + strUtils.highlighted(StringUtilities.Color.BLUE, messageModel.getModel()));
        Call<MessageResponse> textCall = gptServices.sendMessage(messageModel);
        return perform(textCall, true , false);
    }
}
