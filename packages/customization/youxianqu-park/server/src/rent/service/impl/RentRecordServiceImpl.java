package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.dto.RentRecordAreaSumDTO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.enums.RentRoomStatusEnum;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.mapper.RentRecordMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.param.RentRecordQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.po.RentRecordPO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentRecordResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.RentedSoldStatResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.result.UtilizationRateStatResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.service.RentRecordService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 租控记录实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RentRecordServiceImpl extends ServiceImpl<RentRecordMapper, RentRecordPO> implements RentRecordService {
    private final RentRecordMapper rentRecordMapper;

    /**
     * 分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @Override
    public Pagination<RentRecordResult> getPage(RentRecordQueryParam param) {
        Page<RentRecordPO> page = rentRecordMapper.selectPage(PagingUtils.convert(param), Wrappers.<RentRecordPO>lambdaQuery()
                                                                      .eq(StringUtils.isNotBlank(param.getStatus()), RentRecordPO::getStatus, param.getStatus())
                                                                      .orderByDesc(RentRecordPO::getContractId)
                                                             );

        return PagingUtils.convert(page, RentRecordResult.class);
    }

    /**
     * 空间利用率统计
     *
     * @return 结果
     */
    @Override
    public UtilizationRateStatResult utilizationRateStat() {
        UtilizationRateStatResult result = new UtilizationRateStatResult();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal use = BigDecimal.ZERO;
        // 非闲置
        RentRecordAreaSumDTO notIdleDTO = rentRecordMapper.RentRecordNeStatusAreaStat(RentRoomStatusEnum.IDLE.getCode());
        if (notIdleDTO != null && StringUtils.isNotBlank(notIdleDTO.getRoomAreaCalculate())) {
            use = new BigDecimal(notIdleDTO.getRoomAreaCalculate());
            total = new BigDecimal(notIdleDTO.getRoomAreaCalculate());
        }

        // 闲置面积(业务其实是需要总面积。但是统计全部没必要，因为上面有个不等于闲置的查询，这里查询闲置+不等于闲置就是全部)
        RentRecordAreaSumDTO idleDTO = rentRecordMapper.RentRecordEqStatusAreaStat(RentRoomStatusEnum.IDLE.getCode());
        if (idleDTO != null && StringUtils.isNotBlank(idleDTO.getRoomAreaCalculate())) {
            total = total.add(new BigDecimal(idleDTO.getRoomAreaCalculate()));
        }

        result.setUseCalculate(use);
        result.setTotalCalculate(total);
        return result;
    }

    /**
     * 租售记录情况统计
     *
     * @return 租售记录
     */
    @Override
    public RentedSoldStatResult rentedSoldStat() {
        RentedSoldStatResult result = new RentedSoldStatResult();

        // 租
        BigDecimal rented = this.getSumAreaBigDecimalByStatus(RentRoomStatusEnum.RENTED.getCode());
        // 售
        BigDecimal sold = this.getSumAreaBigDecimalByStatus(RentRoomStatusEnum.SOLD.getCode());
        // 预定
        BigDecimal reserve = this.getSumAreaBigDecimalByStatus(RentRoomStatusEnum.RESERVE.getCode());

        // 总(非闲置)
        BigDecimal total = BigDecimal.ZERO;
        RentRecordAreaSumDTO notIdleDTO = rentRecordMapper.RentRecordNeStatusAreaStat(RentRoomStatusEnum.IDLE.getCode());
        if (notIdleDTO != null && StringUtils.isNotBlank(notIdleDTO.getRoomAreaCalculate())) {
            total = new BigDecimal(notIdleDTO.getRoomAreaCalculate());
        }

        result.setTotal(total);
        result.setSold(sold);
        result.setReserve(reserve);
        result.setRented(rented);
        return result;
    }

    private BigDecimal getSumAreaBigDecimalByStatus(String status) {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtils.isBlank(status)) {
            return result;
        }
        RentRecordAreaSumDTO dto = rentRecordMapper.RentRecordNeStatusAreaStat(status);
        if (dto != null && StringUtils.isNotBlank(dto.getRoomAreaCalculate())) {
            result = new BigDecimal(dto.getRoomAreaCalculate());
        }
        return result;
    }
}
