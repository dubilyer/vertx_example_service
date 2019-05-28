package model;

import java.util.Objects;

public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @SuppressWarnings("unused") //We need it for serialization
    public User() {
    }

    @Override
    public String toString() {
        return " User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
