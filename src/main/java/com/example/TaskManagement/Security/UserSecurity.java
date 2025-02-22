package com.example.TaskManagement.Security;

import com.example.TaskManagement.API.Model.User;
import com.example.TaskManagement.API.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;



//not UserDetails is an Interface which is being implemented as a Spring security User Class.
@Component
@RequiredArgsConstructor
public  class UserSecurity implements UserDetailsService {

    @Autowired
    public UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),new ArrayList<>());
    }
}
