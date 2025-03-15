package com.project.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.Model.UrlMap;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMap, Long> {
    Optional<UrlMap> findByShortUrl(String shortUrl);
}
