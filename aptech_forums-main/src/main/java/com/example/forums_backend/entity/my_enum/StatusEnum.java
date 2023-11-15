package com.example.forums_backend.entity.my_enum;

import java.util.Objects;

public enum StatusEnum {
    ACTIVE(1),BLOCKED(-1), UNDEFINED(0), DELETED(-2);
    private final int value;
    StatusEnum(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static StatusEnum statusEnum(int value){
        for(StatusEnum statusEnum : StatusEnum.values()){
            if(Objects.equals(statusEnum.getValue(), value)){
                return statusEnum;
            }
        }
        return UNDEFINED;
    }
}
