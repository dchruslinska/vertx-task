package com.dominikach.vertx_task.Service;

import com.dominikach.vertx_task.Model.Item;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.dominikach.vertx_task.Verticle.MainVerticle.jwtAuthProvider;
import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;

@Slf4j
public class ItemService {

  public static void response(RoutingContext routingContext, int httpstatus, String description) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end(description);
  }

  public static void addItem(RoutingContext routingContext){
    String token = routingContext.request().headers().get("Authorization").substring(("Bearer ").length());
    JsonObject tokenJson = new JsonObject().put("token", token);
    jwtAuthProvider.authenticate(tokenJson)
      .onSuccess(success -> {
        if (routingContext.getBodyAsJson() == null || routingContext.getBodyAsJson().getString("name").isEmpty() ) {
          response(routingContext, 400, "You have not provided item name.");
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
              response(routingContext, 201, "Item created successfully");
            }
          });
        }
      }).onFailure(fail -> response(routingContext, 401, "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic."));
  }

  public static void getItems(RoutingContext routingContext){
    String token = routingContext.request().headers().get("Authorization").substring(("Bearer ").length());
    JsonObject tokenJson = new JsonObject().put("token", token);
    jwtAuthProvider.authenticate(tokenJson)
      .onSuccess(success -> {
        String owner = success.principal().getString("UUID");
        JsonObject ownerJson = new JsonObject().put("owner", owner);
        mongoClient.find("item", ownerJson, res -> {
          if(res.succeeded()) {
            List<JsonObject> itemsList = res.result();
            JsonObject itemsListJson = new JsonObject().put("items", itemsList);
            response(routingContext, 200, itemsListJson.encode());
          }
        });
      }).onFailure(fail -> response(routingContext, 401, "You have not provided an authentication token, the one provided has expired, was revoked or is not authentic."));
  }


}
