package com.project.chatapp.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String content;
    LocalDateTime timeStamp;

    @ManyToOne
    User user;

    @ManyToOne
//    @JoinColumn(name = "chat_id")
    Chat chat;

}
