package com.project.chatapp.service.impl;

import com.project.chatapp.config.TokenProvider;
import com.project.chatapp.request.UpdateUserRequest;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.User;
import com.project.chatapp.repository.UserRepository;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    TokenProvider tokenProvider;

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("User not found with id: " + id);
    }

    @Override
    public User getUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if (email == null) {
            throw new BadCredentialsException("Received invalid token");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User not found with email: " + email);
        }
        return user;
    }

    @Override
    public User updateUser(Long id, UpdateUserRequest req) throws UserException {
        User user = getUserById(id);
        if (req.getFullName() != null) {
            user.setFullName(req.getFullName());
        }
        if (req.getAvatar() != null) {
            user.setAvatar(req.getAvatar());
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {

        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
