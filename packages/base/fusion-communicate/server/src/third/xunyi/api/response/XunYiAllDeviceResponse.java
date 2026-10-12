package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 讯一设备返回参数
 *
 * @author lanchun
 * @date 2024/7/30 17:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XunYiAllDeviceResponse {

    private static final long serialVersionUID = 1L;

    /**
     * 员工工号
     */
    private String employeeId;

    /**
     * 分组名称
     */
    private String groupNum;

    /**
     * 密码
     */
    private String password;

    /**
     * 员工级别
     */
    private Integer employeeLevel;

    /**
     * 员工名称
     */
    private String name;

    /**
     * 员工类型
     */
    private String type;

    /**
     * 子类型
     */
    private String subType;

    /**
     * 描述
     */
    private String remark;

    /**
     * 是否开启录音
     */
    private Integer isRecord;

    /**
     * 是否开启录像
     */
    private Integer isVideo;
}
