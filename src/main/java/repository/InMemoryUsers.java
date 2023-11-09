package repository;

import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryUsers implements RepositoryUsers {
    private String path;
    private long id = 0;

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void loadFile(String path) {
        this.path = path;
        loadUser(path + File.separator + "users.csv");
        loadListOfWords(path + File.separator + "listOfWords.csv");
    }

    @Override
    public void saveFile() {
        saveUser();
        saveListOfWords();
    }

    @Override
    public void save(User user) {
        user.setId(generationId());
        users.put(user.getId(), user);
    }

    @Override
    public List<User> findGamersByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Map<Long, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    public String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
    }

    private void saveUser() {
        StringBuilder builderContent = new StringBuilder("ID,Name,Email,Password,Score,\n");
        try {
            for (var user : users.values()) {
                builderContent.append(user.getId()).append(",");
                builderContent.append(user.getName()).append(",");
                builderContent.append(user.getEmail()).append(",");
                builderContent.append(user.getPassword()).append(",");
                builderContent.append(user.getScore());
                builderContent.append("\n");
            }
            String content = builderContent.toString();
            Files.writeString(Path.of(path + File.separator + "users.csv"), content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения");
        }
    }

    private void saveListOfWords() {
        StringBuilder builderContent = new StringBuilder("ID,ListOfWords\n");
        try {
            for (var user : users.values()) {
                if (user.getListOfWords() != null) {
                    builderContent.append(user.getId()).append(",");
                    List<String> words = user.getListOfWords();
                    for (String word : words) {
                        builderContent.append(word).append(",");
                    }
                    builderContent.append("\n");
                }
            }
            String content = builderContent.toString();
            Files.writeString(Path.of(path + File.separator + "listOfWords.csv"), content,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения");
        }
    }

    private void loadUser(String path) {
        String content = readFileContentsOrNull(path);
        if (content == null) {
            System.out.println("Файл пустой");
        } else {
            String[] lines = content.split("\r?\n");
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String[] parts = line.split(",");
                long id = Long.parseLong(parts[0]);
                String name = parts[1];
                String email = parts[2];
                String password = parts[3];
                int score = Integer.parseInt(parts[4]);
                if (i == lines.length - 1) {
                    this.id= id;
                }

                User user = new User(id, name, email, password, score);
                users.put(user.getId(), user);
            }
            System.out.println("Данные обновлен");
        }
    }

    private void loadListOfWords(String path) {
        String content = readFileContentsOrNull(path);
        if (content == null) {
            System.out.println("Файл пустой");
        } else {
            String[] lines = content.split("\r?\n");
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String[] parts = line.split(",");
                long id = Long.parseLong(parts[0]);
                User user = users.get(id);
                List<String> words = new ArrayList<>();
                for (int j = 1; j < parts.length; j++) {
                    words.add(parts[j]);
                }
                user.setListOfWords(words);
            }
            System.out.println("Данные обновлен");
        }
    }

    private long generationId() {
        return ++id;
    }
}
