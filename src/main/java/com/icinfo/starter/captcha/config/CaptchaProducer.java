 package com.icinfo.starter.captcha.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Component
public class CaptchaProducer {

    @Bean
    public Producer captcha() {
        Properties pro = new Properties();
        pro.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, "150");
        pro.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, "50");
        pro.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "123456789");
        pro.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");

        Config config = new Config(pro);
        DefaultKaptcha captcha = new DefaultKaptcha();
        captcha.setConfig(config);
        return captcha;
    }
}
