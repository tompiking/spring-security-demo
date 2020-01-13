 package com.icinfo.starter.provider.datasource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

public class CustomAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 1L;

    private Boolean imageCodeIsRight;

    public Boolean getImageCodeIsRight() {
        return this.imageCodeIsRight;
    }

    public CustomAuthenticationDetails(HttpServletRequest request) {
        super(request);
        // 处理图形验证码数据
        this.imageCodeIsRight = true;
        String inptCaptcha = request.getParameter("captcha");
        HttpSession session = request.getSession();
        String captcha = (String)session.getAttribute("captcha");
        if (!StringUtils.isEmpty(captcha)) {
            session.removeAttribute("captcha");
        }
        if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(inptCaptcha) || !inptCaptcha.equals(captcha)) {
            this.imageCodeIsRight = false;
        }
    }
}
