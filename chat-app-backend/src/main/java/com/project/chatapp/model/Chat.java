package com.project.chatapp.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String chatName;
    String chatImage;
    boolean isGroup;

    @ManyToMany
    Set<User> admins = new HashSet<>();

    @ManyToOne
    User createdBy;

    @ManyToMany
    Set<User> users = new HashSet<>();

    @OneToMany
    List<Message> messages = new ArrayList<>();

}
