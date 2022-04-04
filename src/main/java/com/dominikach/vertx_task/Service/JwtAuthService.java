package com.dominikach.vertx_task.Service;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import static com.dominikach.vertx_task.Verticle.MainVerticle.jwtAuthProvider;
import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;

@Slf4j
public class JwtAuthService {

  public static void response(RoutingContext routingContext, int httpstatus) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end();
  }

  public static void createAuthToken(RoutingContext routingContext) {
    String login = routingContext.getBodyAsJson().getString("login");
    JsonObject loginMatch = new JsonObject().put("login", login);
    mongoClient.findOne("user", loginMatch, null, jsonObjectAsyncResult -> {
      if(jsonObjectAsyncResult.succeeded()){
        if(jsonObjectAsyncResult.result() == null){
          response(routingContext, 400);
        } else {
          String userId = jsonObjectAsyncResult.result().getString("id");
          JsonObject token = new JsonObject().put("token", jwtAuthProvider.generateToken(new JsonObject().put("UUID", userId)));
          String tokenString = token.getString("token");
          routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(200)
            .end(tokenString);
        }
      }
    });
  }

}
