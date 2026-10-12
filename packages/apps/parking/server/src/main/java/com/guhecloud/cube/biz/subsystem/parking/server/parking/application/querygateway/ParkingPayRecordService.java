package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.CarSafeFeeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingPayRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingPayRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayTrendTotalVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingPayRecordService extends ServiceImpl<ParkingPayRecordMapper, ParkingPayRecordPO> {
    private final ParkingPayRecordMapper parkingPayRecordMapper;

    public Page<ParkingPayRecordPageVO> getPage(ParkingPayRecordQueryParam param) {
        Page<ParkingPayRecordPageVO> page = parkingPayRecordMapper.getPage(PagingUtils.convert(param), param, param.getPeriod());
        if (!Objects.isNull(page) && !CollectionUtils.isEmpty(page.getRecords())) {
            fillAccessInInfo(page.getRecords());
            page.getRecords().forEach(this::convertPayType);
        }
        return page;
    }

    private void fillAccessInInfo(List<ParkingPayRecordPageVO> records) {
        List<String> identifyIds = records.stream()
                .map(ParkingPayRecordPageVO::getIdentifyId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(identifyIds)) {
            return;
        }
        Map<String, ParkingPayRecordPageVO> accessInfoMap = parkingPayRecordMapper.listAccessInByIdentifyIds(identifyIds).stream()
                .collect(Collectors.toMap(ParkingPayRecordPageVO::getIdentifyId, Function.identity(), (left, right) -> left));
        records.forEach(record -> {
            ParkingPayRecordPageVO accessInfo = accessInfoMap.get(record.getIdentifyId());
            if (Objects.isNull(accessInfo)) {
                return;
            }
            record.setAccessInTime(accessInfo.getAccessInTime());
            record.setParkingInfoCode(accessInfo.getParkingInfoCode());
            record.setParkingInfoName(accessInfo.getParkingInfoName());
        });
    }

    private void convertPayType(ParkingPayRecordPageVO vo) {
        if (Objects.isNull(vo.getPayType())) {
            return;
        }
        switch (vo.getPayType()) {
            case "0":
                vo.setPayType(CarSafeFeeEnum.CASH_PAY.getName());
                break;
            case "1":
                vo.setPayType(CarSafeFeeEnum.ALI_PAY.getName());
                break;
            case "2":
                vo.setPayType(CarSafeFeeEnum.WE_CHAT_PAY.getName());
                break;
            case "3":
                vo.setPayType(CarSafeFeeEnum.UNION_PAY.getName());
                break;
            default:
                vo.setPayType(CarSafeFeeEnum.OTHER_PAY.getName());
                break;
        }
    }

    public ParkingPayTotalVO getParkingPayTotal(@NonNull Period period) {
        return this.baseMapper.selectParkingPayTotal(period);
    }

    public List<ParkingPayTrendTotalVO> getTodayPayRecordTrend() {
        var beginTime = DateUtils.beginOfDay(LocalDateTime.now());
        var endTime = DateUtils.endOfDay(beginTime);
        List<ParkingPayRecordPO> parkingPayRecordPOS = this.lambdaQuery()
                .ge(ParkingPayRecordPO::getPayTime, beginTime)
                .lt(ParkingPayRecordPO::getPayTime, endTime)
                .list();
        List<ParkingPayTrendTotalVO> parkingPayTrendTotalVOS = new ArrayList<>();
        for (var start = beginTime; start.isBefore(endTime); start = DateUtils.offset(start, ChronoUnit.HOURS, 1)) {
            String date = DateUtils.format(start, "yyyy-MM-dd HH");
            BigDecimal amount = parkingPayRecordPOS.stream()
                    .filter(parkingAccessRecord -> date.equals(DateUtils.format(parkingAccessRecord.getPayTime(), "yyyy-MM-dd HH")))
                    .map(ParkingPayRecordPO::getFreeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            parkingPayTrendTotalVOS.add(new ParkingPayTrendTotalVO(amount, DateUtils.format(start, "HH") + "点"));
        }
        return parkingPayTrendTotalVOS;
    }
}
