 package com.icinfo.starter.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.icinfo.starter.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 从数据库中获取用户数据
        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setUsername("tom");
        List<GrantedAuthority> auth = new LinkedList<GrantedAuthority>();
        GrantedAuthority a = new GrantedAuthority() {
            private static final long serialVersionUID = 1L;
            @Override
            public String getAuthority() {
                return "USER";
            }
        };
        auth.add(a);
        user.setAuthorities(auth);
        return user;
    }

}
