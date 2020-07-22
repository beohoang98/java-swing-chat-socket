/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.store;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.MessageEvent;
import io.github.beohoang98.chat_ui.models.MessageModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author noobcoder
 */
public class MessagesStore {
    public static final MessagesStore instance = new MessagesStore();
    public static EventBus bus = new EventBus();
    Map<String, List<MessageModel>> mapMessages = new ConcurrentHashMap<>();
    
    @Subscribe
    void onNewMessage(MessageEvent event) {
        MessageModel msg = event.getMessage();
        String otherUsername = msg.getOwner().getUsername().equals(App.getUser().getUsername())
            ? msg.getToUser().getUsername()
            : msg.getOwner().getUsername();
        mapMessages.getOrDefault(otherUsername, new ArrayList<>()).add(msg);
        bus.post(event);
    }
    
    public void register() {
        App.eventBus.register(this);
    }
    public void unregister() {
        App.eventBus.unregister(this);
    }
}
