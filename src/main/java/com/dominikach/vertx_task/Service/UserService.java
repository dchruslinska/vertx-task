package com.dominikach.vertx_task.Service;

import com.dominikach.vertx_task.Model.User;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;


public class UserService {

  public static void response(RoutingContext routingContext, int httpstatus) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end();
  }

  public static void isLoginAvailable(RoutingContext routingContext) {
    String login = routingContext.getBodyAsJson().getString("login");
    JsonObject match = new JsonObject();
    match.put("login", login);

    mongoClient.findOne("user", match, null, res -> {
      if (res.succeeded()) {
        if (res.result() != null) {
          response(routingContext, 400);
          //found user with this login
        } else {
          routingContext.next(); //if empty
        }
      }
    });
  }

  public static void loginAndPasswordCheck(RoutingContext routingContext) {
    String login = routingContext.getBodyAsJson().getString("login");
    String password = routingContext.getBodyAsJson().getString("password");
    JsonObject match = new JsonObject().put("login", login);

    mongoClient.findOne("user", match, null, res -> {
      if (res.succeeded()) {
        if (res.result() == null) { //login not found
          response(routingContext, 400);
        } else { //found login -> check password
          String db_password = res.result().getString("password");
            if (BCrypt.checkpw(password, db_password)) { //password correct
              routingContext.next();
            } else { //password incorrect
              response(routingContext, 400);
              }
        }
      } else { //not succeeded
        response(routingContext, 400);
      }
    });
  }

  public static void save(RoutingContext routingContext) {
    String login = routingContext.getBodyAsJson().getString("login");
    String password = routingContext.getBodyAsJson().getString("password");
    User user = new User();
    String enPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    user.setId(UUID.randomUUID());
    user.setLogin(login);
    user.setPassword(enPassword);
    JsonObject document = new JsonObject(Json.encode(user));

    mongoClient.save("user", document, res -> {
      if (res.succeeded()) {
        response(routingContext, 201);
      } else {
        response(routingContext, 400);
      }
    });


  }


}
