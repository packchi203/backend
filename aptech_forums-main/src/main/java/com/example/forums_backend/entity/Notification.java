package com.example.forums_backend.entity;

import com.example.forums_backend.entity.my_enum.NotificationStatus;
import com.example.forums_backend.entity.my_enum.NotificationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.example.forums_backend.config.constant.notification.ContentConstant.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String redirect_url;
    private String content;
    @ManyToOne
    private Account interactive_user;
    @ManyToOne
    private Account receiver;
    private NotificationType type;
    private NotificationStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void setNotificationContent(NotificationType notificationType){
        switch (notificationType){
            case UPVOTE:
                this.content = UPVOTE_POST_CONTENT_NOTIFY;
                break;
            case DOWN_VOTE:
                this.content = DOWN_VOTE_POST_CONTENT_NOTIFY;
                break;
            case COMMENT:
                this.content = COMMENT_POST_CONTENT_NOTIFY;
                break;
            case REPLY_COMMENT:
                this.content = REPLY_COMMENT_CONTENT_NOTIFY;
                break;
            case UPVOTE_COMMENT:
                this.content = UPVOTE_COMMENT_CONTENT_NOTIFY;
                break;
            case DOWN_VOTE_COMMENT:
                this.content = DOWN_VOTE_COMMENT_CONTENT_NOTIFY;
                break;
            case UNDEFINED:
                break;
        }
    }
}
