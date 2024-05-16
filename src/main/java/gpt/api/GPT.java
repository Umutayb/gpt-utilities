package gpt.api;

import api_assured.ApiUtilities;
import api_assured.Caller;
import api_assured.ServiceGenerator;
import context.ContextStore;
import gpt.models.message.BaseMessageRequest;
import gpt.models.message.MessageModel;
import gpt.models.message.standard.MessageRequest;
import gpt.models.message.MessageResponse;
import gpt.models.message.multicontent.MultiMessageRequest;
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
    boolean printLogs;

    /**
     * Instantiate GPT with a given bearer token
     *
     * @param token bearer token for gpt interaction
     */
    public GPT(String token) {
        printLogs = Boolean.parseBoolean(ContextStore.get("gpt-print-request-logs", "false"));
        gptServices = new ServiceGenerator(
                new Headers.Builder().add("Authorization","Bearer " + token).build()
        ).setConnectionTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-timeout", "240"))
        ).setWriteTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-write-timeout", "120"))
        ).setReadTimeout(
                Integer.parseInt(ContextStore.get("gpt-connection-read-timeout", "120"))
        ).printHeaders(printLogs).setRequestLogging(printLogs).generate(GptServices.class);
        Caller.keepLogs(printLogs);
        this.token = token;
    }

    /**
     * Sends a message to ChatGPT
     *
     * @param messageRequest containing model name, messages and temperature information (creativity score)
     * @return a response model containing response and usage information
     */
    public <MessageType> MessageResponse sendMessage(BaseMessageRequest<MessageType> messageRequest) {
        if (Caller.keepsLogs()) log.info("Messaging GPT model " + highlighted(StringUtilities.Color.BLUE, messageRequest.getModel()));
        Call<MessageResponse> messageCall = gptServices.sendMessage(messageRequest);
        return perform(messageCall, true , printLogs);
    }
}
