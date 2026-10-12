package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author appet
 * @Description 能耗日报
 * @date 2021-12-30 15:12:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnergyTrendUsageParam extends Paging {

    private String instrumentType;//仪表类型
    private String projectIds;//分项ID 可能是projectId,areaId,routeId
    private Period period;
    private String projectCategory;//CATEGORY分类 AREA区域 ITEM分项 ROUTE支路
    private List<Long> projectIdList;
    private String type;//时间类型
    private String groupFormat;//统计时需要的格式
    private Long deviceId;
}