package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.QueryDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.PageVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.PeriodQuery;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HazardStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.platform.base.model.Period;

/**
 * @author lzy
 * @date 2022/4/26
 */
public interface HiddenDangerQueryService extends IService<HiddenDangerNew> {

    Integer isHiddenDangerValid(String deviceId, Period period);

    Page<PageVO> getHiddenDangerCount(QueryDTO query);

    HazardStatisticsVO getHazardStatistics(PeriodQuery period);
}
