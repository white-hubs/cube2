package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.server.common.query.StatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingPayRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayTrendTotalVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.model.PeriodTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.MathUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingPayRecordController {

    private final ParkingPayRecordService parkingPayRecordService;

    /**
     * 停车缴费记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-pay-records")
    public Page<ParkingPayRecordPageVO> getPage(@Valid ParkingPayRecordQueryParam param) {
        Page<ParkingPayRecordPageVO> parkingPayRecordPage = this.parkingPayRecordService.getPage(param);
        return parkingPayRecordPage;
    }

    /**
     * 今日停车缴费金额
     *
     * @return
     */
    @GetMapping("/parking-pay/today-total")
    public ParkingPayTotalVO getTodayParkingPayTotal() {
        var beginTime = DateUtils.beginOfDay(LocalDateTime.now());
        var endTime = DateUtils.endOfDay(beginTime);
        Period period = new Period(beginTime, endTime);
        return this.parkingPayRecordService.getParkingPayTotal(period);
    }

    /**
     * 停车缴费统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/parking-pay/statistics")
    public ParkingPayTotalVO getParkingPayStatistics(StatisticsQuery query) {
        var current = this.parkingPayRecordService.getParkingPayTotal(query.getPeriod());
        var last = this.parkingPayRecordService.getParkingPayTotal(Period.getPreviousPeriod(PeriodTypeEnum.valueOf(query.getType()), query.getPeriod()));
        current.setTotalAmountQoq(MathUtils.calculateProportion(current.getTotalAmountQoq(), last.getTotalAmountQoq()));
        current.setDiscountAmountQoq(MathUtils.calculateProportion(current.getDiscountAmountQoq(), last.getDiscountAmountQoq()));
        current.setReceivableAmountQoq(MathUtils.calculateProportion(current.getReceivableAmountQoq(), last.getReceivableAmountQoq()));
        return current;
    }

    /**
     * 今日停车缴费趋势
     *
     * @return
     */
    @GetMapping("/parking-pay/today-trend")
    public List<ParkingPayTrendTotalVO> getTodayPayRecordTrend() {
        return this.parkingPayRecordService.getTodayPayRecordTrend();
    }
}
