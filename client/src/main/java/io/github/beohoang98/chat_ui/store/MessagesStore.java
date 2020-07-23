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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author noobcoder
 */
public class MessagesStore {
    
    public static final MessagesStore instance = new MessagesStore();
    Map<String, Map<String, MessageModel>> mapMessages = new ConcurrentHashMap<>();
    
    @Subscribe
    void onNewMessage(MessageEvent event) {
        MessageModel msg = event.getMessage();
        String otherUsername = msg.getOwnerUsername().equals(App.getUser().getUsername())
            ? msg.getToUsername()
            : msg.getOwnerUsername();
        mapMessages.getOrDefault(otherUsername, new HashMap<>())
            .put(msg.getId(), msg);
    }
    
    @Subscribe
    void onListMessage(GetMessageResponseEvent event) {
        String other = event.getUsername();
        List<MessageModel> messages = event.getMessages();
        Map<String, MessageModel> map = mapMessages.getOrDefault(other, new HashMap<>());
        
        messages.forEach(msg -> {
            map.put(msg.getId(), msg);
        });
        
        App.eventBus.post(new MessageListLoadedEvent(other));
    }
    
    public void register() {
        App.eventBus.register(this);
    }
    
    public void unregister() {
        App.eventBus.unregister(this);
    }
    
    public List<MessageModel> getLoadedMessage(String username) {
        return new ArrayList<>(
            mapMessages.getOrDefault(username, new HashMap<>())
                .values()
        );
    }
}
