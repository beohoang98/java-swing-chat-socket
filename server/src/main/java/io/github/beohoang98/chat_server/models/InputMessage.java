package io.github.beohoang98.chat_server.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class InputMessage implements Serializable {
    public String content;
    public User toUser;
}
