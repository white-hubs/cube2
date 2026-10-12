package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/3 11:32
 * @Description:
 */
@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OuaaUser {
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
     * 用户性别
     */
    private String gender;

    /**
     * 是否启用
     */
    private Integer enabled;

    /**
     * 员工状态
     */
    private String status;

    /**
     * 证件类型
     */
    private String paperType;

    /**
     * 证件号码
     */
    private String paperNumber;

    /**
     * 是否已删除
     */
    private Long deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 租户id
     */
    private Integer tenant;
    private Long organizationId;

    private static final long serialVersionUID = 7819390613160157641L;
}
