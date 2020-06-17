package com.dbls.api.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class Maplike extends ArrayList<Maplike.Entrylike> {
    private static final long serialVersionUID = 1L;
    @Data
    public class Entrylike {
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Value")
        private String value;
        public Entrylike(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
    public Entrylike get(String key) {
        return this.stream().filter(e -> e.getName().equals(key)).findFirst().orElse(null);
    }
}

