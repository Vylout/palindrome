package service;

import model.User;

import java.util.List;
import java.util.Map;

public interface ServiceUser {

    void addUser(User user);

    Long authorization(String email, String password);

    void phraseInput(Long id, String phrase);

    int getScore(Long id);

    List<User> getLeaderboard();

    void load(String path);

    List<String> getListWords(Long id);

    Map<Long, User> getAllUsers();
}
