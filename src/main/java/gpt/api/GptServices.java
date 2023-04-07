package gpt.api;

import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static gpt.api.GptAPI.*;

@SuppressWarnings("unused")
interface GptServices {

    String BASE_URL = GptAPI.BASE_URL;

    /**
     * This method is used to send a message to a GPT using an HTTP POST request.
     *
     * @param messageModel A MessageModel object that contains the message to be sent.
     * @return A Call that represents the HTTP request. The response from the server is a MessageResponse object.
     */
    @POST(VERSION_INFIX + CHAT_INFIX + COMPLETIONS_SUFFIX)
    Call<MessageResponse> sendMessage(@Body MessageModel messageModel);
}
