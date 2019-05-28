import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxTestContext;
import model.User;
import org.junit.jupiter.api.AfterAll;

import java.util.Random;

class BaseTest {
    static Vertx vertx;
    static WebClient client;

    User createRandomUser() {
        int id = new Random().nextInt();
        return new User(id, "User" + id);
    }

    @AfterAll
    static void tearDown(VertxTestContext context) {
        context.completeNow();
        vertx.close();
    }
}
