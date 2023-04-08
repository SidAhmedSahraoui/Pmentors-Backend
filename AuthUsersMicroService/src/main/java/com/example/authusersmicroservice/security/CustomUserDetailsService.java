package com.example.authusersmicroservice.security;

import com.example.authusersmicroservice.models.User;
import com.example.authusersmicroservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository ;

    @Override
    public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailOrUsernameOrPhone(credential).orElseThrow(()-> new UsernameNotFoundException("User not found !"));
        return  user ;
    }
}

