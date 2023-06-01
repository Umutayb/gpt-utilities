package models;

import lombok.Data;

import java.util.List;

@Data
public class Librarian {
    String userID;
    String username;
    List<BookModel> books;

    @Data
    public static class BookModel {
        String isbn;
        String title;
        String subTitle;
        String author;
        String publish_date;
        String publisher;
        int pages;
        String description;
        String website;
    }
}
