package com.dominikach.vertx_task.Api;

import com.dominikach.vertx_task.Service.ItemService;
import io.vertx.ext.web.RoutingContext;

public class ItemApi {

  public static void addItem(RoutingContext routingContext){ ItemService.addItem(routingContext); }
  public static void showItems(RoutingContext routingContext) { ItemService.getItems(routingContext); }

}
