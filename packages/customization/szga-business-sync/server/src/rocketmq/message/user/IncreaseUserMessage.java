package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user;

import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 16:25
 * @Description: 新增用户消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseUserMessage {

    public final static String TOPIC = "TENANT_USER_INCREASE";
    private Long id;
    private String name;

    private String mobile;

    private Long organizationId;

    private Gender gender;
    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 状态
     */
    private String status;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 短号
     */
    private String cornet;
}
