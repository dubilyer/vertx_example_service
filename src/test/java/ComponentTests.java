import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
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
import verticles.RestVerticle;

import java.util.Random;

@ExtendWith(VertxExtension.class)
class ComponentTests extends BaseTest{
    private static Message[] receivedMessage;

    @BeforeAll
    static void deploy_verticle(VertxTestContext context) {
        vertx = Vertx.vertx();
        client = WebClient.create(vertx);
        vertx.deployVerticle(new RestVerticle());
        EventBus eventBus = vertx.eventBus();
        receivedMessage = new Message[1];
        eventBus.consumer("DB",
                message -> {
                    message.reply("OK");
                    receivedMessage[0] = message;
                });
        context.completeNow();
    }

    @Test
    void testGetUser(VertxTestContext context) {
        int id = new Random().nextInt();
        String expectedMessage = String.format("Get User#%d", id);
        client.get(8080, "localhost", "/user/" + id)
                .as(BodyCodec.string())
                .send(context.succeeding((response -> context.verify(() -> {
                    Assertions.assertEquals(expectedMessage, receivedMessage[0].body());
                    context.completeNow();
                }))));
    }

    @Test
    void testAddUser(VertxTestContext context) {
        User user = createRandomUser();
        String expectedMessage = String.format("Add User#%s", Json.encode(user));
        client.post(8080, "localhost", "/user")
                .sendJson(user, ar -> {
                    Assertions.assertEquals(expectedMessage, receivedMessage[0].body());
                    context.completeNow();
                });
    }


    @Test
    void DeleteUser(VertxTestContext context) {
        User user = createRandomUser();
        String expectedMessage = String.format("Delete User#%d", user.getId());
        client.delete(8080, "localhost", "/user/" + user.getId())
                .as(BodyCodec.string())
                .send(
                        context.succeeding(response -> context.verify(
                                () -> {
                                    Assertions.assertEquals(expectedMessage, receivedMessage[0].body());
                                    context.completeNow();
                                }
                        ))
                );
    }

}
