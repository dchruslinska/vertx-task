package com.dominikach.vertx_task.Configuration;

import io.vertx.core.json.JsonObject;

public class MongoConfig {

  public static JsonObject getDbConfig() {
    JsonObject dbConfig = new JsonObject();
    dbConfig.put("host", "127.0.0.1");
    dbConfig.put("port", 27017);
    dbConfig.put("db_name", "mydb");
    return dbConfig;
  }

}
