/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.models;

import io.github.beohoang98.chat_server.entities.MessageEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author noobcoder
 */
@AllArgsConstructor
@Getter
public class GetMessageResponse {
    String username;
    List<MessageEntity> messages;
}
