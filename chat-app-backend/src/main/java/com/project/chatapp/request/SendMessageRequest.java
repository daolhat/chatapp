package com.project.chatapp.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageRequest {

    Long userId;
    Long chatId;
    String content;

}
