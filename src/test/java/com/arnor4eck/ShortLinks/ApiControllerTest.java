package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.controller.api.ApiController;
import com.arnor4eck.ShortLinks.entity.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.ShortUrl;
import com.arnor4eck.ShortLinks.entity.ShortUrlDto;
import com.arnor4eck.ShortLinks.entity.response.ShortUrlsConfig;
import com.arnor4eck.ShortLinks.entity.response.ShortUrlsDtoFactory;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ApiController.class})
class ApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShortUrlsService shortUrlsService;

    @MockitoBean
    ShortUrlsDtoFactory shortUrlsDtoFactory;

    ObjectMapper mapper;

    @BeforeEach
    public void before(){
        mapper = new ObjectMapper();
    }

    static Stream<Arguments> urlsAndDays(){
        return Stream.of(
                Arguments.of("http://arnor4eck.com", null),
                Arguments.of("http://example.com", 5));
    }

    @ParameterizedTest
    @MethodSource("urlsAndDays")
    public void testCreateShortUrlEndPoint(String originalUrl, Integer daysUrlAlive) throws Exception {
        LocalDate createdAt = LocalDate.of(2000, 12, 12);

        CreateShortUrlRequest request = new CreateShortUrlRequest(originalUrl, daysUrlAlive);
        String json = mapper.writeValueAsString(request);

        when(shortUrlsDtoFactory.createFromEntity(any()))
                .thenReturn(new ShortUrlDto("short_link", originalUrl,
                        createdAt, createdAt.plusDays(daysUrlAlive == null ? 0 : daysUrlAlive)));

        mockMvc.perform(post("/api/short_links/create")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value("short_link"));
    }

    @Test
    public void testGetUrlBySHortCodeOK() throws Exception {
        ShortUrl url = new ShortUrl(1, "shortCode", "http://arnor4eck.com", LocalDate.now(), LocalDate.now());
        ShortUrlDto dto = new ShortUrlDto("http://example.com/shortCode", "http://arnor4eck.com", LocalDate.now(), LocalDate.now());

        when(shortUrlsService.getByShortCode(anyString())).thenReturn(url);
        when(shortUrlsDtoFactory.createFromEntity(any())).thenReturn(dto);

        mockMvc.perform(get("/api/short_links/short_code"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(4))
                .andExpect(jsonPath("$.originalUrl").value(url.getOriginalUrl()));
    }

    @Test
    public void testGetUrlBySHortCodeNotFound() throws Exception {
        mockMvc.perform(get("/api/short_links/short_code"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
