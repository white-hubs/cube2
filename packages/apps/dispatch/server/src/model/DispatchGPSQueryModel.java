package com.guhecloud.cube.biz.dispatch.server.model;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description gps数据查询模型
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchGPSQueryModel extends Paging {

    //组编号
    private String code;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

}