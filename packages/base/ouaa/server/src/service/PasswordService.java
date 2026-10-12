package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.PasswordSetting;

import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/10/12 15:09
 * @description
 */
public interface PasswordService extends IService<PasswordSetting> {

    /**
     * 密码有效设置
     *
     * @param usefulLife
     * @param dueToPromptDay
     */
    void setting(Integer usefulLife, Integer dueToPromptDay, Boolean forced);

    /**
     * 密码有效设置加登录失败限制设置
     *
     * @param usefulLife
     * @param dueToPromptDay
     */
    void setting(Integer usefulLife, Integer dueToPromptDay, Boolean forced, Boolean manyLoginFailuresCalibrated,
                 Integer accountLoginLockingDuration, Integer accountLoginAllowedFailNum, Integer accountLoginAllowedFailDuration
                );

    Optional<PasswordSetting> get();

}
