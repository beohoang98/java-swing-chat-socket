/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.events;

import io.github.beohoang98.chat_ui.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author noobcoder
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginEvent {

    UserModel user;
}
