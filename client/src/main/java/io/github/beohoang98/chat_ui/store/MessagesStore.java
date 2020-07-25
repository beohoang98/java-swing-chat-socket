/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.store;

import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.GetMessageResponseEvent;
import io.github.beohoang98.chat_ui.events.MessageEvent;
import io.github.beohoang98.chat_ui.events.MessageListLoadedEvent;
import io.github.beohoang98.chat_ui.models.MessageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author noobcoder
 */
public class MessagesStore {

    Logger logger = LogManager.getRootLogger();

    public static final MessagesStore instance = new MessagesStore();
    Map<String, SortedMap<Long, MessageModel>> mapMessages = new ConcurrentHashMap<>();

    @Subscribe
    void onNewMessage(MessageEvent event) {
        MessageModel msg = event.getMessage();
        String otherUsername = msg.getOwnerUsername().equals(App.getUser().getUsername())
            ? msg.getToUsername()
            : msg.getOwnerUsername();
        SortedMap map = mapMessages.getOrDefault(otherUsername, new TreeMap<>());
        map.put(msg.getCreatedAt(), msg);
        mapMessages.put(otherUsername, map);
    }

    @Subscribe
    void onListMessage(GetMessageResponseEvent event) {
        String other = event.getUsername();
        List<MessageModel> messages = event.getMessages();
        SortedMap<Long, MessageModel> map = mapMessages.getOrDefault(other, new TreeMap<>());

        logger.debug("Get Message Response: " + messages.size() + " msgs");

        messages.stream()
            .parallel()
            .forEach((msg) -> {
                map.put(msg.getCreatedAt(), msg);
            });
        mapMessages.put(other, map);

        App.eventBus.post(new MessageListLoadedEvent(other));
    }

    public void register() {
        App.eventBus.register(this);
    }

    public void unregister() {
        App.eventBus.unregister(this);
    }

    public List<MessageModel> getLoadedMessage(String username) {
        List result = new ArrayList<>(
            mapMessages.getOrDefault(username, new TreeMap<>())
                .values()
        );
        logger.info("Get message from " + username + ": " + result.size());

        return result;
    }
}
