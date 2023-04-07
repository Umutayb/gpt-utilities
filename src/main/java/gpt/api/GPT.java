package gpt.api;

import api_assured.ApiUtilities;
import api_assured.Caller;
import api_assured.ServiceGenerator;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import okhttp3.Headers;
import retrofit2.Call;
import utils.StringUtilities;

@Data
public class GPT extends ApiUtilities {

    GptServices gptServices;
    String token;

    /**
     * Instantiate GPT with a given bearer token
     *
     * @param token bearer token for gpt interaction
     */
    public GPT(String token) {
        gptServices = new ServiceGenerator(
                new Headers.Builder().add("Authorization","Bearer " + token).build()
        ).setConnectionTimeout(240).setWriteTimeout(120).setReadTimeout(120).setPrintHeaders(false).generate(GptServices.class);
        Caller.keepLogs(false);
        this.token = token;
    }

    /**
     * Sends message to ChatGPT
     *
     * @param messageModel containing model name, messages and temperature information (creativity score)
     * @return a response model containing response and usage information
     */
    public MessageResponse sendMessage(MessageModel messageModel) {
        if (Caller.keepsLogs()) log.new Info("Messaging GPT model " + strUtils.highlighted(StringUtilities.Color.BLUE, messageModel.getModel()));
        Call<MessageResponse> messageCall = gptServices.sendMessage(messageModel);
        return perform(messageCall, true , false);
    }
}
