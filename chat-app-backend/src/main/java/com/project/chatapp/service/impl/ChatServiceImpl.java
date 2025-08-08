package com.project.chatapp.service.impl;

import com.project.chatapp.exception.ChatException;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.Chat;
import com.project.chatapp.model.User;
import com.project.chatapp.repository.ChatRepository;
import com.project.chatapp.request.GroupChatRequest;
import com.project.chatapp.service.ChatService;
import com.project.chatapp.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ChatServiceImpl implements ChatService {

    ChatRepository chatRepository;
    UserService userService;

    @Override
    public Chat createChat(User requestUser, Long userId) throws UserException {
        User user = userService.getUserById(userId);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, requestUser);
        if (isChatExist != null) {
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreatedBy(requestUser);
        chat.getUsers().add(user);
        chat.getUsers().add(requestUser);
        chat.setGroup(false);

        return chat;
    }

    @Override
    public Chat getChatById(Long id) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(id);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("Chat not found with id: " + id);
    }

    @Override
    public List<Chat> getAllChatByUserId(Long userId) throws UserException {
        User user = userService.getUserById(userId);
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());
        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest request, User requestUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChatImage(request.getChatImage());
        group.setChatName(request.getChatName());
        group.setCreatedBy(requestUser);
        group.getAdmins().add(requestUser);
        for (Long userId : request.getUserIds()) {
            User user = userService.getUserById(userId);
            group.getUsers().add(user);
        }

        return group;
    }

    @Override
    public Chat addUserToGroup(Long userId, Long chatId, User requestUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.getUserById(userId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(requestUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            } else {
                throw new UserException("You are not admin");
            }
        }
        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public Chat renameGroup(Long chatId, String groupName, User requestUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getUsers().contains(requestUser)) {
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You are not member of this group");
        }
        throw new ChatException("Chat not found with id: " + chatId);
    }

    @Override
    public Chat removeFromGroup(Long chatId, Long userId, User requestUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.getUserById(userId);

        if (opt.isPresent()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(requestUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);

            } else if (chat.getUsers().contains(requestUser)) {
                if (user.getId().equals(requestUser.getId())) {
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }
            }
            throw new UserException("You can't remove another user");
        }
        throw new ChatException("Chat not found with id: " + chatId);

    }

    @Override
    public void deleteChat(Long chatId, Long userId) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if (opt.isPresent()) {
            Chat chat = opt.get();
            chatRepository.deleteById(chat.getId());
        }

    }
}
