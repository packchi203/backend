package com.example.forums_backend.entity.my_enum;

import java.util.Objects;

public enum NotificationType {
    UPVOTE(1),DOWN_VOTE(2),COMMENT(3),UPVOTE_COMMENT(4),DOWN_VOTE_COMMENT(5),REPLY_COMMENT(6), UNDEFINED(0);
    private final int value;
    NotificationType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static NotificationType getNotificationType(int value){
        for(NotificationType notificationType : NotificationType.values()){
            if(Objects.equals(notificationType.getValue(), value)){
                return notificationType;
            }
        }
        return UNDEFINED;
    }
}
