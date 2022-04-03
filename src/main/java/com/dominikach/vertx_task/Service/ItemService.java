package com.dominikach.vertx_task.Service;

import com.dominikach.vertx_task.Model.Item;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.dominikach.vertx_task.Verticle.MainVerticle.jwtAuthProvider;
import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;

@Slf4j
public class ItemService {

  public static void response(RoutingContext routingContext, int httpstatus) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end();
  }

  public static void add(RoutingContext routingContext) {
    String token = routingContext.request().headers().get("Authorization").substring(("Bearer ").length());
    JsonObject tokenJson = new JsonObject().put("token", token);
    jwtAuthProvider.authenticate(tokenJson)
      .onSuccess(success -> {
        if (routingContext.getBodyAsJson() == null) {
          response(routingContext, 400);
        } else {
          String name = routingContext.getBodyAsJson().getString("name");
          log.info(String.valueOf(success.principal()));
          String owner = success.principal().getString("UUID");
          Item item = new Item();
          item.setId(UUID.randomUUID());
          item.setOwner(UUID.fromString(owner));
          item.setName(name);
          JsonObject itemJson = new JsonObject(Json.encode(item));
          mongoClient.save("item", itemJson, res -> {
            if(res.succeeded()) {
              response(routingContext, 201);
              log.info("added new item");
            }
          });

        }
      }).onFailure(fail -> {
        response(routingContext, 401);
        log.info("login failed");
    });
  }

}
