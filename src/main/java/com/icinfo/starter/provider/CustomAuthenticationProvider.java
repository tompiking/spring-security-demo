 package com.icinfo.starter.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.icinfo.starter.provider.datasource.CustomAuthenticationDetails;

@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.setPasswordEncoder(new BCryptPasswordEncoder());
        // 必须初始化，不然会报null异常
        this.setUserDetailsService(userDetailsService);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        CustomAuthenticationDetails detail = (CustomAuthenticationDetails)authentication.getDetails();
        if (!detail.getImageCodeIsRight()) {
            throw new AuthenticationException("验证码错误") {};
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
