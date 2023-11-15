package com.example.forums_backend.entity.my_enum;

public enum VoteType {
    UPVOTE(1),DOWN_VOTE(-1),UNDEFINED(0);
    private int value;
    VoteType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static VoteType getVoteType(int value){
        for(VoteType voteType : VoteType.values()){
            if(voteType.getValue() == value){
                return voteType;
            }
        }
        return UNDEFINED;
    }
}
