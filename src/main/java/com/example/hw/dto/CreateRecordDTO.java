package com.example.hw.dto;

public class CreateRecordDTO {
    String name;
    String login;
    String password;
    String url;

    public CreateRecordDTO(String name, String login, String password, String url) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
