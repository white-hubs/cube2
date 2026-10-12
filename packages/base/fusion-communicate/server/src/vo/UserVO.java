package com.guhecloud.cube.biz.fusion.communicate.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.fusion.communicate.server.gps.vo.UserGpsHistoriesVO;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.vo.AccountVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
     * 照片主键
     */
    private Long photographKey;

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
    private FileVO file;

    /**
     * 用户坐标信息
     */
    private UserGpsHistoriesVO gps;

    /**
     * 通讯账号信息
     */
    private AccountVO account;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 组织机构信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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
    @AllArgsConstructor
    @NoArgsConstructor
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
