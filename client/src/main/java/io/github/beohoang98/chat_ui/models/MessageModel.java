package io.github.beohoang98.chat_ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author noobcoder
 */
@AllArgsConstructor
@Getter
@Setter
public class MessageModel {

    String id;
    String content;
    String filePath;
    UserModel owner;
    UserModel toUser;
}
