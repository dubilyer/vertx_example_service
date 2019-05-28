package verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import service.UserService;

public class PersistanceVerticle extends AbstractVerticle {
   private UserService service;

    @Override
    public void start() {
        service = new UserService(vertx);
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer("DB", receivedMessage -> {
            System.out.println("Received: " + receivedMessage.body());
            switch (receivedMessage.body().toString().split("#")[0]) {
                case "Get User":
                    service.getUser(receivedMessage, future -> handle(future, receivedMessage));
                    break;
                case "Add User":
                    service.addUser(receivedMessage, future -> handle(future, receivedMessage));
                    break;
                case "Delete User":
                    service.deleteUser(receivedMessage, future -> handle(future, receivedMessage));
                    break;
                default:
                    receivedMessage.reply("Unknown message: " + receivedMessage.body());
            }
        });
    }

    private <T> void handle(AsyncResult<T> future, Message message){
        System.out.println(future.succeeded());
        if (future.succeeded()) {
            message.reply(future.result());
        }
    }

}
