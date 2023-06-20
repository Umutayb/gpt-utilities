package gpt.api.mongo;

import api_assured.ApiUtilities;
import api_assured.Caller;
import gpt.models.pickleib.CollectionModel;
import gpt.models.pickleib.MethodDocuments;
import gpt.models.pickleib.MethodResponseModel;
import utils.ReflectionUtilities;
import utils.StringUtilities;
import utils.TextParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DBInteraction extends ApiUtilities {

    private final MongoDB mongoDB = new MongoDB();
    private final StringUtilities strUtils = new StringUtilities();
    private final TextParser parser = new TextParser();
    private final ReflectionUtilities objUtils = new ReflectionUtilities();

    public DBInteraction() {
        Caller.keepLogs(false);
    }

    public String getMethod(String methodName) {
        String response;
        String pattern = "(?<=\\b)(_id=|method=|packageName=|className=|methodName=|methodDescription=|returnType=|returnDescription=|inputs=|innerMethod=)";

        CollectionModel.Filter filter = new CollectionModel.Filter(methodName);
        CollectionModel collection = new CollectionModel("methods", filter);
        response = mongoDB.getMethodByName(collection) + "stop";
        response = parser.parse("MethodDocuments(documents=[MethodResponseModel","])stop", response);
        response = response.replaceAll(pattern, "\n$1");

        return response;
    }

    public MethodDocuments getAllMethods() {
        CollectionModel.Filter filter = new CollectionModel.Filter();
        CollectionModel collection = new CollectionModel("methods", filter);
        return mongoDB.getMethodByName(collection);
    }

    public List<String> getMethodFieldInfo(String targetFieldName) {
        ArrayList<String> methodNameList = new ArrayList<>();
        try {
            for (MethodResponseModel methodModel : getAllMethods().getDocuments()) {
                Method getter = objUtils.getMethod("get" + strUtils.firstLetterCapped(targetFieldName), methodModel);
                methodNameList.add(getter.invoke(methodModel).toString());
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {e.getMessage();}

        return methodNameList;
    }

}
