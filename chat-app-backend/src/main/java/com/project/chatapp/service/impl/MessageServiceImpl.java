package com.project.chatapp.service.impl;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.MessageException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Chat;
import com.project.chatapp.model.Message;
import com.project.chatapp.model.User;
import com.project.chatapp.repository.MessageRepository;
import com.project.chatapp.request.SendMessageRequest;
import com.project.chatapp.service.ChatService;
import com.project.chatapp.service.MessageService;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    UserService userService;
    ChatService chatService;

    @Override
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {

        User user = userService.getUserById(request.getUserId());
        Chat chat = chatService.getChatById(request.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(request.getContent());
        message.setTimeStamp(LocalDateTime.now());


        return message;
    }

    @Override
    public List<Message> getChatsMessage(Long chatId, User requestUser) throws ChatException, UserException {
        Chat chat = chatService.getChatById(chatId);
//        if (chat == null) {
//            throw new ChatException("");
//        }
        if (!chat.getUsers().contains(requestUser)) {
            throw new UserException("You are not related to this chat " + chat.getId());
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());

        return messages;
    }

    @Override
    public Message getMessageById(Long messageId) throws MessageException {
        Optional<Message> opt = messageRepository.findById(messageId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new MessageException("Message not found with id " + messageId);
    }

    @Override
    public void deleteMessage(Long messageId, User requestUser) throws MessageException, UserException {
        Message message = getMessageById(messageId);

        if (message.getUser().getId().equals(requestUser.getId())) {
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You can't delete another user's message" + requestUser.getFullName());

    }
}
