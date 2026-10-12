package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/10/12 16:03
 * @description
 */
@Data
public class PasswordSettingParam {
    private Integer usefulLife;
    private Integer dueToPromptDay;
    private Boolean forced;
    /**
     * 登录失败次数过多校验是否开启
     */
    private Boolean manyLoginFailuresCalibrated;

    /**
     * 账号登录失败次数过多的锁定时长(目前默认为分钟)
     */
    private Integer accountLoginLockingDuration;

    /**
     * 账号登录失败在一定范围内允许重试的次数
     */
    private Integer accountLoginAllowedFailNum;

    /**
     * 账号登录登录失败允许重试的规定时间范围(目前默认为分钟)(在一定时间内且不超过登录次数的限制下还可继续登录)
     */
    private Integer accountLoginAllowedFailDuration;
}
