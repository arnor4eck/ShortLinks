package com.arnor4eck.ShortLinks.entity.short_url;

import com.arnor4eck.ShortLinks.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="short_url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @Column(nullable = false, length = 1024)
    private String originalUrl;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column
    private LocalDate expiresAt;

    @Column
    @Builder.Default
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User author;
}
