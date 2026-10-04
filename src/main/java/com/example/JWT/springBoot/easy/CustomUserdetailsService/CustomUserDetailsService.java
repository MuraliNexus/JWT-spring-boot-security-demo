package com.example.JWT.springBoot.easy.CustomUserdetailsService;

import com.example.JWT.springBoot.easy.Entity.Users;
import com.example.JWT.springBoot.easy.Repoistory.UserRepoistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepoistory userRepoistory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepoistory.findByName(username)
                .orElseThrow(()-> new UsernameNotFoundException("username not found"+username));

        return new CustomUserDetails(user);
    }
}
