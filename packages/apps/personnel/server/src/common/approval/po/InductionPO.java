package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po;

import annotation.FormField;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo.DoorPermissionVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo.ParkingPermissionVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2023/02/24
 * @description 入职申请表
 */
@Data
public class InductionPO {

    @FormField(label = "员工姓名")
    private String staffName;

    @FormField(label = "身份证号")
    private String idCard;

    @FormField(label = "联系电话")
    private String mobile;

    @FormField(label = "短号")
    private String cornet;

    @FormField(label = "人员类别")
    private String type;

    @FormField(label = "入职部门")
    private List<String> organizationIdList;
    @FormField(label = "入职职位")
    private Long postId;
    @FormField(label = "入职岗位")
    private Long appointment;
    @FormField(label = "入职角色")
    private List<Long> roleId;

    @FormField(label = "入职日期")
    private LocalDateTime inductionDate;

    @FormField(label = "服务期限")
    private LocalDateTime servicePeriod;

    @FormField(label = "权限信息")
    private List<UserPermission> userPermissionList;
    @FormField(label = "消费权限")
    private Boolean consumePm;
    @FormField(label = "停车权限")
    private ParkingPermissionVO parkingPm;

    @FormField(label = "门禁权限")
    private DoorPermissionVO doorPm;

    @Data
    public static class UserPermission {
        /**
         * 停车权限
         */
        private ParkingPermissionVO parkingPm;
        /**
         * 门禁权限
         */
        private DoorPermissionVO doorPm;
        /**
         * 消费权限
         */
        private Boolean consumePm;
    }
}