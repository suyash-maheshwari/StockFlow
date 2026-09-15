package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.User;
import com.Suyash.StockFlow.payload.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }
}
