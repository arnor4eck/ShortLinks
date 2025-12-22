package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.short_url.request.CreateShortUrlRequest;
import com.arnor4eck.ShortLinks.service.ShortUrlsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
