package com.project.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Model.UrlMap;
import com.project.repo.UrlRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class UrlService {
	
	@Autowired
     public UrlRepository urlRepository;


	
	public String shortenUrl(String originalUrl) {
	    // Trim quotes if present
	    originalUrl = originalUrl.replaceAll("^\"|\"$", "");  // Removes surrounding quotes
	    
	    UrlMap url = new UrlMap();
	   
	    UrlMap urlcheck= urlRepository.findByOriginalUrl(originalUrl);
	   
	    if(urlcheck!=null && urlcheck.getId()!=null) {
	    	return urlcheck.getShortUrl();
	    }
	    
	    String shortUrl = generateShortUrl(originalUrl);

	    url.setOriginalUrl(originalUrl);  // Save clean URL
	    url.setShortUrl(shortUrl);
	    url.setCreatedAt(LocalDateTime.now());
	    urlRepository.save(url);
	    return shortUrl;
	}



    public Optional<String> getOriginalUrl(String shortUrl) {
        return urlRepository.findByShortUrl(shortUrl).map(UrlMap::getOriginalUrl);
    }

    
    private String generateShortUrl(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash).substring(0, 8);
        } catch (Exception e) {
            throw new RuntimeException("Error generating short URL");
        }
    }
    
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
   

    public void  deleteExpiredUrls() {
         urlRepository.deleteAll();
    }
}

