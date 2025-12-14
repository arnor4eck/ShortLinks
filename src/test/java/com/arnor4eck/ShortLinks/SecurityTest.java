package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.config.SecurityConfig;
import com.arnor4eck.ShortLinks.controller.ApiController;
import com.arnor4eck.ShortLinks.entity.short_url.dto.ShortUrlsDtoFactory;
import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.security.CookieAccessFilter;
import com.arnor4eck.ShortLinks.security.handlers.CookieAccessDeniedHandler;
import com.arnor4eck.ShortLinks.security.handlers.CookieAuthenticationEntryPoint;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoBeans;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class SecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ShortUrlsService shortUrlsService;

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsService",
            value = "arnor4eck@gmail.com")
    public void testDeleteShouldReturn202() throws Exception {
        when(shortUrlsService.deleteByShortCode(eq("shortCode"), any())).thenReturn(true);


        mockMvc.perform(delete("/api/short_links/shortCode"))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithAnonymousUser
    public void testApiShouldReturn401() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CreateShortUrlRequest request = new CreateShortUrlRequest("http://example.com", null, 1L);

        mockMvc.perform(post("/api/short_links/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
