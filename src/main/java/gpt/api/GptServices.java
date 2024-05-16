package gpt.api;

import gpt.models.message.BaseMessageRequest;
import gpt.models.message.standard.MessageRequest;
import gpt.models.message.MessageResponse;
import gpt.models.message.multicontent.MultiMessageRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static gpt.api.GptAPI.*;

/**
 * This interface defines the services provided by the GPT API.
 */
@SuppressWarnings("unused")
interface GptServices {

    /**
     * Base URL for the GPT API.
     */
    String BASE_URL = GptAPI.BASE_URL;

    /**
     * Sends a message to the GPT using an HTTP POST request.
     *
     * @param multiMessageRequest A {@link MultiMessageRequest} object that contains the message to be sent.
     * @param <MessageType>       The type of the message content.
     * @return A {@link Call} representing the HTTP request. The response from the server is a {@link MessageResponse} object.
     */
    @POST(VERSION_INFIX + CHAT_INFIX + COMPLETIONS_SUFFIX)
    <MessageType> Call<MessageResponse> sendMessage(@Body BaseMessageRequest<MessageType> multiMessageRequest);
}
