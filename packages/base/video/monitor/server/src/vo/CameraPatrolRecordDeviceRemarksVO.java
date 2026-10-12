package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频巡逻设备记录备注
 *
 * @author lanchun
 * @date 2022/8/4 14:05
 */
@Data
public class CameraPatrolRecordDeviceRemarksVO {

    private Long id;

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 视频巡逻设备记录主键
     */
    private Long cameraPatrolRecordDeviceId;

    /**
     * 巡逻监控设备主键
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
     * 序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件地址字符串
     */
    private String filePaths;

    /**
     * 附件地址集合
     */
    private List<CameraPatrolFileVO> filePathList;

    /**
     * 图片
     */
    private FileVO image;

    /**
     * 巡逻人
     */
    private String createByName;

    /**
     * 巡逻时间
     */
    private LocalDateTime createTime;
}
