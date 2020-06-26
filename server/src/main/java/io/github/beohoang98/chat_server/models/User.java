package io.github.beohoang98.chat_server.models;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {
    public String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }
}
