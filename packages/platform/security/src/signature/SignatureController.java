package com.guhecloud.platform.security.signature;

import com.guhecloud.platform.security.config.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数字签名接口
 *
 * @author Woody
 * @date 2021/8/30
 */
@RestController
@ConditionalOnBean(Signer.class)
@ConditionalOnProperty(prefix = "platform.security.signature", name = "enable", matchIfMissing = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignatureController {
    private final Signer signer;
    private final SecurityProperties securityProperties;

    /**
     * 数字签名
     *
     * @param parameters 签名参数
     *
     * @return 签名信息
     */
    @PostMapping("/sign")
    public String sign(@RequestBody Map<?, ?> parameters) {
        return this.signer.sign(parameters, this.securityProperties.getSignature().getExpiration());
    }
}
