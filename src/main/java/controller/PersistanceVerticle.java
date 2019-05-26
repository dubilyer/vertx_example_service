package controller;

import dao.DAO;
import dao.UserRedisRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import model.User;

public class PersistanceVerticle extends AbstractVerticle {
    DAO repo;

    @Override
    public void start() throws Exception {
        repo = new UserRedisRepository(vertx);
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer("DB", receivedMessage -> {
            System.out.println("Received: " + receivedMessage.body());
            switch (receivedMessage.body().toString().split("#")[0]) {
                case "Get User":
                    getUser(receivedMessage);
                    break;
                case "Add User":
                    addUser(receivedMessage);
                    break;
                case "Delete User":
                    deleteUser(receivedMessage);
                    break;
                default:
                    receivedMessage.reply("Unknown message: " + receivedMessage.body());
            }
        });
    }

    private void deleteUser(Message message) {
        String id = message.body().toString().split("#")[1];
        repo.deleteUser(id, future -> handle(future, message));
    }

    private void addUser(Message message) {
        User user = Json.decodeValue(message.body().toString().split("#")[1], User.class);
        repo.addUser(user, future -> handle(future, message));
    }

    void getUser(Message message) {
        repo.getUserById(
                future -> handle(future, message));
    }

    <T> void handle(AsyncResult<T> future, Message message) {
        System.out.println(future.succeeded());
        if (future.succeeded()) {
            message.reply(future.result());
        }
    }

}
