package com.example.trail.model.login;

import com.example.trail.model.user.UserDTO;

import java.io.Serializable;

public class LoginDTO implements Serializable {
    public String email;
    public String password;
    public boolean isLogin = false;
    public String message;
    public UserDTO user;

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
