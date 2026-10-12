package com.guhecloud.cube.biz.ouaa.server.dto;

import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 用户组织关联信息
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserOrganizationDTO extends Organization {
    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 岗位主键
     */
    private Long positionId;
    /**
     * 职位
     */
    private Long appointmentId;
}
