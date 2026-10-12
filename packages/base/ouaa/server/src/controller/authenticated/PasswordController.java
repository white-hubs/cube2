package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.guhecloud.cube.biz.ouaa.server.entity.PasswordSetting;
import com.guhecloud.cube.biz.ouaa.server.param.PasswordSettingParam;
import com.guhecloud.cube.biz.ouaa.server.service.PasswordService;
import com.guhecloud.cube.biz.ouaa.server.vo.PasswordSettingVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/10/12 15:07
 * @description
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/password/setting")
    public void setting(@RequestBody PasswordSettingParam param) {
        passwordService.setting(param.getUsefulLife(),
                                param.getDueToPromptDay(),
                                param.getForced(),
                                param.getManyLoginFailuresCalibrated(),
                                param.getAccountLoginLockingDuration(),
                                param.getAccountLoginAllowedFailNum(),
                                param.getAccountLoginAllowedFailDuration());
    }

    @GetMapping("/password/detail")
    public PasswordSettingVO get() {
        Optional<PasswordSetting> passwordSettingOptional = this.passwordService.get();
        if (!passwordSettingOptional.isPresent()) {
            return null;
        }
        return ObjectUtils.copying(passwordSettingOptional.get(), PasswordSettingVO.class);
    }
}
