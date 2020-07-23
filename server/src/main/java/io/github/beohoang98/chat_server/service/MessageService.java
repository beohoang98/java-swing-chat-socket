/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.service;

import io.github.beohoang98.chat_server.entities.MessageEntity;
import io.github.beohoang98.chat_server.models.InputMessage;
import io.github.beohoang98.chat_server.models.User;
import io.github.beohoang98.chat_server.utils.HBUtils;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author noobcoder
 */
public class MessageService {

    public static final MessageService instance = new MessageService();
    Logger logger = LogManager.getRootLogger();

    @NotNull
    public MessageEntity create(@NotNull InputMessage inputMessage, @NotNull User user) {
        Session session = HBUtils.instance.open();
        Transaction t = session.beginTransaction();
        try {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setContent(inputMessage.getContent());
            messageEntity.setToUsername(inputMessage.getToUsername());
            messageEntity.setOwnerUsername(user.getUsername());
            session.save(messageEntity);
            t.commit();
            return messageEntity;
        } catch (Exception e) {
            e.printStackTrace();
            t.rollback();
            throw e;
        }
    }

    public List<MessageEntity> list(String fromUsername, String toUsername, int from, int limit) {
        logger.debug("List Messages " + fromUsername + " and " + toUsername);
        try (Session session = HBUtils.instance.open()) {
            return session.createQuery("FROM "
                + MessageEntity.class.getSimpleName()
                + " WHERE ((owner_username = :fromUsername AND to_username = :toUsername)"
                + " OR (owner_username = :toUsername AND to_username = :fromUsername))"
                + " ORDER BY created_at DESC", MessageEntity.class)
                .setParameter("fromUsername", fromUsername)
                .setParameter("toUsername", toUsername)
                .setFirstResult(from)
                .setMaxResults(limit)
                .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<MessageEntity> list(String fromUsername, String toUsername) {
        return this.list(fromUsername, toUsername, 0);
    }

    public List<MessageEntity> list(String fromUsername, String toUsername, int from) {
        return this.list(fromUsername, toUsername, from, 20);
    }

    public List<MessageEntity> listDirectConversation(String fromUsername, String toUsername, int from, int limit) {
        try (Session session = HBUtils.instance.open()) {
            return session.createQuery("FROM "
                + MessageEntity.class.getSimpleName()
                + " WHERE ((owner_username = :fromUsername AND to_username = :toUsername)"
                + " OR (owner_username = :toUsername AND to_username = :fromUsername))"
                + " ORDER BY created_at DESC")
                .setParameter("fromUsername", fromUsername)
                .setParameter("toUsername", toUsername)
                .setFirstResult(from)
                .setMaxResults(limit)
                .getResultList();
        }
    }
}
