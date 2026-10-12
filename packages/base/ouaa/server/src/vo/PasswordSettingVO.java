package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/10/12 15:55
 * @description
 */
@Data
public class PasswordSettingVO {
    private Long id;
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
}
