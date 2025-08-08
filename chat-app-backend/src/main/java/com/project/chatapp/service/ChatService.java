package com.project.chatapp.service;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Chat;
import com.project.chatapp.model.User;
import com.project.chatapp.request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    Chat createChat(User requestUser, Long userId) throws UserException;

    Chat getChatById(Long id) throws ChatException;

    List<Chat> getAllChatByUserId(Long userId) throws UserException;

    Chat createGroup(GroupChatRequest request, User requestUser) throws UserException;

    Chat addUserToGroup(Long userId, Long chatId, User requestUser) throws UserException, ChatException;

    Chat renameGroup(Long chatId, String groupName, User requestUser) throws UserException, ChatException;

    Chat removeFromGroup(Long chatId, Long userId, User requestUser) throws UserException, ChatException;

    void deleteChat(Long chatId, Long userId) throws UserException, ChatException;


}
