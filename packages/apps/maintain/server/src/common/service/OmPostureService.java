package com.guhecloud.cube.srv.gen.maintain.server.common.service;

import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultHandleStatusStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.FaultObjectTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectRecordTrendVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.InspectTaskTop5Vo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.MaintenanceStatisticsVo;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.om.TicketStatisticsVo;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.PeriodTypeEnum;
import lombok.NonNull;

import java.util.List;

public interface OmPostureService {
    TicketStatisticsVo ticketStatistics(@NonNull Period period);

    List<FaultObjectTop5Vo> faultObjectTop5(@NonNull Period period);

    List<FaultHandleStatusStatisticsVo> faultHandleStatusStatistics(@NonNull Period period);

    List<MaintenanceStatisticsVo> maintenanceStatistics(@NonNull Period period, @NonNull PeriodTypeEnum type);

    List<InspectTaskTop5Vo> patrolTaskTop5(@NonNull Period period);

    List<InspectRecordTrendVo> inspectRecordTrend(@NonNull Period period, @NonNull PeriodTypeEnum periodTypeEnum);
}
