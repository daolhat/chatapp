package com.project.chatapp.request;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {

    String fullName;

//    String email;

    String avatar;

//    String password;

}
