package com.project.chatapp.controller;

import com.project.chatapp.config.TokenProvider;
import com.project.chatapp.exception.UserException;
import com.project.chatapp.model.User;
import com.project.chatapp.repository.UserRepository;
import com.project.chatapp.request.LoginRequest;
import com.project.chatapp.response.AuthResponse;
import com.project.chatapp.service.CustomUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenProvider tokenProvider;
    CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String fullName = user.getFullName();
        String password = user.getPassword();

        User isUser = userRepository.findByEmail(email);
        if (isUser != null) {
            throw new UserException("Email is used with another account: " + email);
        }

        User createdUser = User.builder()
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse response = AuthResponse.builder()
                .jwt(jwt)
                .isAuth(true)
                .build();

        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse response = AuthResponse.builder()
                .jwt(jwt)
                .isAuth(true)
                .build();

        return new ResponseEntity<AuthResponse>(response, HttpStatus.ACCEPTED);
    }


    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
