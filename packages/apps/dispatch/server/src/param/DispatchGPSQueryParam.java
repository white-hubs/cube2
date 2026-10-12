package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Appet
 * @Description GPS历史轨迹查询
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchGPSQueryParam extends Paging {

    //组编号
    @NotBlank
    private String code;
    //查询时间段
    @NotNull
    private Period period;

}