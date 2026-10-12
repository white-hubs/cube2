package com.guhecloud.cube.base.message2.client;

import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message2")
public interface CodeClient {
    @PostMapping("/send/code")
    CodeResponseDTO sendCode(@Valid @RequestBody SendVerifyCodeParam param);

    /**
     * 短信验证码校验
     */
    @PostMapping("/verify/code")
    CodeResponseDTO verifyCode(@Valid @RequestBody CodeVerifyParam param);

}
