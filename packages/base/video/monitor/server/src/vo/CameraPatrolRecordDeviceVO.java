package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频巡逻设备记录返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:50
 */
@Data
public class CameraPatrolRecordDeviceVO {

    private Long id;

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 巡逻监控设备主键
     */
    private Long deviceId;

    /**
     * 巡逻监控设备编号
     */
    private String deviceCode;

    /**
     * 巡逻监控设备名称
     */
    private String deviceName;

    private String area;

    /**
     * 所属区域名称
     */
    private String areaNames;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 巡逻时间
     */
    private LocalDateTime patrolTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 巡逻结果
     */
    private CameraPatrolDeviceResultEnum result;

    /**
     * 巡逻状态
     */
    private CameraPatrolDeviceStatusEnum status;

    /**
     * 异常数量
     */
    private Integer abnormalNum;

    private FileVO image;
}