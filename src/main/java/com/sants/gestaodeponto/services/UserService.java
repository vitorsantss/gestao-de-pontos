package com.sants.gestaodeponto.services;

import com.sants.gestaodeponto.domain.user.RequestUserDTO;
import com.sants.gestaodeponto.domain.user.ResponseUserDTO;
import com.sants.gestaodeponto.domain.user.User;
import com.sants.gestaodeponto.exceptions.ResourceNotFoundException;
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

    public ResponseUserDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> new ResponseUserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getWork_schedule()
                ))
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }

    public ResponseUserDTO updateUser(String id, RequestUserDTO data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        user.setName(data.name());
        user.setEmail(data.email());
        user.setRole(data.role());
        user.setWork_schedule(data.work_schedule());
        user = userRepository.save(user);

        return new ResponseUserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getWork_schedule()
        );
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
