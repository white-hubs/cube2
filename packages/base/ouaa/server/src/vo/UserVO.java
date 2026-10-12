package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
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
public class UserVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 电子邮箱
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
    /**
     * 用户密码
     */
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
     * 照片主键
     */
    private Long photographKey;
    private String avatar;

    /**
     * 组织机构名称
     */
    private List<Organization> organizations;

    /**
     * 管理的部门
     */
    private List<Organization> managedOrganizations;

    /**
     * 组织机构名称
     */
    private Organization organization;

    /**
     * 岗位列表
     */
    private List<Position> positions;

    /**
     * 角色名称列表
     */
    private List<Role> roles;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 文件信息
     */
    private FileVO fileVO;
    private List<Appointment> appointmentIds;
    private List<AppointmentDetailVO> appointmentList;
    /**
     * 照片集合
     */
    private List<FileVO> photographList;
    /**
     * 租户集合
     */
    private List<UserTenant> tenants;
    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 是否绑定手机
     */
    private Boolean isBindTel;
    /**
     * 是否绑定微信
     */
    private Boolean isBindWechat;
    /**
     * 微信名称
     */
    private String wechatName;

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
