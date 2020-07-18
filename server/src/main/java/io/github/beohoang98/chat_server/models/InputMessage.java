package io.github.beohoang98.chat_server.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InputMessage implements Serializable {
    public String content;
    public User toUser;
}
