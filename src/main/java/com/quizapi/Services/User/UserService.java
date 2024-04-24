package com.quizapi.Services.User;

import com.quizapi.Domain.DTO.UserDTO.UserResponseDTO;
import com.quizapi.Domain.Entity.User;
import com.quizapi.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (Objects.isNull(user) || Objects.isNull(user.getLogin()) || Objects.isNull(user.getPassword())) {
            throw new IllegalArgumentException("Usuário inválido");
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        Optional<User> existingUser = userRepository.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com esse login: " + user.getLogin());
        }
        return userRepository.save(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getLogin()
                ))
                .toList();
    }

    public void patchUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (user.getLogin() != null) {
            existingUser.setLogin(user.getLogin());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        userRepository.save(existingUser);
    }

    public UserResponseDTO getUserById(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserResponseDTO(user.getId(), user.getLogin());
        } else {
            throw new RuntimeException("User not found for id: " + id);
        }
    }

    public User updateUser(Long id, User user) {
        if (Objects.isNull(id) || id <= 0 || Objects.isNull(user) || Objects.isNull(user.getLogin()) || Objects.isNull(user.getPassword())) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setLogin(user.getLogin());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            existingUser.setPassword(hashedPassword);
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found for id: " + id);
        }
    }

    public void deleteUser(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found for id: " + id);
        }
    }
}