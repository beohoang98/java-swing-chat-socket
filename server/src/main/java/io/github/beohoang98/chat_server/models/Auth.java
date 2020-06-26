package io.github.beohoang98.chat_server.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Auth implements Serializable {
    public String username;
    public String password;
}
