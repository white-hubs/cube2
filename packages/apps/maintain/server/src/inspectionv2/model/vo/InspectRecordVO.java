package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InspectRecordVO {

    private Long id;

    /**
     * 任务ID（临时巡检没有任务ID）
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 计划名
     */
    private String planName;

    /**
     * 目标对象类型
     */
    private String targetCategory;

    /**
     * 目标对象ID
     */
    private Long targetId;

    /**
     * 目标对象名
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
     * 组织机构ID
     */
    private Long organizationId;

    /**
     * 组织机构名
     */
    private String organizationName;

    /**
     * 岗位ID
     */
    private Long positionId;

    /**
     * 岗位名
     */
    private String positionName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

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
     * 协同人对象
     */
    private String assistNames;

    /**
     * 异常项数量
     */
    private Integer errValueNum;
    /**
     * 设备区域id
     */
    private String areaId;
    /**
     * 设备区域名称
     */
    private String areaName;

    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;
    /**
     * 巡检对象具体地址
     */
    private String targetAddress;
    /**
     * 记录项
     */
    private List<InspectRecordItemVO> recordItemVOS;

    /**
     * 记录转其他业务记录
     */
    private List<InspectRecordToBusinessVO> recordToBusinessVOS;

    /**
     * 记录标准
     */
    private List<InspectRecordStandardVO> standardVOS;
    /**
     * 巡检方式
     */
    private String inspectMethod;
    /**
     * 异常项ids
     */
    private String errIds;
    /**
     * 异常项名称
     */
    private String errNames;

    /**
     * 异常项描述
     */
    private String errDesc;

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
     * 修复取消原因
     */
    private String cancelReason;
    /**
     * 取消修复文件关联id
     */
    private Long cancelFileKey;

    /**
     * 抓拍附件（视频转存记录 id，逗号拼接）
     */
    private String snapAttachmentIds;

    /**
     * 取消修复文件集合
     */
    private List<FileVO> cancelFileList;

    /**
     * 手动处理集合
     */
    private List<InspectRecordRepairVO> recordHandledManuallyPOS;

    /**
     * 抓拍截图地址列表
     */
    private List<String> snapCaptureUrls;

}
