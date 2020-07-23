/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author noobcoder
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at")
    Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Timestamp updatedAt;
}
