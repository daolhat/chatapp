package com.project.chatapp.repository;

import com.project.chatapp.model.Chat;
import com.project.chatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c where c.isGroup=false And :user Member of c.users And :requestUser Member of c.users")
    Chat findSingleChatByUserIds(@Param("user") User user, @Param("requestUser") User requestUser);

    @Query("select c from Chat c join c.users u where u.id =: userId")
    List<Chat> findChatByUserId(@Param("userId") Long userId);

}
