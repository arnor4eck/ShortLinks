package com.arnor4eck.ShortLinks.service;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.short_url.UrlStatistics;
import com.arnor4eck.ShortLinks.repository.UrlStatisticsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;


@Service
@AllArgsConstructor
public class UrlStatisticsService {

    private final UrlStatisticsRepository urlStatisticsRepository;

    private final ShortUrlCacheService shortUrlCacheService;

    private final Parser userAgentParser;

    public ShortUrl getUrlAndUpdateStatistics(String shortCode, HttpServletRequest request){
        ShortUrl url = shortUrlCacheService.findShortUrl(shortCode);

        addNewStatistics(url, request);

        return url;
    }

    private void addNewStatistics(ShortUrl url, HttpServletRequest request){

        UrlStatistics.UrlStatisticsBuilder builder = UrlStatistics.builder()
                .shortUrl(url)
                .ip(request.getRemoteAddr())
                .country(request.getLocale().toLanguageTag());

        String userAgent = request.getHeader("User-Agent");
        if(userAgent != null){
            Client client = userAgentParser.parse(userAgent);

            builder.browser(client.userAgent.family)
                    .device(client.device.family)
                    .os(client.os.family);
        }

        urlStatisticsRepository.save(builder.build());
    }
}
