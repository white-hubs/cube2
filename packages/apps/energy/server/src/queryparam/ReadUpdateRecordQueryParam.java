package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lrj
 * @Description 抄表修改记录
 * @date 2022-07-04 15:12:36
 */
@Data
public class ReadUpdateRecordQueryParam extends Paging {

    //修改前值
    private BigDecimal updateOldValue;
    //修改值
    private BigDecimal updateValue;
    //设备id
    private String deviceId;
    //设备名称
    private String deviceName;

}