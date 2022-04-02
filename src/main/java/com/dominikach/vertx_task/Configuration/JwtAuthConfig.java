package com.dominikach.vertx_task.Configuration;

import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuthOptions;

public class JwtAuthConfig {

  public static JWTAuthOptions getJwtOpt() {
    JWTAuthOptions config = new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setBuffer("ozW=.^)RFRo3U6:EbHy.@N0y3>!M;nY[qYVyog&u8H'%+|+fk/?5QrU}Kp'3]$k"));
    return config;

  }


}
