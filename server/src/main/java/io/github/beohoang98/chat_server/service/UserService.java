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

/**
 *
 * @author noobcoder
 */
public class UserService {

    public static UserService instance = new UserService();

    public boolean checkUser(String username, String password) {
        try (Session session = HBUtils.instance.open()) {
            UserEntity userEntity = session.find(UserEntity.class, username);
            if (userEntity == null) {
                return false;
            }

            BCrypt.Result res = BCrypt.verifyer().verify(password.getBytes(), userEntity.getPassword().getBytes());
            return res.verified;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User register(String username, String rawPassword) {
        try (Session session = HBUtils.instance.open()) {
            UserEntity entity = new UserEntity();
            entity.setUsername(username);
            String hashedPassword = BCrypt.withDefaults().hashToString(5, rawPassword.toCharArray());
            entity.setPassword(hashedPassword);
            session.save(entity);
            return new User(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
