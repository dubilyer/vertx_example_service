package controllers;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import verticles.PersistanceVerticle;
import verticles.RootVerticle;

public class RootController {
    static EventBus eventBus;

    public static void main(String[] args) {
        deployAll();
    }

    public static void deployAll() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RootVerticle());
        vertx.deployVerticle(new PersistanceVerticle());
    }
}
