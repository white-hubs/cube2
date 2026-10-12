package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 视频巡逻设备记录备注保存参数
 *
 * @author lanchun
 * @date 2022/8/4 15:45
 */
@Data
public class CameraPatrolRecordDeviceRemarksModifyParam {

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 视频巡逻设备记录主键
     */
    private Long cameraPatrolRecordDeviceId;

    /**
     * 巡逻设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @NotEmpty(message = "备注不能为空")
    @Size(max = 300, message = "备注不能大于300个字符")
    private String remark;

    private String filePaths;

    /**
     * 抓拍附件地址
     */
    private String filePathList;
}
