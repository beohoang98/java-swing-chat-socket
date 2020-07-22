/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.store;

import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.OnlineUserEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author noobcoder
 */
@Getter
public class OnlineUsersStore {
    public static final OnlineUsersStore instance = new OnlineUsersStore();
    public List<String> users = new ArrayList<>();
    
    @Subscribe
    void onOnlineEvent(OnlineUserEvent event) {
        this.users.clear();
        this.users.addAll(event.getUsers());
    }
    
    public void register() {
        App.eventBus.register(this);
    }
    public void unregister() {
        App.eventBus.unregister(this);
    }
}
