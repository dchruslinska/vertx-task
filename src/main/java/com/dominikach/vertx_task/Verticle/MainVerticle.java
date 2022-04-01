package com.dominikach.vertx_task.Verticle;

import com.dominikach.vertx_task.Configuration.MongoConfig;
import com.dominikach.vertx_task.UserApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  public static MongoClient mongoClient;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    JsonObject db_config = MongoConfig.getDbConfig();
    mongoClient = MongoClient.create(vertx, db_config); //db
    startServerandRouter(startPromise);
  }

  private Router getRouter() {
    Router router = Router.router(vertx);
    router
      .route()
      .handler(BodyHandler.create());
    router.get("/test").handler(ctx -> {
      ctx.request().response().end("endpointeeee");
    });
    router.post("/register")
      .handler(UserApi::isLoginAvailable)
      .handler(UserApi::register);
    router.post("/login")
      .handler(UserApi::loginAndPasswordCheck);


    return router;
  }

  private void startServerandRouter(Promise<Void> startPromise){
    Router router = getRouter();
    vertx.createHttpServer()            //server start
      .requestHandler(router)
      .listen(8080);
  }
}
