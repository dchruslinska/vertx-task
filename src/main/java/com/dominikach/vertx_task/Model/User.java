package com.dominikach.vertx_task.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class User {
  private UUID id;
  private String login;
  private String password;

}
