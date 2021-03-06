package com.dominikach.vertx_task.Service;

import com.dominikach.vertx_task.Model.User;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static com.dominikach.vertx_task.Verticle.MainVerticle.mongoClient;


public class UserService {

  public static void response(RoutingContext routingContext, int httpstatus, String description) {
    routingContext.response()
      .putHeader("content-type", "application/json")
      .setStatusCode(httpstatus)
      .end(description);
  }

  public static void isLoginAvailable(RoutingContext routingContext) {
    if(routingContext.getBody() == null) {
      response(routingContext, 400,"You have not provided any data.");
    } else {
      String login = routingContext.getBodyAsJson().getString("login");
      JsonObject loginMatch = new JsonObject();
      loginMatch.put("login", login);
      mongoClient.findOne("user", loginMatch, null, jsonObjectAsyncResult -> {
        if (jsonObjectAsyncResult.succeeded()) {
          if (jsonObjectAsyncResult.result() != null) {
            response(routingContext, 400, "This login is already taken.");
            //found user with this login
          } else {
            routingContext.next();
            //if result empty
          }
        }
      });
    }
  }

  public static void loginAndPasswordCheck(RoutingContext routingContext) {
    if (routingContext.getBody() == null) {
      response(routingContext, 400, "You have not provided login or password.");
    } else {
      String login = routingContext.getBodyAsJson().getString("login");
      String password = routingContext.getBodyAsJson().getString("password");
      JsonObject loginMatch = new JsonObject().put("login", login);
      mongoClient.findOne("user", loginMatch, null, jsonObjectAsyncResult -> {
        if (jsonObjectAsyncResult.succeeded()) {
          if (jsonObjectAsyncResult.result() == null) {
            //login not found
            response(routingContext, 400, "This user doesn't exist");
          } else {
            //found login -> check password
            String dbPassword = jsonObjectAsyncResult.result().getString("password");
            if (BCrypt.checkpw(password, dbPassword)) {
              //password correct
              routingContext.next();
            } else {
              //password incorrect
              response(routingContext, 400, "Incorrect login or password.");
            }
          }
        } else {
          //not succeeded
          response(routingContext, 400, "");
        }
      });
    }
  }

  public static void addUser(RoutingContext routingContext) {
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
        response(routingContext, 201, "Registering successfull.");
      } else {
        response(routingContext, 400, "Registering failed.");
      }
    });


  }


}
