 package com.icinfo.starter.captcha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;

/**
 * 图形验证码
 * 
 * @author tanbijing
 * @date 2020/01/09
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    Producer captchaProducer;

    /**
     * 获取验证码
     *
     * @author tanbijing
     * @date 2020/01/09
     * @param request
     * @param response
     */
    @GetMapping("/img")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        request.getSession().setAttribute("captcha", capText);
        BufferedImage image = captchaProducer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

}
