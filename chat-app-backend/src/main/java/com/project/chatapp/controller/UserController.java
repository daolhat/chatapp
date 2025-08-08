package com.project.chatapp.controller;

import com.project.chatapp.request.UpdateUserRequest;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.User;
import com.project.chatapp.response.ApiResponse;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.getUserProfile(token);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query) {
        List<User> users = userService.searchUser(query);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req,
                                                         @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.getUserProfile(token);
        userService.updateUser(user.getId(), req);

        ApiResponse apiResponse = new ApiResponse("User updated successfully", true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<HashSet<User>> searchUsersByNameHandler(@RequestParam("name") String name) {
        List<User> users = userService.searchUser(name);
        HashSet<User> set = new HashSet<>(users);
        return new ResponseEntity<HashSet<User>>(set, HttpStatus.OK);
    }

}
