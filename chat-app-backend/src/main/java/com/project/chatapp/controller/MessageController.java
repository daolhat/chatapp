package com.project.chatapp.controller;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.MessageException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Message;
import com.project.chatapp.model.User;
import com.project.chatapp.request.SendMessageRequest;
import com.project.chatapp.response.ApiResponse;
import com.project.chatapp.service.MessageService;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest request,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.getUserProfile(jwt);
        request.setUserId(user.getId());
        Message message = messageService.sendMessage(request);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsHandler(@PathVariable("chatId") Long chatId,
                                                         @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.getUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessage(chatId, user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable("messageId") Long messageId,
                                                            @RequestHeader("Authorization") String jwt) throws MessageException, UserException {
        User user = userService.getUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse response = ApiResponse.builder()
                .message("Message delete successfully")
                .status(true)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
