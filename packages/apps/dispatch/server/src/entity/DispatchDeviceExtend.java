package com.guhecloud.cube.biz.dispatch.server.entity;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 调度设备-分组-扩展信息
 * @date 2022-07-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DispatchDeviceExtend extends BasicTenantEntity<String> {

    //
    private String id;
    //设备ID
    private String deviceId;
    //是否录音
    private Boolean isRecord;
    //是否录像
    private Boolean isVideo;
    //设备级别
    private Integer deviceLevel;
    //备注
    private String remark;

}