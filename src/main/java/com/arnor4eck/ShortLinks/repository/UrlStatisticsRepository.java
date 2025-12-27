package com.arnor4eck.ShortLinks.repository;

import com.arnor4eck.ShortLinks.entity.short_url.UrlStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlStatisticsRepository extends CrudRepository<UrlStatistics, Long> {}
