package com.dominikach.vertx_task;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Config {

  public Future<JsonObject> get(Vertx vertx) {

    ConfigStoreOptions configStoreOptions = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject()
        .put("path", "configuration/configuration.json"));


    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(configStoreOptions);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    return retriever.getConfig();

  }
}
