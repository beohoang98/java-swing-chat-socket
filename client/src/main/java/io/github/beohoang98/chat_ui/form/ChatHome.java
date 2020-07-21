/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.form;

import com.formdev.flatlaf.FlatDarculaLaf;
import io.github.beohoang98.chat_ui.App;
import io.github.beohoang98.chat_ui.components.ConversationList;
import io.github.beohoang98.chat_ui.components.UserList;
import java.awt.BorderLayout;
import javax.swing.UIManager;

/**
 *
 * @author noobcoder
 */
public class ChatHome extends javax.swing.JFrame {

    /**
     * Creates new form ChatHome
     */
    public ChatHome() {
        initComponents();
        usernameField.setText(App.getUser().getUsername());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebar = new javax.swing.JPanel();
        accountSection = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        sidebarTabsWrapper = new javax.swing.JPanel();
        sidebarTabs = new javax.swing.JTabbedPane();
        conversationPanel = new javax.swing.JPanel();
        onlinePanel = new javax.swing.JTabbedPane();
        main = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1280, 720));

        sidebar.setBackground(javax.swing.UIManager.getDefaults().getColor("nb.output.selectionBackground"));
        sidebar.setPreferredSize(new java.awt.Dimension(300, 368));
        sidebar.setLayout(new java.awt.BorderLayout());

        accountSection.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        accountSection.setAlignmentX(0.0F);
        accountSection.setAlignmentY(0.0F);
        accountSection.setMinimumSize(new java.awt.Dimension(0, 100));
        accountSection.setLayout(new javax.swing.BoxLayout(accountSection, javax.swing.BoxLayout.X_AXIS));

        usernameLabel.setText("Your username:");
        accountSection.add(usernameLabel);

        usernameField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        usernameField.setEditable(false);
        accountSection.add(usernameField);

        sidebar.add(accountSection, java.awt.BorderLayout.NORTH);

        sidebarTabsWrapper.setLayout(new java.awt.BorderLayout());

        sidebarTabs.setToolTipText("");

        conversationPanel.setLayout(new BorderLayout());
        conversationPanel.add(new ConversationList(), BorderLayout.CENTER);

        javax.swing.GroupLayout conversationPanelLayout = new javax.swing.GroupLayout(conversationPanel);
        conversationPanel.setLayout(conversationPanelLayout);
        conversationPanelLayout.setHorizontalGroup(
            conversationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 288, Short.MAX_VALUE)
        );
        conversationPanelLayout.setVerticalGroup(
            conversationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        sidebarTabs.addTab("Conversations", conversationPanel);

        onlinePanel.setLayout(new BorderLayout());
        onlinePanel.add(new UserList(), BorderLayout.CENTER);

        sidebarTabs.addTab("Online", onlinePanel);

        sidebarTabsWrapper.add(sidebarTabs, java.awt.BorderLayout.CENTER);

        sidebar.add(sidebarTabsWrapper, java.awt.BorderLayout.CENTER);

        getContentPane().add(sidebar, java.awt.BorderLayout.WEST);

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
        );

        getContentPane().add(main, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ChatHome().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel accountSection;
    private javax.swing.JPanel conversationPanel;
    private javax.swing.JPanel main;
    private javax.swing.JTabbedPane onlinePanel;
    private javax.swing.JPanel sidebar;
    private javax.swing.JTabbedPane sidebarTabs;
    private javax.swing.JPanel sidebarTabsWrapper;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
