package com.guhecloud.cube.biz.ouaa.server.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordUtils {
    private final PasswordEncoder passwordEncoder;
    private final static String INITIALIZE_PASSWORD = "qwer@Df456!";

    public String getInitializePassword() {
        return passwordEncoder.encode(INITIALIZE_PASSWORD);
    }

    public String getInitializePassword(String mobile) {
        var trimMobile = mobile.trim();
        var lastFour = trimMobile.substring(trimMobile.length() - 4);
        return passwordEncoder.encode(INITIALIZE_PASSWORD + lastFour);

    }
}