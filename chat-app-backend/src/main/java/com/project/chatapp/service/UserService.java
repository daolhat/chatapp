package com.project.chatapp.service;

import com.project.chatapp.request.UpdateUserRequest;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id) throws UserException;

    User getUserProfile(String jwt) throws UserException;

    User updateUser(Long id, UpdateUserRequest req) throws UserException;

    List<User> searchUser(String query);


}
