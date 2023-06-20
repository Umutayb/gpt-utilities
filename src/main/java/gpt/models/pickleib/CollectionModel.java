package gpt.models.pickleib;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CollectionModel {



    public CollectionModel(String collection, Filter filter) {
        this.dataSource = "test1";
        this.database = "pickleib";
        this.collection = collection;
        this.filter = filter;
    }

    String dataSource;
    String database;
    String collection;
    Filter filter;

    @Data @NoArgsConstructor
    public static class Filter {
        public Filter(String name) {
            this.methodName = name;
        }

        String methodName;
    }
}
