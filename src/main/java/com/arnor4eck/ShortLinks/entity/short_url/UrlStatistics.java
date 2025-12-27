package com.arnor4eck.ShortLinks.entity.short_url;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_statistics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ShortUrl shortUrl;

    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String ip;

    private String browser;
    private String os;
    private String device;

    @CurrentTimestamp
    private LocalDateTime createdAt;
}
