package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author appet
 * @Description 能耗日报
 * @date 2021-12-30 15:12:36
 */
@Data
public class EnergyDailyUsageParam extends Paging {

    private String instrumentType;//仪表类型
    private Long projectId;//分项ID 可能是projectId,areaId,routeId
    private Period period;
    private String projectCategory;//CATEGORY分类 AREA区域 ITEM分项 ROUTE支路
    private String exportType; //USAGE 用量 CARBON 碳排 ALL 综合
    private String type;//日期类型: THIS_WEEK THIS_MONTH THIS_YEAR
}