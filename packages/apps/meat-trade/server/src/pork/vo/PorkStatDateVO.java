package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 三维可视化日期工具处理返回结果
 *
 * @author lanchun
 * @date 2023/8/4 15:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PorkStatDateVO {

    private Period currentDate;

    private Period lastDate;
}
