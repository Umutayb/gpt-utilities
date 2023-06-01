package models;

import lombok.Data;

import java.util.List;

@Data
public class Pet {
    Long id;
    DataModel category;
    String name;
    List<String> photoUrls;
    List<DataModel> tags;
    String status;

    @Data
    public static class DataModel {
        Long id;
        String name;
    }
}

