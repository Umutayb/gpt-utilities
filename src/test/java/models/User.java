package models;

import lombok.Data;

@Data
public class User {
    String username;
    String firstname;
    String lastname;
    String email;
    String password;
    String phone;
    int userStatus;
    long id;
}
