package com.example.forums_backend.entity.my_enum;

import java.util.Objects;

public enum Subject {
    POST("POST"),COMMENT("COMMENT"),UNDEFINED("UNDEFINED");
    private final String value;
    Subject(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
    public static Subject getVoteType(String value){
        for(Subject subject : Subject.values()){
            if(Objects.equals(subject.getValue(), value)){
                return subject;
            }
        }
        return UNDEFINED;
    }
}
