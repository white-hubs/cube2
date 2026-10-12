package com.guhecloud.cube.biz.ouaa.server.param.validated;

import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PasswordValidated {

    /**
     * 密码校验规则
     */
    private static final String REGX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>/?\\\\|\"\\[\\]`~]).{8,}$";

    private final Pattern pattern = Pattern.compile(REGX);

    public void validate(String password) {
        Matcher matcher = pattern.matcher(password);
        // 密码校验
        if (!matcher.matches()) {
            throw new InvalidArgumentException("密码必须包含字母大写、小写、数字、特殊字符(~!@#$%^&*,.),且长度大于8位！");
        }
    }
}
