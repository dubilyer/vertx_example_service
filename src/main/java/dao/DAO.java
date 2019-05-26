package dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.redis.RedisClient;
import model.User;

public interface DAO {


    RedisClient getUserById(Handler<AsyncResult<String>> handler);

    RedisClient addUser(User user, Handler<AsyncResult<Long>> handler);
    RedisClient deleteUser(String id, Handler<AsyncResult<Long>> handler);
}
