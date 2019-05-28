package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Rest api interface
 */
public class RestVerticle extends AbstractVerticle {
    private static Handlers handlers;
    private static EventBus eventBus;

    @Override
    public void start(Future<Void> startFuture) {
        eventBus = vertx.eventBus();
        handlers = new Handlers(vertx);
        Router router = registerRoutes();
        startServer(startFuture, router);
    }

    /**
     * Is use to expose api endpoints and set up
     * handler for each one.
     *
     * @return {@link Router}
     */
    private Router registerRoutes() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/user/:id").handler(handlers::getUser);
        router.post("/user").handler(handlers::addUser);
        router.delete("/user/:id").handler(handlers::deleteUser);
        return router;
    }

    /**
     * Starts Rest service
     *
     * @param startFuture - future object for server creation status
     * @param router      - router for api requests
     */
    private void startServer(Future<Void> startFuture, Router router) {
        eventBus.send("CONS", "Starting", reply -> {
            if (reply.succeeded()) {
                System.out.println("Received reply: " + reply.result().body());
            } else {
                System.out.println("No reply");
            }
        });
        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(8080, result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }
}
