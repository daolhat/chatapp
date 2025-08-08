package com.project.chatapp.service;

import com.project.chatapp.model.User;
import com.project.chatapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
         if (user == null) {
             throw new UsernameNotFoundException("User not found with username: " + username);
         }
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new  org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
