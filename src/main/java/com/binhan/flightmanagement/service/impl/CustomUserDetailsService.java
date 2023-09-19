package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.models.UserEntity;
import com.binhan.flightmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository= userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(username).
                orElseThrow( () -> new UsernameNotFoundException("UserName not found"));
        Collection<GrantedAuthority> grantedAuthorities = user.getRoles().
                stream().map(role -> new SimpleGrantedAuthority(("ROLE_" + role.getCode()))).collect(Collectors.toList());
        return new User(user.getUserName(),user.getPassword(),grantedAuthorities);
    }
}
