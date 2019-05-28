package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


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

    private Router registerRoutes() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/user/:id").handler(handlers::getUser);
        router.post("/user").handler(handlers::addUser);
        router.delete("/user/:id").handler(handlers::deleteUser);
        return router;
    }

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
