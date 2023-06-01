package models;

import lombok.Data;

import java.util.List;

@Data
public class CollectionOfIsbnModel {
    String userId;
    List<Isbn> collectionOfIsbns;

    @Data
    public static class Isbn {
        String isbn;
    }

}