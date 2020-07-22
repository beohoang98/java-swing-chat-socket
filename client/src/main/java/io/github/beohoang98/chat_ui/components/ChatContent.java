/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

/**
 *
 * @author noobcoder
 */
public class ChatContent extends javax.swing.JPanel {
    String username = "The Username";
    
    /**
     * Creates new form ChatContent
     */
    public ChatContent() {
        initComponents();
    }
    public ChatContent(String username) {
        this.username = username;
        initComponents();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        chatInputPanel = new javax.swing.JPanel();
        chatField = new javax.swing.JTextArea();
        sendBtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        chatInputPanel.setLayout(new java.awt.GridBagLayout());

        chatField.setColumns(20);
        chatField.setRows(2);
        chatField.setTabSize(4);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        chatInputPanel.add(chatField, gridBagConstraints);

        sendBtn.setText("Send(Enter)");
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
        // TODO add your handling code here:
    }//GEN-LAST:event_sendBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatField;
    private javax.swing.JPanel chatInputPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
