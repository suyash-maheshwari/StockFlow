package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.UserDto;
import com.Suyash.StockFlow.payload.response.UserResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.UserPageResponse;

public interface UserService {
    UserResponse createUser(UserDto userDto);

    UserResponse getUserById(Long userId);

    UserPageResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserResponse updateUser(UserDto userDto, Long userId);

    String disableUser(Long userId);
}
