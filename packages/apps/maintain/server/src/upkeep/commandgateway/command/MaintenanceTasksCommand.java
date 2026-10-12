package com.guhecloud.cube.srv.gen.maintain.server.upkeep.commandgateway.command;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MaintenanceTasksCommand {

    /**
     * id
     */
    private Long id;
    /**
     * 计划id
     */
    private Long planId;
    /**
     * 品类编码
     */
    private String categoryCode;
    /**
     * 品类名称
     */
    private Long categoryName;
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
    /**
     * 维保发布时间
     */
    private String maintenanceTime;
    /**
     * 维保完成时间
     */
    private LocalDateTime maintenanceFinishTime;
    /**
     * 图片
     */
    private String picture;

    /**
     * 文件参数
     */
    private List<String> fileIds;
    /**
     * 描述
     */
    @Size(max = 500, message = "结论不能超过500")
    private String taskRemark;
    /**
     * 维保人员
     */
    private String maintenanceTaskPerson;
    /**
     * 维保人员id
     */
    private Long maintenanceTaskPersonId;

    //记录项
    @Valid
    List<MaintenanceProgetRecordCommand> maintenanceProgetRecordCommands;
}
