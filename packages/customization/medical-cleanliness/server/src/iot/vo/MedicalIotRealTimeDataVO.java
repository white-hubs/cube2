package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeData;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeDataDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-22  20:01
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalIotRealTimeDataVO {
    private Long id;
    /**
     * 企业名称
     */
    private String tenantName;
    /**
     * 所属部门
     */
    private Long depId;
    private String depName;
    /**
     * 设备id
     */
    private Long deviceId;
    private String deviceName;
    /**
     * 指标状态
     */
    private String deviceStatus;
    /**
     * 指标值
     */
    private List<MedicalIotRealTimeDataDetails> data;

}
