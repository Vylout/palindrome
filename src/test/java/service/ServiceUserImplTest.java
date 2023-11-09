package service;

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ServiceUserImplTest {

    private ServiceUser serviceUser;

    @BeforeEach
    public void setup() {
        serviceUser = new ServiceUserImpl();
    }

    @Test
    public void testUserCreationAndAuthorization() {
        User user = new User("User", "user@example.com", "password");

        serviceUser.addUser(user);

        Map<Long, User> userList = serviceUser.getAllUsers();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals(user.getName(), userList.get(1L).getName());
        Assertions.assertEquals(user.getPassword(), userList.get(1L).getPassword());
        Assertions.assertEquals(user.getEmail(), userList.get(1L).getEmail());

        long id = serviceUser.authorization("user@example.com", "password");

        Assertions.assertEquals(id, userList.get(1L).getId());
    }

    @Test
    void testPhraseInputSuccess() {
        User user = new User("User", "user@example.com", "password");
        serviceUser.addUser(user);
        String phrase = "HJYJH";
        serviceUser.phraseInput(1L, phrase);

        Map<Long, User> userList = serviceUser.getAllUsers();
        Assertions.assertEquals(1, userList.size());
        List<String> words = serviceUser.getListWords(1L);
        Assertions.assertEquals(1, words.size());
        Assertions.assertEquals(phrase, words.get(0));
    }

    @Test
    void testPhraseInputFailure() {
        User user = new User("User", "user@example.com", "password");
        serviceUser.addUser(user);
        String phrase = "Hello";
        serviceUser.phraseInput(1L, phrase);

        Map<Long, User> userList = serviceUser.getAllUsers();
        Assertions.assertEquals(1, userList.size());
        List<String> words = serviceUser.getListWords(1L);
        Assertions.assertEquals(0, words.size());
    }

    @Test
    void testScoreCalculation() {
        User user = new User("User", "user@example.com", "password");
        serviceUser.addUser(user);
        String phrase = "Qwert ytrewq";
        serviceUser.phraseInput(1L, phrase);

        Map<Long, User> userList = serviceUser.getAllUsers();
        Assertions.assertEquals(1, userList.size());
        List<String> words = serviceUser.getListWords(1L);
        Assertions.assertEquals(1, words.size());
        Assertions.assertEquals(phrase, words.get(0));
        int score = serviceUser.getScore(1L);
        Assertions.assertEquals(12, score);
    }

    @Test
    void testLeaderboard() {
        User user1 = new User("User1", "user1@example.com", "password");
        serviceUser.addUser(user1);
        String phrase1 = "Qwert ytrewq";
        serviceUser.phraseInput(1L, phrase1);
        User user2 = new User("User2", "user2@example.com", "password");
        serviceUser.addUser(user2);
        String phrase2 = "qwerewq";
        serviceUser.phraseInput(2L, phrase2);
        User user3 = new User("User3", "user3@example.com", "password");
        serviceUser.addUser(user3);
        String phrase3 = "qwertyu ytrewq";
        serviceUser.phraseInput(3L, phrase3);
        User user4 = new User("User4", "user4@example.com", "password");
        serviceUser.addUser(user4);
        String phrase4 = "qwertyuiuytrewq";
        serviceUser.phraseInput(4L, phrase4);
        User user5 = new User("User5", "user5@example.com", "password");
        serviceUser.addUser(user5);
        String phrase5 = "qwewq";
        serviceUser.phraseInput(5L, phrase5);
        User user6 = new User("User6", "user6@example.com", "password");
        serviceUser.addUser(user6);
        String phrase6 = "Qwert trewq";
        serviceUser.phraseInput(6L, phrase6);


        Map<Long, User> userList = serviceUser.getAllUsers();
        Assertions.assertEquals(6, userList.size());
        List<User> userLeadersList = serviceUser.getLeaderboard();
        Assertions.assertEquals(5, userLeadersList.size());
        Assertions.assertEquals(user4, userLeadersList.get(0));
        Assertions.assertEquals(user3, userLeadersList.get(1));
        Assertions.assertEquals(user1, userLeadersList.get(2));
        Assertions.assertEquals(user6, userLeadersList.get(3));
        Assertions.assertEquals(user2, userLeadersList.get(4));
    }
}