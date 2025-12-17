package com.example.demo.dto;

import java.util.HashMap;
import java.util.Map;

public class DashboardData {

    private Map<String, Object> data;

    public DashboardData() {
        this.data = new HashMap<>();
    }

    public DashboardData(Map<String, Object> data) {
        this.data = data != null ? data : new HashMap<>();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data != null ? data : new HashMap<>();
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getData(String key) {
        return this.data.get(key);
    }
}

