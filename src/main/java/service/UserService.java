package service;

import dao.UserRedisRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import model.User;

/**
 * Service layer. Created to separate repository from controller.
 */
public class UserService {
    private UserRedisRepository repo;

    public UserService(Vertx vertx) {
        repo = new UserRedisRepository(vertx);
    }

    public void deleteUser(Message message, Handler<AsyncResult<Long>> future) {
        String id = message.body().toString().split("#")[1];
        repo.deleteUser(id, future);
    }

    public void addUser(Message message, Handler<AsyncResult<Long>> future) {
        User user = Json.decodeValue(getBody(message), User.class);
        repo.addUser(user, future);
    }

    private String getBody(Message message) {
        return message.body().toString().split("#")[1];
    }

    public void getUser(Message message, Handler<AsyncResult<String>> future) {
        repo.getUserById(getBody(message), future);
    }
}
