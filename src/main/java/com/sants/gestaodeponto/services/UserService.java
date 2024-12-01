package com.sants.gestaodeponto.services;

import com.sants.gestaodeponto.domain.user.RequestUserDTO;
import com.sants.gestaodeponto.domain.user.ResponseUserDTO;
import com.sants.gestaodeponto.domain.user.User;
import com.sants.gestaodeponto.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<ResponseUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        // Converter cada entidade User para o DTO
        return users.stream()
                .map(user -> new ResponseUserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getWork_schedule()
                ))
                .toList();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(String id, RequestUserDTO data) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(data.name());
            user.setEmail(data.email());
            user.setRole(data.role());
            user.setWork_schedule(data.work_schedule());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
