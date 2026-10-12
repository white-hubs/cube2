package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolLineMapper;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordMapper;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsPageQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolLineStatisticsService;
import com.guhecloud.cube.biz.vmonitor.server.util.DateUnitUtils;
import com.guhecloud.cube.biz.vmonitor.server.util.MathUtils;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolCurrencyStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolGrowthRateStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineAbnormalStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineStatisticsPageVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolLineStatisticsServiceImpl implements CameraPatrolLineStatisticsService {

    private final CameraPatrolLineMapper cameraPatrolLineMapper;

    private final CameraPatrolRecordMapper cameraPatrolRecordMapper;

    @Override
    public Page<CameraPatrolLineStatisticsPageVO> getPatrolLineStatisticsPage(CameraPatrolLineStatisticsPageQueryParam param) {
        return this.cameraPatrolLineMapper.selectPatrolLineStatisticsPage(PagingUtils.convert(param), param);
    }

    @Override
    public List<CameraPatrolLineStatisticsPageVO> getPatrolLineStatisticsList(CameraPatrolLineStatisticsPageQueryParam param) {
        return this.cameraPatrolLineMapper.selectPatrolLineStatisticsList(param);
    }

    @Override
    public CameraPatrolGrowthRateStatisticsVO getPatrolAbnormalRatioStatistics(CameraPatrolLineStatisticsQueryParam param) {
        //上期日期
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        //本期数量
        Integer currentAbnormalNum = this.cameraPatrolRecordMapper.selectPatrolAbnormalNum(param);
        param.setPeriod(previousPeriod);
        Integer previousAbnormalNum = this.cameraPatrolRecordMapper.selectPatrolAbnormalNum(param);
        double ringRatio = MathUtils.calculateMonthOnMonthRatio(currentAbnormalNum.longValue(), previousAbnormalNum.longValue());
        DecimalFormat b = new DecimalFormat("0.00");
        String format = b.format(ringRatio);
        return CameraPatrolGrowthRateStatisticsVO.builder()
                .totalNum(currentAbnormalNum)
                .ringRatio(format)
                .build();
    }

    @Override
    public CameraPatrolGrowthRateStatisticsVO getPatrolRecordRatioStatistics(CameraPatrolLineStatisticsQueryParam param) {
        //本期数量
        Integer currentAbnormalNum = this.cameraPatrolRecordMapper.selectPatrolRecordNum(param);
        //上期数量
        Period previousPeriod = DateUnitUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Integer previousAbnormalNum = this.cameraPatrolRecordMapper.selectPatrolRecordNum(param);
        double ringRatio = MathUtils.calculateMonthOnMonthRatio(currentAbnormalNum.longValue(), previousAbnormalNum.longValue());
        DecimalFormat b = new DecimalFormat("0.00");
        String format = b.format(ringRatio);
        return CameraPatrolGrowthRateStatisticsVO.builder()
                .totalNum(currentAbnormalNum)
                .ringRatio(format)
                .build();
    }

    @Override
    public List<CameraPatrolCurrencyStatisticsVO> getPatrolLineAbnormalInnerGroupStatistics(CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolLineMapper.getCameraPatrolLineAbnormalInnerStatistics(param);
    }

    @Override
    public CameraPatrolLineAbnormalStatisticsVO getCameraPatrolLineAbnormalStatistics(CameraPatrolLineStatisticsQueryParam param) {
        List<CameraPatrolCurrencyStatisticsVO> cameraPatrolCurrencyStatisticsVOS = this.cameraPatrolLineMapper
                .getCameraPatrolLineAbnormalStatistics(param);
        if (CollectionUtils.isEmpty(cameraPatrolCurrencyStatisticsVOS)) {
            return CameraPatrolLineAbnormalStatisticsVO.builder()
                    .cameraPatrolLineAbnormalStatisticsVOS(Collections.emptyList())
                    .build();
        }
        List<CameraPatrolCurrencyStatisticsVO> abnormaCameraPatrolCurrencyStatisticsVOS = cameraPatrolCurrencyStatisticsVOS.stream()
                .filter(cameraPatrolCurrencyStatisticsVO -> cameraPatrolCurrencyStatisticsVO.getTotalNum() > 0)
                .collect(Collectors.toList());
        Optional<CameraPatrolCurrencyStatisticsVO> opCameraPatrolCurrencyStatisticsVO = abnormaCameraPatrolCurrencyStatisticsVOS.stream()
                .max(Comparator.comparing(CameraPatrolCurrencyStatisticsVO::getTotalNum));

        CameraPatrolLineAbnormalStatisticsVO cameraPatrolLineAbnormalStatisticsVO = CameraPatrolLineAbnormalStatisticsVO.builder()
                .countNum(cameraPatrolCurrencyStatisticsVOS.size())
                .cameraPatrolLineAbnormalStatisticsVOS(abnormaCameraPatrolCurrencyStatisticsVOS)
                .build();
        opCameraPatrolCurrencyStatisticsVO.ifPresent(cameraPatrolCurrencyStatisticsVO -> {
            //大于0时,才给出异常情况最多的一条线路
            if (cameraPatrolCurrencyStatisticsVO.getTotalNum() > 0) {
                cameraPatrolLineAbnormalStatisticsVO.setMostAbnormalPatrolName(cameraPatrolCurrencyStatisticsVO.getName());
                cameraPatrolLineAbnormalStatisticsVO.setMostAbnormalPatrolNum(cameraPatrolCurrencyStatisticsVO.getTotalNum());
            }
        });
        return cameraPatrolLineAbnormalStatisticsVO;
    }
}
