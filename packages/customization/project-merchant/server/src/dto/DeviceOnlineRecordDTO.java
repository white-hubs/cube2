package com.guhecloud.cube.deliver.project.merchant.dto;


import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.DeviceOnlineRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 设备离线记录表
 * </p>
 *
 * @author bq
 * @since 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceOnlineRecordDTO extends ConvertionDTO<DeviceOnlineRecordDTO, DeviceOnlineRecord> {

    public DeviceOnlineRecordDTO() {
    }

    public DeviceOnlineRecordDTO(DeviceOnlineRecord record) {
        super();
        map(record);
    }

    public DeviceOnlineRecordDTO(List<DeviceOnlineRecord> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 是否在线
     */
    private int online;

    /**
     * 上下线时间
     */
    private LocalDateTime date;

}
