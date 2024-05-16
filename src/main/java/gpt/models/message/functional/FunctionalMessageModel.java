package gpt.models.message.functional;

import gpt.models.message.multicontent.MultiContentMessageModel;
import gpt.models.function.FunctionCall;
import gpt.models.function.Function;
import gpt.models.content.Content;
import java.util.List;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FunctionalMessageModel<Type> extends MultiContentMessageModel {
    List<Function<Type>> functions;
    FunctionCall functionCall;

    public FunctionalMessageModel(String role, int max_tokens, List<Function<Type>> functions, String functionCall, Content... contents){
        super(role, max_tokens, contents);
        this.functions = functions;
        this.functionCall = new FunctionCall(functionCall);
    }

    public FunctionalMessageModel(String role, List<Function<Type>> functions, String functionCall, Content... contents){
        super(role, contents);
        this.functions = functions;
        this.functionCall = new FunctionCall(functionCall);
    }
}
