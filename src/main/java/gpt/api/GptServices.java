package gpt.api;

import gpt.models.MessageModel;
import gpt.models.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static gpt.api.GptAPI.*;

interface GptServices {

    String BASE_URL = GptAPI.BASE_URL;

    @POST(VERSION_INFIX + CHAT_INFIX + COMPLETIONS_SUFFIX)
    Call<MessageResponse> sendMessage(@Body MessageModel messageModel);
}
