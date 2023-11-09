package repository;

import model.User;

import java.util.List;
import java.util.Map;

public interface RepositoryUsers {
    void save(User user);

    List<User> findGamersByEmail(String email);

    User getUserById(Long id);

    void updateUser(User user);

    Map<Long, User> getAllUsers();

    void loadFile(String path);

    void saveFile();
}
