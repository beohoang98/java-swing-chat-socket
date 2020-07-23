/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.ChatChooseUserEvent;
import io.github.beohoang98.chat_ui.events.OnlineUserEvent;
import io.github.beohoang98.chat_ui.store.OnlineUsersStore;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author noobcoder
 */
public class UserList extends JList<String> implements AncestorListener {

    DefaultListModel<String> model = new DefaultListModel<>();

    public UserList() {
        super();
        setCellRenderer(new UserCellRenderer());
        setModel(model);
        addAncestorListener(this);
        addMouseListener(new ClickHandler());
    }

    @Subscribe
    public void updateUsers(OnlineUserEvent event) {
        System.out.println(event);
        model.clear();
        for (String username : event.getUsers()) {
            model.addElement(username);
        }
    }

    public static class UserCellRenderer implements ListCellRenderer<String> {

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String username, int i, boolean bln, boolean bln1) {
            JLabel label = new JLabel(username);
            label.setAlignmentX(CENTER_ALIGNMENT);
            label.setAlignmentY(CENTER_ALIGNMENT);
            return label;
        }
    }

    @Override
    public void ancestorAdded(AncestorEvent ae) {
        App.eventBus.register(this);
        model.clear();
        for (String u : OnlineUsersStore.instance.getUsers()) {
            model.addElement(u);
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent ae) {
        App.eventBus.unregister(this);
    }

    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }
    
    public class ClickHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent me) {
            int index = UserList.this.getSelectedIndex();
            if (index < 0) return;
            
            String username = model.elementAt(index);
            if (username == null) return;
            
            App.eventBus.post(new ChatChooseUserEvent(username));
        }
    }
}
