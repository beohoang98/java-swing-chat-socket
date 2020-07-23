/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

import io.github.beohoang98.chat_ui.models.MessageModel;
import io.github.beohoang98.chat_ui.store.MessagesStore;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
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
public class ChatMessageList extends JList<MessageModel> implements AncestorListener {
    
    String ownerUsername;
    String toUsername;
    DefaultListModel<MessageModel> model;
    
    public ChatMessageList(String ownerUsername, String toUsername) {
        super();
        this.ownerUsername = ownerUsername;
        this.toUsername = toUsername;
        setCellRenderer(new ChatListCellRenderer());
        
        model = new DefaultListModel<>();
        setModel(model);
    }
    
    public void updateMessage(List<MessageModel> list) {
        model.clear();
        list.forEach(msg -> {
            model.addElement(msg);
        });
    }
    
    public void updateMessage(MessageModel msg) {
        model.addElement(msg);
    }
    
    public class ChatListCellRenderer implements ListCellRenderer<MessageModel> {
        
        @Override
        public Component getListCellRendererComponent(JList<? extends MessageModel> list, MessageModel msg, int i, boolean bln, boolean bln1) {
            boolean isOther = !msg.getOwnerUsername().equals(ownerUsername);
            
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new FlowLayout(isOther ? FlowLayout.LEFT : FlowLayout.RIGHT));
            JLabel nameLabel = new JLabel(msg.getOwnerUsername()+ ":");
            nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
            
            if (isOther) {
                messagePanel.add(nameLabel);
            }
            
            messagePanel.add(new JLabel(msg.getCreatedAt().toString()));
            messagePanel.add(new JLabel(msg.getContent()));
            
            return messagePanel;
        }
        
    }
    
    public void reloadFromStore() {
        updateMessage(MessagesStore.instance.getLoadedMessage(toUsername));
    }
    
    @Override
    public void ancestorAdded(AncestorEvent ae) {
        reloadFromStore();
    }
    
    @Override
    public void ancestorRemoved(AncestorEvent ae) {
        
    }
    
    @Override
    public void ancestorMoved(AncestorEvent ae) {
        
    }
    
}
