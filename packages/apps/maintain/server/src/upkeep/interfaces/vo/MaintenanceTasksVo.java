package com.guhecloud.cube.srv.gen.maintain.server.upkeep.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class MaintenanceTasksVo {
    /**
     * id
     */
    private Long id;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 计划名称
     */
    private String maintenancePlanName;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private String categoryName;
    /**
     * 对象id
     */
    private Long objectId;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 任务状态
     */
    private String maintenanceTaskStatus;
    //编码
    private String code;
    /**
     * 维保发布时间
     */
    private String maintenanceTime;
    /**
     * 维保完成时间
     */
    private String maintenanceFinishTime;
    /**
     * 图片
     */
    private String picture;
    /**
     * 描述
     */
    private String taskRemark;
    /**
     * 维保人员
     */
    private String maintenanceTaskPerson;
    /**
     * 维保人员id
     */
    private Long maintenanceTaskPersonId;

    /**
     * 部门id
     */
    private String receiveDeptId;

    /**
     * 岗位id
     */
    private String receivePostId;

    private String responsibilityType;

    /**
     * 责任id
     */
    private String responsibilityPersonId;

    //责任人
    private String responsibilityPersonName;

    /**
     * 岗位名称
     */
    private String responsibilityPositionName;
    /**
     * 岗位名称
     */
    private String responsibilityOrganizationName;

    //人员
    private List<String> receiveIds;

    //部门ids
    private List<String> receiveDeptIds;
    //岗位ids
    private List<String> receivePostIds;

    /**
     * 图片
     */
    private List<FileVO> fileVOS;
    //维保记录
    List<MaintenanceProgetRecordVo> maintenanceProgetRecordVos;
}
