package com.guhecloud.cube.biz.ouaa.server.rocketmq.message;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户注册消息
 *
 * @author Woody
 * @date 2021/9/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "user-update";

    /**
     * 主键
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

    private List<Long> positionIds;

    private Gender gender;

    private Boolean enabled;
    private Integer tenant;
    /**
     * 是否修改照片
     */
    private boolean photographEdit;
    /**
     * 人员照片业务主键
     */
    private Long photographKey;

    /**
     * 时间戳
     */
    @Builder.Default
    private final Long timestamp = System.currentTimeMillis();
    private List<Long> appointmentIds;

    /**
     * 身份证号
     */
    private String idCard;

}
