import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxTestContext;
import model.User;
import org.junit.jupiter.api.AfterAll;

import java.util.Random;

/**
 * Base test class
 */
class BaseTest {
    static Vertx vertx;
    static WebClient client;

    /**
     * Random user generator
     * @return {@link User}
     */
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
