package dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import model.User;

/**
 * Data access object interface.
 */
public interface DAO {

    void getUserById(String id, Handler<AsyncResult<String>> handler);
    void addUser(User user, Handler<AsyncResult<Long>> handler);
    void deleteUser(String id, Handler<AsyncResult<Long>> handler);
}
