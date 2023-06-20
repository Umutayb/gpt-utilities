package gpt.api.mongo;

import gpt.models.pickleib.CollectionModel;
import gpt.models.pickleib.MethodDocuments;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static gpt.api.mongo.MongoDBAPI.*;

public interface MongoDBServices {

    String BASE_URL = MongoDBAPI.BASE_URL;

    @POST(VER_PREFIX + ACT_PREFIX + FIND_SUFFIX)
    Call<MethodDocuments> getMethod(@Body CollectionModel collection);

}