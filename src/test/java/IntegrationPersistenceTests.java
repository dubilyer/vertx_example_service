import controllers.RootController;
import verticles.RootVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import redis.clients.jedis.Jedis;

import java.util.Random;

@ExtendWith(VertxExtension.class)
class IntegrationPersistenceTests {

    private static Vertx vertx;
    private static WebClient client;
    private static Jedis jedis;

    @BeforeAll
    static void deploy_verticle(VertxTestContext context) {
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);
        jedis = new Jedis("192.168.99.100", 6379);
        RootController.deployAll();
        context.completeNow();
    }

    @AfterAll
    static void tearDown(VertxTestContext context) {
        context.completeNow();
        vertx.close();
    }

    @Test
    void testGetUser(VertxTestContext context) {
        User user = createRundomUser();
        client.get(8080, "localhost", "/user/" + user.getId())
                .as(BodyCodec.string())
                .send(context.succeeding(response -> context.verify(() -> {
                    Assertions.assertEquals(200, response.statusCode());
                    Assertions.assertEquals(Json.decodeValue(jsonTrim(response.body()), User.class), user);
                    context.completeNow();
                })));
    }

    private String jsonTrim(String body) {
        return body.substring(1,body.length()-1).replace("\\","");
    }

    @Test
    void testAddUser(VertxTestContext context) {
        User user = createRundomUser();
        client.post(8080, "localhost", "/user")
                .sendJson(user, ar -> {
                    Assertions.assertEquals(ar.result().statusCode(), 201);
                    Assertions.assertEquals(Json.decodeValue(ar.result().body(), User.class), user);
                    context.completeNow();
                });
    }


    @Test
    void DeleteUser(VertxTestContext context) {
        User user = createRundomUser();
        client.delete(8080, "localhost", "/user/" + user.getId())
                .as(BodyCodec.string())
                .send(
                        context.succeeding(response -> context.verify(
                                () -> {
                                    Assertions.assertEquals(204, response.statusCode());
                                    context.completeNow();
                                }
                        ))
                );
    }

    private User createRundomUser() {
        int id = new Random().nextInt();
        User user = new User(id, "User" + id);
        jedis.hset("User", String.valueOf(id), Json.encode(user));
        return user;
    }
}
