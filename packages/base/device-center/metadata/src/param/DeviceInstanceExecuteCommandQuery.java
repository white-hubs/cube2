package com.guhecloud.cube.base.device.center.metadata.param;

import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/7/12 16:10
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceInstanceExecuteCommandQuery extends Paging {
    private String keyword;
    private Long genreId;
    private AttrCtlExecuteResultEnum executeResult;
    private Period period;
    private Long deviceInstanceId;
    private Integer tenant;
    private Long deviceId;
    private List<Long> deviceIdList;
}
