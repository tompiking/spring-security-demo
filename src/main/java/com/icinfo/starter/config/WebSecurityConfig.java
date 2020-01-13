 package com.icinfo.starter.config;

import java.io.IOException;
import java.io.PrintWriter;

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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.icinfo.starter.provider.CustomAuthenticationProvider;
import com.icinfo.starter.provider.datasource.CustomAuthenticationDetailsSource;
import com.icinfo.starter.service.MyUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    /** 自定义用户信息详情，增加图形验证码 */
    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    /** 自定义登录认证，增加图形验证码 */
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/myLogin.html", "/captcha/**", "/test").permitAll()
            .antMatchers("/user/**")
            .hasAuthority("USER").antMatchers("/admin/**").hasAuthority("ADMIN")
            .anyRequest().authenticated().and()
            .formLogin().authenticationDetailsSource(customAuthenticationDetailsSource)
            .loginPage("/myLogin.html")
            .loginProcessingUrl("/mylogin")
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
            }).failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                    PrintWriter out = response.getWriter();
                    out.write(exception.getMessage());
                }
            }).and().rememberMe()
            /** 
             * 使用rememberMe时必须指定，否则执行autoLogin时会报错 
             * TODO 问题，由于密码经过二次BCrypt加密（每次加密结果都不一样），导致自动登录时token无法比对一致，需要解决
             */
            .userDetailsService(userDetailsService).key("tom").and().csrf().disable();
        // 1、过滤器方式验证登录
        // http.addFilterAt(customFromLoginFilter(), UsernamePasswordAuthenticationFilter.class);

        // 图形验证码过滤器
        // http.addFilterBefore(new CaptchaValidateFilter(), UsernamePasswordAuthenticationFilter.class);

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
        // auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        // 自定义校验过程，就不用上面的设置了，不然会加入默认的DaoAuthenticationProvider校验，结果就是自定义的校验即使不通过，校验结果也可能是校验正确
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

}
