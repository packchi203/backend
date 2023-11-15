package com.example.forums_backend.service;

import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.Notification;
import com.example.forums_backend.entity.my_enum.NotificationStatus;
import com.example.forums_backend.entity.my_enum.NotificationType;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    AccountService accountService;

    public List<Notification> getAllNotification() {
        Account account = accountService.getUserInfoData();
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        return notificationRepository.findNotificationByReceiver_IdOrderByStatus(account.getId(),sort);
    }

    public Notification saveNotification(Notification notification) {
        Account accountSend = accountService.findByUsername(notification.getInteractive_user().getUsername());
        Account accountReceiver = accountService.findByUsername(notification.getReceiver().getUsername());
        Notification notificationSave = new Notification();
        notificationSave.setInteractive_user(accountSend);
        notificationSave.setReceiver(accountReceiver);
        notificationSave.setRedirect_url(notification.getRedirect_url());
        notificationSave.setType(NotificationType.getNotificationType(notification.getType().getValue()));
        notificationSave.setStatus(NotificationStatus.NOT_SEEN);
        notificationSave.setNotificationContent(notification.getType());
        notificationRepository.save(notificationSave);
        return notificationSave;
    }

    public boolean seenNotification(Long id) throws AppException {
        Account account =  accountService.getUserInfoData();
        Optional<Notification> notificationOptional = notificationRepository.findFirstByIdAndReceiver_Id(id,account.getId());
        if(!notificationOptional.isPresent()){
            throw new AppException("COMMENT NOT FOUND!");
        }
        Notification notification = notificationOptional.get();
        notification.setStatus(NotificationStatus.SEEN);
        notificationRepository.save(notification);
        return true;
    }
}
