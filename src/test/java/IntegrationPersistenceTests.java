import controllers.RootController;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;

@ExtendWith(VertxExtension.class)
@Testcontainers
class IntegrationPersistenceTests extends BaseTest{
    private static Jedis jedis;

    @Container
    private static GenericContainer redis = new GenericContainer("redis:latest")
            .withExposedPorts(6379);

    @BeforeAll
    static void deploy_verticle(VertxTestContext context) {
        System.setProperty("redisPort", String.valueOf(redis.getMappedPort(6379)));
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);
        jedis = new Jedis("192.168.99.100", redis.getMappedPort(6379));
        RootController.deployAll();
        context.completeNow();
    }

    @Test
    void testGetUser(VertxTestContext context) {
        User user = createRandomUser();
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
        User user = createRandomUser();
        client.post(8080, "localhost", "/user")
                .sendJson(user, ar -> {
                    Assertions.assertEquals(ar.result().statusCode(), 201);
                    Assertions.assertEquals(Json.decodeValue(ar.result().body(), User.class), user);
                    context.completeNow();
                });
    }


    @Test
    void DeleteUser(VertxTestContext context) {
        User user = createRandomUser();
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

    @Override
    User createRandomUser() {
        User user = super.createRandomUser();
        jedis.hset("User", String.valueOf(user.getId()), Json.encode(user));
        return user;
    }
}
