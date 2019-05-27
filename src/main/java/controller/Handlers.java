package controller;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

class Handlers {
    Vertx vertx;

    Handlers(Vertx vertx) {
        this.vertx = vertx;
    }

    void getUser(RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        vertx.eventBus().send("DB", "Get User#" + id, reply -> {
            if (reply.succeeded()) {
                System.out.println("Received reply: " + reply.result().body());
                routingContext.response().end(Json.encode(reply.result().body()));
            } else {
                System.out.println("No reply");
            }
        });
    }

    void addUser(RoutingContext routingContext) {
        vertx.eventBus().send("DB", "Add User#" + routingContext.getBodyAsString(), reply -> {
            if (reply.succeeded()) {
                System.out.println("Received reply: " + reply.result().body());
                routingContext.response()
                        .setStatusCode(201)
                        .end(routingContext.getBodyAsString());
            } else {
                System.out.println("No reply");
            }
        });

    }

    void deleteUser(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            vertx.eventBus().send("DB", "Delete User#" + id, reply -> {
                if(reply.succeeded()){
                    routingContext.response().setStatusCode(204).end();
                }
            });
        }
    }
}
