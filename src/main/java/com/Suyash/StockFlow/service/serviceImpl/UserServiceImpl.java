package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.User;
import com.Suyash.StockFlow.payload.mapper.UserMapper;
import com.Suyash.StockFlow.payload.request.UserDto;
import com.Suyash.StockFlow.payload.response.UserResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.UserPageResponse;
import com.Suyash.StockFlow.repository.UserRepository;
import com.Suyash.StockFlow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserDto userDto) {
        userRepository.findByEmailIgnoreCase(userDto.getEmail())
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException(
                            "A User with email " + userDto.getEmail() + " already exists"
                    );
                });

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));
        return mapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPageResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> userPage = userRepository.findByEnabledTrue(pageable);

        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return UserPageResponse.builder()
                .content(responses)
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
    }

    @Override
    public UserResponse updateUser(UserDto userDto, Long userId) {

        User existingUser = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));

        userRepository.findByEmailIgnoreCase(userDto.getEmail())
                .ifPresent(matched -> {
                    if(!matched.getId().equals(userId)){
                        throw new DuplicateResourceFoundException(
                                "A User with email " + userDto.getEmail() + " already exists"
                        );
                    }
                });

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());

        User updated = userRepository.save(existingUser);
        return mapper.toResponse(updated);
    }

    @Override
    public String disableUser(Long userId) {

        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));

        user.setEnabled(false);
        userRepository.save(user);
        return "User with userId: " + userId + " disabled successfully";
    }
}
