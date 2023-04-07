package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class UsersResponse {

    private List<User> data;

    public UsersResponse() {
        data = new ArrayList<>();
    }

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

}
