/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.events.LoginEvent;
import io.github.beohoang98.chat_ui.events.RegisterEvent;
import io.github.beohoang98.chat_ui.events.SwitchAuthPageEvent;
import io.github.beohoang98.chat_ui.form.Auth;
import io.github.beohoang98.chat_ui.form.ChatHome;
import io.github.beohoang98.chat_ui.form.Register;
import io.github.beohoang98.chat_ui.models.UserModel;
import io.github.beohoang98.chat_ui.store.MessagesStore;
import io.github.beohoang98.chat_ui.store.OnlineUsersStore;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

/**
 *
 * @author noobcoder
 */
public class App {

    public static EventBus eventBus = new EventBus();
    static UserModel user;
    Auth auth;
    Register register;
    ChatHome home;

    public App() throws Exception {
        eventBus.register(this);
        OnlineUsersStore.instance.register();
        MessagesStore.instance.register();
        
        auth = new Auth();
        auth.setVisible(true);
    }

    public static void main(String[] args) {
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new App();
            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).severe(ex.getMessage());
            }
        });
    }
    
    void showHome() {
        auth.dispose();
        home = new ChatHome();
        home.setVisible(true);
    }

    @Subscribe
    void onLogin(LoginEvent loginEvent) {
        user = loginEvent.getUser();
        showHome();
    }
    
    @Subscribe
    void onSwitchAuthForm(SwitchAuthPageEvent event) {
        if (event.isLogin()) {
            auth = new Auth();
            auth.setVisible(true);
            register.dispose();
        } else {
            register = new Register();
            register.setVisible(true);
            auth.dispose();
        }
    }

    @Subscribe
    void onRegister(RegisterEvent registerEvent) {
        user = registerEvent.getUser();
        showHome();
    }
    
    public static UserModel getUser() {
        return user;
    } 
}
