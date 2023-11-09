package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private int score;
    private List<String> listOfWords;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.listOfWords = new ArrayList<>();
    }

    public User(long id, String name, String email, String password, int score) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.score = score;
        this.listOfWords = new ArrayList<>();
    }
}
