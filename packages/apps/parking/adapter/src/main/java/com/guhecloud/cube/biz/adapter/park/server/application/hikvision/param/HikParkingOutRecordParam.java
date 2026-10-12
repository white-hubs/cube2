package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param;

import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.model.OutResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 海康入场出场放行事件
 * @date 2024/3/5 18:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HikParkingOutRecordParam extends HikParkingRecordParam {
    /**
     * 放行结果数据
     */
    private OutResult outResult;

}
