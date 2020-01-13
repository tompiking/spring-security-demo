 package com.icinfo.starter.config;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class CaptchaValidateFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (!"/mylogin".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            // 校验验证码
            String inptCaptcha = request.getParameter("captcha");
            HttpSession session = request.getSession();
            String captcha = (String)session.getAttribute("captcha");
            if (!StringUtils.isEmpty(captcha)) {
                session.removeAttribute("captcha");
            }
            if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(inptCaptcha) || !inptCaptcha.equals(captcha)) {
                throw new AuthenticationException("验证码错误");
            }
            filterChain.doFilter(request, response);
        }

    }

}
