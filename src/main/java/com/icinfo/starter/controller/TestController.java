 package com.icinfo.starter.controller;

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
