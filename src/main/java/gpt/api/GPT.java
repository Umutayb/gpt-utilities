package gpt.api;

import api_assured.ApiUtilities;
import api_assured.Caller;
import api_assured.ServiceGenerator;
import context.ContextStore;
import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.Headers;
import retrofit2.Call;
import utils.StringUtilities;

import static utils.StringUtilities.highlighted;

@Data
@EqualsAndHashCode(callSuper = true)
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
        ).setConnectionTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-timeout", "240"))
        ).setWriteTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-write-timeout", "120"))
        ).setReadTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-read-timeout", "120"))
        ).printHeaders(true).setRequestLogging(true).generate(GptServices.class);
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
        if (Caller.keepsLogs()) log.info("Messaging GPT model " + highlighted(StringUtilities.Color.BLUE, messageModel.getModel()));
        Call<MessageResponse> messageCall = gptServices.sendMessage(messageModel);
        return perform(messageCall, true , true);
    }
}
