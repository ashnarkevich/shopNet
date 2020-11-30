package com.gmail.petrikov05.app.service.model.user;

public class LoginUserDTO {

    private String username;
    private String password;
    private UserRoleDTOEnum role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoleDTOEnum getRole() {
        return role;
    }

    public void setRole(UserRoleDTOEnum role) {
        this.role = role;
    }

}
