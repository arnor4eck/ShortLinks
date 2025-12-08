package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.short_url.dto.AdminShortUrlDto;
import com.arnor4eck.ShortLinks.entity.short_url.dto.ShortUrlDto;
import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.short_url.dto.ShortUrlsDtoFactory;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.UserDto;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // отключение фильтров безопасности
class ApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShortUrlsService shortUrlsService;

    @MockitoBean
    ShortUrlsDtoFactory shortUrlsDtoFactory;

    @MockitoBean
    UserRepository userRepository;

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

        CreateShortUrlRequest request = new CreateShortUrlRequest(originalUrl, daysUrlAlive, 1L);
        String json = mapper.writeValueAsString(request);

        when(shortUrlsDtoFactory.createFromEntity(any())) // обычный DTO
                .thenReturn(new ShortUrlDto(originalUrl, "short_link",
                        createdAt, createdAt.plusDays(daysUrlAlive == null ? 0 : daysUrlAlive), true));

        mockMvc.perform(post("/api/short_links/create")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortUrl").value("short_link"))
                .andExpect(jsonPath("$.author").doesNotExist());
    }

    @Test
    public void testGetUrlByShortCodeOK() throws Exception {
        User mockUser = new User(1, "arnor4eck", "arnor4eck@mail.ru",
                "password", Role.ADMIN);
        ShortUrl url = new ShortUrl(1, "shortCode", "http://arnor4eck.com",
                LocalDate.now(), LocalDate.now(), true, mockUser);
        ShortUrlDto dto = new ShortUrlDto("http://arnor4eck.com", "http://example.com/shortCode",
                LocalDate.now(), LocalDate.now(), true);

        when(shortUrlsService.getByShortCode(anyString())).thenReturn(url);
        when(shortUrlsDtoFactory.createFromEntity(any())).thenReturn(dto);

        mockMvc.perform(get("/api/short_links/short_code"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$.originalUrl").value(url.getOriginalUrl()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.author").doesNotExist());
    }

    @Test
    public void testGetUrlByShortCodeAdmin() throws Exception {
        User mockUser = new User(1, "arnor4eck", "arnor4eck@mail.ru",
                "password", Role.ADMIN);
        ShortUrl url = new ShortUrl(1, "shortCode", "http://arnor4eck.com",
                LocalDate.now(), LocalDate.now(), true, mockUser);
        ShortUrlDto dto = new AdminShortUrlDto("http://arnor4eck.com", "http://example.com/shortCode",
                LocalDate.now(), LocalDate.now(),
                true, UserDto.fromEntity(mockUser));

        when(shortUrlsService.getByShortCode(anyString())).thenReturn(url);
        when(shortUrlsDtoFactory.createFromEntity(any())).thenReturn(dto);

        mockMvc.perform(get("/api/short_links/short_code"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(6))
                .andExpect(jsonPath("$.originalUrl").value(url.getOriginalUrl()))
                .andExpect(jsonPath("$.isActive").exists())
                .andExpect(jsonPath("$.author").exists());
    }

    @Test
    public void testGetUrlBySHortCodeNotFound() throws Exception {
        mockMvc.perform(get("/api/short_links/short_code"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
