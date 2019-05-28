package controllers;

import io.vertx.core.Vertx;
import verticles.PersistanceVerticle;
import verticles.RestVerticle;

/**
 * Controller. Deploys all components (verticles)
 */
public class RootController {
    public static void main(String[] args) {
        deployAll();
    }

    public static void deployAll() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestVerticle());
        vertx.deployVerticle(new PersistanceVerticle());
    }
}
