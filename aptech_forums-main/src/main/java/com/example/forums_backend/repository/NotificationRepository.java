package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Notification;
import com.example.forums_backend.entity.my_enum.NotificationStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findNotificationByReceiver_IdOrderByStatus(Long id, Sort sort);

    Optional<Notification> findFirstByIdAndReceiver_Id(Long id, Long receiverId);
}
