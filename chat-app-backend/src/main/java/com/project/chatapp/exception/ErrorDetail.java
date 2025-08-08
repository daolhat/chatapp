package com.project.chatapp.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetail {

    String error;
    String message;

    LocalDateTime timeStamp;

}
