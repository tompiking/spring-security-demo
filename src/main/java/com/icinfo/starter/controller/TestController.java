 package com.icinfo.starter.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icinfo.starter.model.User;

@RestController
public class TestController {

    @GetMapping("/user")
    public String user(String phone) {
        String a = new BCryptPasswordEncoder().encode("123456");
        String b = new BCryptPasswordEncoder().encode("123456");
        String c = new BCryptPasswordEncoder().encode("123456");
        String d = new BCryptPasswordEncoder().encode("123456");
        String e = new BCryptPasswordEncoder().encode("123456");
        System.out.println(new BCryptPasswordEncoder().matches("123456", a));
        System.out.println(new BCryptPasswordEncoder().matches(a, c));
        System.out.println(new BCryptPasswordEncoder().matches(a, d));
        System.out.println(new BCryptPasswordEncoder().matches(a, e));
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/test")
    public String test(@Validated() User user, BindingResult result) {
        StringBuffer reMsg = new StringBuffer();
        for (FieldError error : result.getFieldErrors()) {
            reMsg.append(error.getDefaultMessage());
            reMsg.append("\n");
        }
        return reMsg.toString();
    }

}
