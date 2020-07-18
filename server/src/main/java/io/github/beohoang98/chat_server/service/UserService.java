/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.beohoang98.chat_server.entities.UserEntity;
import io.github.beohoang98.chat_server.models.User;
import io.github.beohoang98.chat_server.utils.HBUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author noobcoder
 */
public class UserService {

    public static UserService instance = new UserService();

    public String checkUser(String username, String password) {
        try (Session session = HBUtils.instance.open()) {
            UserEntity userEntity = session.find(UserEntity.class, username);
            if (userEntity == null) {
                return "User not found";
            }

            BCrypt.Result res = BCrypt.verifyer().verify(password.getBytes(), userEntity.getPassword().getBytes());
            return res.verified ? null : "Wrong password";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public User register(String username, String rawPassword) {
        Session session = HBUtils.instance.open();
        Transaction t = session.beginTransaction();
        try {
            UserEntity entity = new UserEntity();
            entity.setUsername(username);
            String hashedPassword = BCrypt.withDefaults().hashToString(5, rawPassword.toCharArray());
            entity.setPassword(hashedPassword);
            session.save(entity);
            t.commit();
            return new User(username);
        } catch (Exception e) {
            e.printStackTrace();
            t.rollback();
            return null;
        }
    }
}
