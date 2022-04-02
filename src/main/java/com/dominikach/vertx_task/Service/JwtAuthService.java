package com.dominikach.vertx_task.Service;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import static com.dominikach.vertx_task.Verticle.MainVerticle.jwtAuthProvider;
import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;

public class JwtAuthService {

  public static void response(RoutingContext routingContext, int httpstatus) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end();
  }

  public static void getToken(RoutingContext routingContext) {
    String login = routingContext.getBodyAsJson().getString("login");
    JsonObject loginMatch = new JsonObject().put("login", login);
    mongoClient.findOne("user", loginMatch, null, jsonObjectAsyncResult -> {
      if(jsonObjectAsyncResult.succeeded()){
        if(jsonObjectAsyncResult == null){
          response(routingContext, 400);
        } else {
          String userId = jsonObjectAsyncResult.result().getString("id");
          JsonObject token = new JsonObject().put("token", jwtAuthProvider.generateToken(new JsonObject().put("UUID", userId)));
          String aaa = token.getString("token");
        }
      }
    });
  }

}
