package dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import model.User;

public class UserRedisRepository implements DAO{
    Vertx vertx;
    RedisClient client;
    private static final String REDIS_USER_KEY = "User";

    public UserRedisRepository(Vertx vertx) {
        this.vertx = vertx;
        client = RedisClient.create(vertx,
                new RedisOptions().setHost("192.168.99.100"));
        populate();
    }

    private void populate() {
        client.hset(REDIS_USER_KEY, "25", Json.encode(
                new User(1, "Admin")), res -> {
            if (res.failed()) {
                res.cause().printStackTrace();
            }
        });
    }

    @Override
    public RedisClient getUserById(Handler<AsyncResult<String>> handler) {
       return client.hget(REDIS_USER_KEY, "25", handler);
    }

    @Override
    public RedisClient addUser(User user, Handler<AsyncResult<Long>> handler) {
        String usr = Json.encode(user);
        return client.hset(REDIS_USER_KEY, String.valueOf(user.getId()), usr, handler);
    }

    @Override
    public RedisClient deleteUser(String id, Handler<AsyncResult<Long>> handler) {
        return client.hdel(REDIS_USER_KEY, String.valueOf(id), handler);
    }
}
