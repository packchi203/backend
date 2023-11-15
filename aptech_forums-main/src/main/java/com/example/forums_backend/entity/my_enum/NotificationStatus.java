package com.example.forums_backend.entity.my_enum;

import java.util.Objects;

public enum NotificationStatus {
    NOT_SEEN(0),SEEN(1),UNDEFINED(-1);
    private final int value;
    NotificationStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static NotificationStatus getNotificationStatus(int value){
        for(NotificationStatus subject : NotificationStatus.values()){
            if(Objects.equals(subject.getValue(), value)){
                return subject;
            }
        }
        return UNDEFINED;
    }
}
