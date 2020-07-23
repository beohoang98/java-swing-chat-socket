/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_server.utils;

import io.github.beohoang98.chat_server.entities.MessageEntity;
import io.github.beohoang98.chat_server.entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author noobcoder
 */
public class HBUtils {
    
    public static HBUtils instance = new HBUtils();
    
    SessionFactory sessionFactory;
    
    public HBUtils() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .loadProperties("hibernate.properties")
            .enableAutoClose()
            .build();
        sessionFactory = new MetadataSources(registry)
            .addAnnotatedClass(UserEntity.class)
            .addAnnotatedClass(MessageEntity.class)
            .buildMetadata().buildSessionFactory();
    }
    
    public Session open() {
        return sessionFactory.openSession();
    }
}
