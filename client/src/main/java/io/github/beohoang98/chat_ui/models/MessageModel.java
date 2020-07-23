package io.github.beohoang98.chat_ui.models;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Getter
public class MessageModel {

    String id;

    @Setter
    String content;
    String filePath;
    String ownerUsername;

    @Setter
    String toUsername;
    
    Timestamp createdAt;
    Timestamp updatedAt;
}
