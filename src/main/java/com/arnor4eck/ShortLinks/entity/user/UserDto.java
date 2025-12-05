package com.arnor4eck.ShortLinks.entity.user;

public record UserDto(long id, String username, String email) {
    public static UserDto fromEntity(User user){
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
