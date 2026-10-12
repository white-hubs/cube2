package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 应急设备使用记录
 * @date 2024-01-26 10:08:36
 */
@Data
public class EmergencyDeviceUsedRecordQueryParam extends Paging {

    //设备ID
    private Long deviceId;
    //使用人ID
    private Long userId;
    //使用人姓名
    private String userName;
    //使用时间
    private LocalDateTime usedTime;
    //使用数量
    private Integer usedNum;
    //使用类型编号
    private String usedTypeCode;
    //使用类型名称
    private String usedTypeName;

}