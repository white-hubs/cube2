package com.guhecloud.cube.biz.ouaa.server.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtController implements ApplicationContextAware {

    private Map<String, Object> publicJwtKey;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        // 初始化公钥数据
        KeyPair keyPair = applicationContext.getBean(KeyPair.class);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        this.publicJwtKey = Collections.unmodifiableMap(new JWKSet(key).toJSONObject());
    }

    @GetMapping("/oauth2/jwt.key")
    public Map<String, Object> key() {
        return this.publicJwtKey;
    }

}
