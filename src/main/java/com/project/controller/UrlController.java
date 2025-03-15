package com.project.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.project.Service.UrlService;

@RestController
public class UrlController {
	
	// String base  = "http://localhost:8066/negativeUrlShortner/";

    String base  = "https://urlshort-teuc.onrender.com/";
	
	@Autowired
    private  UrlService urlService;

    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody String url) {
        String shortUrl = urlService.shortenUrl(url);
        return base + shortUrl;
    }


    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<String> originalUrl = urlService.getOriginalUrl(shortUrl);
        return (ResponseEntity<Object>) originalUrl.map(url -> {
            try {
                String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString()); // Decode before redirecting
                URI encodedUri = URI.create(decodedUrl);
                return ResponseEntity.status(302).location(encodedUri).build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid URL format: " + url);
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
