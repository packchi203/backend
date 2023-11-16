package com.example.forums_backend.controller;

import com.example.forums_backend.dto.CommentReqDto;
import com.example.forums_backend.dto.CommentReqWsDto;
import com.example.forums_backend.dto.CommentResDto;
import com.example.forums_backend.entity.Notification;
import com.example.forums_backend.entity.Post;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

//comment websocket
@Controller
public class WebsocketController {
    @Autowired
    CommentService commentService;
    @Autowired
    VoteService voteService;
    @Autowired
    BookmarkService bookmarkService;
    @Autowired
    PostService postService;
//    @Autowired

    @Autowired
    NotificationService notificationService;

//    @MessageMapping("/comment-res")
//    public CommentResDto recMessage(@Payload CommentReqWsDto commentReqWsDto) throws AppException {
//        Post post = postService.findByID(commentReqWsDto.getPostId());
//        CommentReqDto commentReqDto = new CommentReqDto();
//        commentReqDto.setContent(commentReqWsDto.getContent());
//        commentReqDto.setReply_to(commentReqWsDto.getReply_to());
//        CommentResDto commentResDto = commentService.saveComment(post.getId(), commentReqDto);
//        simpMessagingTemplate.convertAndSendToUser(String.valueOf(post.getId()), "/post-comment", commentResDto);
//        return commentResDto;
//    }
//
//    @MessageMapping("/notification")
//    public Notification pushNotification(@Payload Notification notification){
//        Notification notificationRes = notificationService.saveNotification(notification);
//        simpMessagingTemplate.convertAndSendToUser(notification.getReceiver().getUsername(), "/post-comment", notificationRes);
//        return notificationRes;
//    }
}
