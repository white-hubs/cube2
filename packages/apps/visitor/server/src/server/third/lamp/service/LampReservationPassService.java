package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.EventLampCallbackParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.PassRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.PassRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 第三方预约通行
 */
public interface LampReservationPassService {
    Pagination<PassRecordResult> passRecordPage(PassRecordParam param);

    ReservationResult reservation(ReservationParam param);

    @Deprecated
    Pagination<ReservationRecordResult> reservationRecordPage(ReservationRecordParam param);

    String getReservationCode(Long id);

    void updateReservation(String ids, Integer status);

    void visitorBusinessEventCallback(EventLampCallbackParam param);
}
