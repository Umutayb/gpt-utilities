package gpt.api.mongo;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import gpt.models.pickleib.CollectionModel;
import gpt.models.pickleib.MethodDocuments;
import okhttp3.Headers;

import retrofit2.Call;
import utils.PropertyUtility;
import utils.StringUtilities;


public class MongoDB extends ApiUtilities {

    static {
        PropertyUtility.loadProperties("src/test/resources/test.properties");
    }
    MongoDBServices mongoDb = new ServiceGenerator().setHeaders(
            Headers.of("apiKey", "lBpNJNsyJ7NZMJNXKkUaMGHuxHDrn3CExr5hC5BshkJn6sQxzqyE93sPyjYbAt5F")
    ).setPrintHeaders(false).generate(MongoDBServices.class);

    public MethodDocuments getMethodByName(CollectionModel collection) {
        log.info("Getting method info from database: " + strUtils.highlighted(StringUtilities.Color.BLUE, collection.getDatabase()));
        Call<MethodDocuments> methodCall = mongoDb.getMethod(collection);
        return perform(methodCall, false, false);
    }

}
