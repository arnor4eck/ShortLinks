package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.entity.user.request.CreateUserRequest;
import com.arnor4eck.ShortLinks.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    private static Stream<Arguments> userCreateStream(){
        return Stream.of(
                Arguments.of("arnor4eck", "arnor4eck@gmail.com", "ADMIN"),
                Arguments.of("John", "test@inbox.dot", "USER"),
                Arguments.of("username", "user@java.kt", "ADMIN")
        );
    }

    @ParameterizedTest
    @MethodSource("userCreateStream")
    public void testCreateUser(String username,
                               String email,
                               String role) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserRequest request = new CreateUserRequest(username, email, "password", role);
        User mockUser = new User(1, username, email, "encoded_password", Role.valueOf(role));
        String requestString = objectMapper.writeValueAsString(request);

        when(userService.create(any(CreateUserRequest.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$.username").value(mockUser.getUsername()))
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()));
    }
}
