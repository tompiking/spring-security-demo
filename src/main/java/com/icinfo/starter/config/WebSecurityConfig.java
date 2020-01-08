 package com.icinfo.starter.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.icinfo.starter.service.MyUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/myLogin.html", "/test").permitAll().antMatchers("/user/**")
            .hasAuthority("USER").antMatchers("/admin/**").hasAuthority("ADMIN")
            .anyRequest().authenticated().and()
            .formLogin().loginPage("/myLogin.html").loginProcessingUrl("/mylogin")
            // 登录成功跳转：
            // 登录成功，如果是直接从登录页面登录，会跳转到该URL；
            // 如果是从其他页面跳转到登录页面，登录后会跳转到原来页面。
            // 可设置true来任何时候到跳转 .defaultSuccessUrl("/hello2", true);
            .defaultSuccessUrl("/user")
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                    RequestCache cache = new HttpSessionRequestCache();
                    SavedRequest savedRequest = cache.getRequest(request, response);
                    String url = savedRequest.getRedirectUrl();
                }
            }).and().csrf().disable();
        // 1、过滤器方式验证登录
        // http.addFilterAt(customFromLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // TODO 图形验证码过滤器

    }

    /**
     * 自定义认证过滤器
     */
    private CustomFromLoginFilter customFromLoginFilter() {
        return new CustomFromLoginFilter("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /// 2、spring security自定义认证方式验证登录
        // 指定获取用户信息服务和密码加密方式为BCrypt，密码存入数据库也要以这个方式加密保存
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}
