package com.quizapi.Services.User;

import com.quizapi.Domain.DTO.UserDTO.UserResponseDTO;
import com.quizapi.Domain.Entity.User;
import java.util.List;

public interface IUserService {
    User createUser(User user);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}