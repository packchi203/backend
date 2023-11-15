package com.example.forums_backend.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * Khi định nghĩa entity mapping vào database cần phải define rõ tên bảng, tên trường, kiểu dữ liệu.
 * Việc này có thể khiến code dài hơn nhưng rõ ràng hơn.
 * Tên bảng phải là tên class chuyển thành số nhiều viết thường.
 * </p>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", columnDefinition = "varchar(100)")
    private String name;
    @Column(name = "price")
    @DecimalMin(value = "0.0")
    private BigDecimal price;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "created_at", columnDefinition = "datetime")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at", columnDefinition = "datetime")
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Column(name = "status", columnDefinition = "int")
    private Integer status;
}
