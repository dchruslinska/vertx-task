package com.dominikach.vertx_task.Api;

import com.dominikach.vertx_task.Service.UserService;
import io.vertx.ext.web.RoutingContext;

public class UserApi {

  public static void register(RoutingContext routingContext) { UserService.addUser(routingContext); }
  public static void isLoginAvailable(RoutingContext routingContext) { UserService.isLoginAvailable(routingContext); }
  public static void verifyUser(RoutingContext routingContext) { UserService.loginAndPasswordCheck(routingContext); }

}
