/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.models.MessageModel;
import io.github.beohoang98.chat_ui.services.SocketService;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author noobcoder
 */
public class ChatContent extends javax.swing.JPanel {

    String username = "The Username";
    ChatMessageList chatMessageList;

    /**
     * Creates new form ChatContent
     */
    public ChatContent() {
        initComponents();
    }

    public ChatContent(String username) {
        this.username = username;
        initComponents();
        chatMessageList = new ChatMessageList(App.getUser().getUsername(), username);
        chatScrollContainer.setViewportView(chatMessageList);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chatScrollContainer = new javax.swing.JScrollPane();
        chatInputPanel = new javax.swing.JPanel();
        chatField = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();

        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        setLayout(new java.awt.BorderLayout());
        add(chatScrollContainer, java.awt.BorderLayout.CENTER);

        chatInputPanel.setLayout(new java.awt.GridBagLayout());

        chatField.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow"));
        chatField.setColumns(20);
        chatField.setRows(2);
        chatField.setTabSize(4);
        chatField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chatFieldKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        chatInputPanel.add(chatField, gridBagConstraints);

        sendBtn.setText("Send");
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        chatInputPanel.add(sendBtn, gridBagConstraints);

        add(chatInputPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
        sendChat();
    }//GEN-LAST:event_sendBtnActionPerformed

    private void chatFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chatFieldKeyPressed
        int isShiftPressed = evt.getModifiersEx() & KeyEvent.VK_SHIFT;
        if (isShiftPressed > 0 && evt.getKeyCode() == KeyEvent.VK_ENTER) {
            sendChat();
        }
    }//GEN-LAST:event_chatFieldKeyPressed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        try {
            SocketService.instance.send("GET_MESSAGE", username);
        } catch (IOException e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }//GEN-LAST:event_formAncestorAdded

    void sendChat() {
        String text = chatField.getText();
        if (text == null || text.trim().length() == 0) {
            return;
        }
        MessageModel msg = new MessageModel();
        msg.setContent(text);
        msg.setToUsername(username);
        chatField.setText("");

        try {
            SocketService.instance.send("MESSAGE", msg);
        } catch (IOException e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }

    public void addMessage(MessageModel msg) {
        chatMessageList.updateMessage(msg);
    }

    public void reload() {
        chatMessageList.reloadFromStore();
    }

    void showError(String e) {
        JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatField;
    private javax.swing.JPanel chatInputPanel;
    private javax.swing.JScrollPane chatScrollContainer;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
