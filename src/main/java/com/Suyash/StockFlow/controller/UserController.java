package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.UserDto;
import com.Suyash.StockFlow.payload.response.UserResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.UserPageResponse;
import com.Suyash.StockFlow.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDto userDto){
        UserResponse response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable @Min(1) Long userId){
        UserResponse response = userService.getUserById(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        UserPageResponse response = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable @Min(1) Long userId){
        UserResponse response = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> disableUser(@PathVariable @Min(1) Long userId){
        String message = userService.disableUser(userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
