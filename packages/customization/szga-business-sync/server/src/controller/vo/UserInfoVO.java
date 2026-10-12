package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.StatusEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/20 16:24
 * @Description: 员工信息
 */

@Data
public class UserInfoVO {

    /**
     * 用户唯一id
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户电话
     */
    private String mobile;

    /**
     * 员工状态（FORMAL("正式"), LEAVE_OFFICE("离职"), ON_TRIAL("试用")）
     */
    private String userStatus;

    /**
     * 部门全路径id
     */
    private String orgPathId;

    /**
     * 部门全路径名称
     */
    private String orgPathName;

    /**
     * 身份证号
     */
    private String paperNumber;

    /**
     * 物理卡号
     */
    private String serialNumber;

    /**
     * 卡状态（OPEN开卡、CANCEL冻结，LOGOFF注销）
     */
    private StatusEnum cardStatus;

    /**
     * 有效期至（日期格式可能需要根据实际情况调整）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String validityDate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
}
