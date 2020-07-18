/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

import com.google.common.eventbus.Subscribe;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.events.ConversationListEvent;
import io.github.beohoang98.chat_ui.models.MessageModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author noobcoder
 */
public class ConversationList extends JList<MessageModel> implements AncestorListener {

    DefaultListModel<MessageModel> model;

    public ConversationList() {
        super();
        this.setCellRenderer(new MessageListRenderer());
        model = new DefaultListModel<>();
        setModel(model);
    }

    @Subscribe
    public void updateConversationList(ConversationListEvent event) {
        model.clear();
        model.addAll(event.getConversations());
    }

    static class MessageListRenderer implements ListCellRenderer<MessageModel> {

        @Override
        public Component getListCellRendererComponent(JList<? extends MessageModel> list, MessageModel message, int i, boolean bln, boolean bln1) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            String username = App.getUser().getUsername().equals(message.getOwner().getUsername())
                ? message.getToUser().getUsername()
                : message.getOwner().getUsername();
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.BOLD, 18f));

            JLabel messageLabel = new JLabel(message.getContent().substring(0, 50));

            panel.add(usernameLabel, BorderLayout.NORTH);
            panel.add(messageLabel, BorderLayout.CENTER);

            return panel;
        }
    }

    @Override
    public void ancestorAdded(AncestorEvent ae) {
        App.eventBus.register(this);
    }

    @Override
    public void ancestorRemoved(AncestorEvent ae) {
        App.eventBus.unregister(this);
    }

    @Override
    public void ancestorMoved(AncestorEvent ae) {
    }

}
