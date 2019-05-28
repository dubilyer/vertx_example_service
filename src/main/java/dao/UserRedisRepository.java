package dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import model.User;

import java.util.Optional;

/**
 * Redis realization of DAO
 */
public class UserRedisRepository implements DAO {
    private RedisClient client;
    private static final String REDIS_USER_KEY = "User";

    public UserRedisRepository(Vertx vertx) {
        System.out.println(System.getProperty("testRedisPort"));
        RedisOptions options = new RedisOptions()
                .setHost("192.168.99.100")
                .setPort(Integer.valueOf(
                        Optional.ofNullable(System.getProperty("testRedisPost"))
                                .orElse(Optional.ofNullable(System.getProperty("redisPort"))
                                        .orElse("6379"))));

        client = RedisClient.create(vertx, options);

    }

    @Override
    public void getUserById(String id, Handler<AsyncResult<String>> handler) {
        client.hget(REDIS_USER_KEY, id, handler);
    }

    @Override
    public void addUser(User user, Handler<AsyncResult<Long>> handler) {
        String usr = Json.encode(user);
        client.hset(REDIS_USER_KEY, String.valueOf(user.getId()), usr, handler);
    }

    @Override
    public void deleteUser(String id, Handler<AsyncResult<Long>> handler) {
        client.hdel(REDIS_USER_KEY, String.valueOf(id), handler);
    }
}
