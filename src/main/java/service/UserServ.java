package service;

import dao.UserRepository;
import model.User;

public enum UserServ {
    INSTANCE;

    UserRepository repo = UserRepository.INSTANCE;

    public User getUser(){
        return repo.getUserById(1);
    }

    public void addUser(User user){
        repo.addUser(user);
    }

    public void deleteUser(int id){
        repo.deleteUser(id);
    }

    public void logDisplay(){
        System.out.println(repo.toString());
    }
}
