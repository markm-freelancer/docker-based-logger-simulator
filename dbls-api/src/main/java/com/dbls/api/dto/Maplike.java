package com.dbls.api.dto;

import java.util.ArrayList;

import lombok.Data;

public class Maplike extends ArrayList<Maplike.Entrylike> {
    private static final long serialVersionUID = 1L;
    @Data
    public class Entrylike {
        private String Name;
        private String Value;
        public Entrylike(String name, String value) {
            this.Name = name;
            this.Value = value;
        }
    }
    public Entrylike get(String key) {
        return this.stream().filter(e -> e.getName().equals(key)).findFirst().orElse(null);
    }
}

