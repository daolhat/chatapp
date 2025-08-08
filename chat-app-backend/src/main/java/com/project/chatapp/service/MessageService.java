package com.project.chatapp.service;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.MessageException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Message;
import com.project.chatapp.model.User;
import com.project.chatapp.request.SendMessageRequest;

import java.util.List;

public interface MessageService {

    Message sendMessage(SendMessageRequest request) throws UserException, ChatException;

    List<Message> getChatsMessage(Long chatId, User requestUser) throws ChatException, UserException;

    Message getMessageById(Long messageId) throws MessageException;

    void deleteMessage(Long messageId, User requestUser) throws MessageException, UserException;

}
