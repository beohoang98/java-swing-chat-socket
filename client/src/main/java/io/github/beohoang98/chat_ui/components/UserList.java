/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.components;

import io.github.beohoang98.chat_ui.models.UserModel;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author noobcoder
 */
public class UserList extends JList<UserModel> {
    public UserList() {
        super();
        setCellRenderer(new UserCellRenderer());
    }
    public static class UserCellRenderer implements ListCellRenderer<UserModel> {
        @Override
        public Component getListCellRendererComponent(JList<? extends UserModel> list, UserModel user, int i, boolean bln, boolean bln1) {
            JLabel label = new JLabel(user.getUsername());
            return label;
        }
    }
}
