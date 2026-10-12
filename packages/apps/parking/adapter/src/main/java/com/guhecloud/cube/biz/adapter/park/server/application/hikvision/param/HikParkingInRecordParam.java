package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param;

import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.model.InResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 海康入场放行事件
 * @date 2024/3/5 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikParkingInRecordParam extends HikParkingRecordParam {

    /**
     * 放行结果数据
     */
    private InResult inResult;

}
