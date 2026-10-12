package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InspectTargetRepairVO {

    private Long id;
    /**
     * 任务ID（临时巡检没有任务ID）
     */
    private Long taskId;

    private Long planId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 目标对象类型
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 巡检对象名
     */
    private String targetName;

    /**
     * 状态
     */
    private String status;

    /**
     * 修复状态
     */
    private String repairStatus;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 协同人ids
     */
    private String assistIds;

    /**
     * 协同人名称
     */
    private String assistNames;

    /**
     * 用户姓名
     */
    private String nickname;

    /**
     * 巡检项名称
     */
    private String itemName;

    /**
     * 巡检结果
     */
    private String result;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 设备区域id
     */
    private String areaId;
    /**
     * 设备区域名称
     */
    private String areaName;

    /**
     * 具体地址
     */
    private String address;

    /**
     * 记录转其他业务记录
     */
    private List<InspectRecordToBusinessVO> recordToBusinessVOS;

    /**
     * 异常项ids
     */
    private String errIds;
    /**
     * 异常项名称
     */
    private String errNames;
    /**
     * 取消巡检原因
     */
    private String cancelInspectReason;
    /**
     * 取消巡检文件关联id
     */
    private Long cancelInspectFileKey;
    /**
     * 取消巡检文件集合
     */
    private List<FileVO> cancelInspectFileList;
    /**
     * 取消修复文件的关联key
     */
    private Long cancelFileKey;

    /**
     * 修复取消原因
     */
    private String cancelReason;

    /**
     * 取消修复的文件
     */
    private List<FileVO> cancelFileVOList;

    /**
     * 手动处理集合
     */
    private List<InspectRecordRepairVO> inspectRecordRepairVOList;

    /**
     * 巡检项
     */
    private List<InspectRecordItemVO> itemVOList;
}
