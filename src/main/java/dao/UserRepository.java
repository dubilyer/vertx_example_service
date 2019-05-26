package dao;

import model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public enum UserRepository {
    INSTANCE;

    List<User> users = new LinkedList<>();

    UserRepository() {
        users.add(new User(1, "Eduard"));
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        users.forEach(sb::append);
        return sb.toString();

    }

    public User getUserById(int id) {
        return users
                .parallelStream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void deleteUser(int id) {
        users.remove(getUserById(id));
    }
}
