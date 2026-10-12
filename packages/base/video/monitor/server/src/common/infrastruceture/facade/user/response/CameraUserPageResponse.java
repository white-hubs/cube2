package com.guhecloud.cube.biz.vmonitor.server.common.infrastruceture.facade.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraUserPageResponse {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 用户性别
     */
    private String gender;

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
}
