package com.guhecloud.cube.biz.ouaa.client.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息
 *
 * @author Woody
 * @date 2021/7/20
 */
@Data
public class UserDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 用户性别
     */
    private Gender gender;

    /**
     * 工作状态
     */
    private String workStatus;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 登录账号
     */
    private String username;

    private String password;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 入职日期
     */
    private LocalDateTime entryDate;

    /**
     * 创建时间（注册时间）
     */
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 人员照片业务主键
     */
    private Long photographKey;

    /**
     * 组织机构名称
     */
    private Organization organization;

    private List<Organization> organizations;

    /**
     * 岗位列表
     */
    private List<Position> positions;
    /**
     * 岗位列表
     */
    private List<Appointment> appointments;

    /**
     * 岗位列表别名，兼容 OUAA 用户分页返回的 appointmentList 字段
     */
    private List<Appointment> appointmentList;
    /**
     * 角色名称列表
     */
    private List<Role> roles;

    /**
     * 人员照片
     */
    private List<FileVO> photographList;
    /**
     * 租户集合
     */
    private List<UserTenantDTO> tenants;
    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 组织机构信息
     */
    @Data
    public static class Organization {
        /**
         * 组织机构主键
         */
        private Long id;

        /**
         * 组织机构名称
         */
        private String name;
    }

    @Data
    public static class Position {
        /**
         * 岗位主键
         */
        private Long id;

        /**
         * 岗位名称
         */
        private String name;
    }

    @Data
    public static class Appointment {
        /**
         * 岗位主键
         */
        private Long id;

        /**
         * 岗位名称
         */
        private String name;
    }

    /**
     * 角色信息
     */
    @Data
    public static class Role {
        /**
         * 角色主键
         */
        private Long id;

        /**
         * 角色名称
         */
        private String name;
    }
}
