package com.project.chatapp.controller;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Chat;
import com.project.chatapp.model.User;
import com.project.chatapp.request.GroupChatRequest;
import com.project.chatapp.request.SingleChatRequest;
import com.project.chatapp.response.ApiResponse;
import com.project.chatapp.service.ChatService;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    ChatService chatService;
    UserService userService;

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest,
                                                  @RequestHeader("Authorization") String jwt ) throws UserException {
        User requestUser = userService.getUserProfile(jwt);
        Chat chat = chatService.createChat(requestUser, singleChatRequest.getUserId());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest request,
                                                   @RequestHeader("Authorization") String jwt ) throws UserException {
        User requestUser = userService.getUserProfile(jwt);
        Chat chat = chatService.createGroup(request, requestUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable("chatId") Long chatId,
                                                    @RequestHeader("Authorization") String jwt ) throws UserException, ChatException {
        Chat chat = chatService.getChatById(chatId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByIdHandler(@RequestHeader("Authorization") String jwt ) throws UserException, ChatException {
        User requestUser = userService.getUserProfile(jwt);
        List<Chat> chats = chatService.getAllChatByUserId(requestUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable("chatId") Long chatId,
                                                      @PathVariable("userId") Long userId,
                                                      @RequestHeader("Authorization") String jwt ) throws UserException, ChatException {
        User requestUser = userService.getUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(chatId, userId, requestUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable("chatId") Long chatId,
                                                           @PathVariable("userId") Long userId,
                                                           @RequestHeader("Authorization") String jwt ) throws UserException, ChatException {
        User requestUser = userService.getUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(chatId, userId, requestUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable("chatId") Long chatId,
                                                         @RequestHeader("Authorization") String jwt ) throws UserException, ChatException {
        User requestUser = userService.getUserProfile(jwt);
        chatService.deleteChat(chatId, requestUser.getId());
        ApiResponse response = ApiResponse.builder()
                .message("Chat is deleted successfully")
                .status(true)
                .build();
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }



}
