/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author noobcoder
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "messages")
public class MessageEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", unique = false)
    String id;

    String content;

    @Column(columnDefinition = "file_path", nullable = true)
    String filePath;

    @Column(name = "owner_username", nullable = false)
    String ownerUsername;
    @Column(name = "to_username", nullable = true)
    String toUsername;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_username", referencedColumnName = "username", nullable = false, updatable = false, insertable = false)
    UserEntity owner;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_username", referencedColumnName = "username", nullable = true, insertable = false, updatable = false)
    UserEntity toUser;
}
