package io.github.beohoang98.chat_server.models;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Data
@Getter
@Setter
public class Message extends InputMessage {
    public User owner;
    public Timestamp createdAt;

    public Message() {

    }

    public Message(@NotNull InputMessage inputMessage) {
        content = inputMessage.content;
        toUser = inputMessage.toUser;
    }
}
