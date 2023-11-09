package service;

import model.User;
import repository.InMemoryUsers;
import repository.RepositoryUsers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceUserImpl implements ServiceUser {

    RepositoryUsers repositoryUsers = new InMemoryUsers();

    public Map<Long, User> getAllUsers(){
        return repositoryUsers.getAllUsers();
    }

    public List<String> getListWords(Long id) {
        List<String> words;
        User user = repositoryUsers.getUserById(id);
        words = user.getListOfWords();
        return words;
    }

    public void load(String path) {
        repositoryUsers.loadFile(path);
    }

    @Override
    public void addUser(User user){
        String email = user.getEmail();
        if (isCheckingEmail(email)) {
            repositoryUsers.save(user);
            repositoryUsers.saveFile();
            System.out.println("Вы зарегистрированы.");
        } else {
            System.out.println("Данный Email уже зарегистрирован");
        }
    }

    @Override
    public Long authorization(String email, String password) {
        Long id = null;
        List<User> users = repositoryUsers.findGamersByEmail(email);
        if (users == null || users.size() == 0) {
            System.out.println("Данный Email не существует");
            return id;
        }
        for (User user : users) {
            if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
                id = user.getId();
            } else {
                System.out.println("Введен неверный пароль");
                return id;
            }
        }
        return id;
    }

    public void phraseInput(Long id, String phrase) {
        User user = repositoryUsers.getUserById(id);
        if (user != null) {
            if (isCheckPhrase(user.getListOfWords(), phrase)) {
                if (isPalindrome(phrase)){
                    user.getListOfWords().add(phrase);
                    int s = user.getScore();
                    user.setScore(s + phrase.length());
                    repositoryUsers.updateUser(user);
                    repositoryUsers.saveFile();
                    System.out.println("Молодец! Это палиндром");
                } else {
                    System.out.println("Введенная фраза не является палиндромом");
                }
            } else {
                System.out.println("Вы уже вводили данную фразу");
            }
        } else {
            System.out.println("Указанный пользователь не зарегистрирован");
        }
    }

    @Override
    public int getScore(Long id) {
        int score = 0;
        User user = repositoryUsers.getUserById(id);
        if (user != null) {
            return user.getScore();
        }
        return score;
    }

    @Override
    public List<User> getLeaderboard() {
        Map<Long, User> users = repositoryUsers.getAllUsers();
        return users.values().stream()
                .filter(user -> user.getScore() > 0)
                .sorted(Comparator.comparingInt(User::getScore).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private boolean isPalindrome(String phrase) {
        // Удаляем все пробелы и приводим к нижнему регистру
        String cleanPhrase = phrase.replaceAll("\\s+", "").toLowerCase();

        // Положение первого символа
        int left = 0;

        // Положение последнего символа
        int right = cleanPhrase.length() - 1;

        // Проверяем символы в строке
        while (left < right) {
            if (cleanPhrase.charAt(left) != cleanPhrase.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }

    private boolean isCheckPhrase (List<String> listOfWords, String phrase) {
        if (listOfWords == null) {
            return false;
        }
        for (String word : listOfWords) {
            if (phrase.equalsIgnoreCase(word)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCheckingEmail(String email) {
        List<User> gamers = repositoryUsers.findGamersByEmail(email);
        return gamers.size() == 0;
    }
}
