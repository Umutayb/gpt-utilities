package gpt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Data
public class Function {
    public String name;
    public String description;
    public Parameters parameters;

    @AllArgsConstructor @NoArgsConstructor @Data
    public static class Parameters{
        public String type;
        public List<String> required;
        public Properties properties;

        @AllArgsConstructor @NoArgsConstructor @Data
        public static class Properties{
            public Query query;

            @AllArgsConstructor @NoArgsConstructor @Data
            public static class Query{
                public String type;
                public String description;
            }

        }
    }

}
