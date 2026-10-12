package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user;

import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 17:36
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "user-update";
    /**
     * 用户主键
     */
    private Long id;
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 移动电话
     */
    private String mobile;

    /**
     * 部门
     */
    private Long organizationId;

    private Gender gender;

    private Boolean enabled;

    /**
     * 状态
     */
    private String status;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 短号
     */
    private String cornet;

}
