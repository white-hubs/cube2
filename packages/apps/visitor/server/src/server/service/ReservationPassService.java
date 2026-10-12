package com.guhecloud.cube.srv.gen.visitor.management.server.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.PassRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ContrastResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.PassRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.time.temporal.ChronoUnit;

/**
 * 预约通行
 */
public interface ReservationPassService {

    Pagination<PassRecordResult> passRecordPage(PassRecordParam param);

    Pagination<ReservationRecordResult> reservationRecordPage(ReservationRecordParam param);

    ReservationResult reservation(ReservationParam param);

    ReservationRecordResult reservationDetail(Long id);

    String getReservationCode(Long id);

    void cancelReservation(Long id);

    Boolean phoneIsReservation(ReservationParam param);

    ContrastResult getReservationStatRatio(Period period, ChronoUnit type);

    /**
     * 预约记录环比(日环比+周环比)
     *
     * @return 环比
     */
    QoqStatResult dayAndWeekRatio();

    /**
     * 获取预约人总数
     *
     * @param param
     *
     * @return
     */
    Long reservationRecordCount(ReservationRecordParam param);

    /**
     * 人事-获取访客列表
     *
     * @param param
     *
     * @return
     */
    Pagination<ReservationRecordResult> reservationPersonnelPage(ReservationRecordParam param);

    /**
     * 根据流程id 查询预约信息
     *
     * @param processInsId
     *
     * @return
     */
    ReservationRecordResult getReservationDetailByProcess(String processInsId);

}
