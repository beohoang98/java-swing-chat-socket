/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.beohoang98.chat_ui.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author noobcoder
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthModel implements Serializable {
    String username;
    String password;
}
