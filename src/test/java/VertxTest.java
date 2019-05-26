import controller.RootVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import service.UserServ;

import java.util.Random;

@ExtendWith(VertxExtension.class)
class VertxTest {

    private static Vertx vertx;
    private static WebClient client;

    @BeforeAll
    static void deploy_verticle(VertxTestContext context) {
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);
        RootVerticle.deployAll();
        context.completeNow();
    }

    @AfterAll
    static void tearDown(VertxTestContext context) {
        context.completeNow();
        vertx.close();
    }

    @BeforeEach @AfterEach
    void display(){
        UserServ.INSTANCE.logDisplay();
    }

    @RepeatedTest(3)
    @Disabled
    void testMyApplication(VertxTestContext context) {
        client.get(8080, "localhost", "/")
                .as(BodyCodec.string())
                .send(context.succeeding(response -> context.verify(() -> {
                    Assertions.assertEquals(200, response.statusCode());
                    Assertions.assertTrue(response.body().contains("Test"));
                    context.completeNow();
                })));
    }

    @Test
    void testGetUser(VertxTestContext context) {
        client.get(8080, "localhost", "/user")
                .as(BodyCodec.string())
                .send(context.succeeding(response -> context.verify(() -> {
                    Assertions.assertEquals(200, response.statusCode());
                    Assertions.assertEquals(Json.decodeValue(jsonTrim(response.body()), User.class), UserServ.INSTANCE.getUser());
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
        UserServ.INSTANCE.addUser(user);
        UserServ.INSTANCE.logDisplay();
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
        return new User(id, "User" + id);
    }
}
