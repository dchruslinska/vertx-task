package com.dominikach.vertx_task;

import com.dominikach.vertx_task.Service.UserService;
import io.vertx.ext.web.RoutingContext;

public class UserApi {

  public static void register(RoutingContext routingContext) {
    UserService.add(routingContext);
  }

  public static void isLoginAvailable(RoutingContext routingContext) {
    UserService.isLoginAvailable(routingContext);
  }

  public static void loginAndPasswordCheck(RoutingContext routingContext) {
    UserService.loginAndPasswordCheck(routingContext);
  }
}
