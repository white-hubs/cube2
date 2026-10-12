package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.enums.TimePeriodTypeEnum;
import com.guhecloud.cube.srv.prepaid.common.vo.WaterUseDayDTO;
import com.guhecloud.cube.srv.prepaid.server.dao.WaterUseDayMapper;
import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.param.WaterUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.po.WaterUseDayPO;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.cube.srv.prepaid.server.result.WaterUseDayPageResult;
import com.guhecloud.cube.srv.prepaid.server.service.WaterUseDayService;
import com.guhecloud.cube.srv.prepaid.server.third.client.WaterUseDayClient;
import com.guhecloud.cube.srv.prepaid.server.utils.PageResponse;
import com.guhecloud.cube.srv.prepaid.server.utils.ServiceUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日用水服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterUseDayServiceImpl extends ServiceImpl<WaterUseDayMapper, WaterUseDayPO> implements WaterUseDayService {

    private final WaterUseDayMapper waterUseDayMapper;
    private final WaterUseDayClient waterUseDayClient;

    /**
     * 日用水分页列表
     *
     * @param param 条件入参对象
     *
     * @return 分页列表
     */
    @Override
    public Pagination<WaterUseDayPageResult> getWaterUserDayPage(WaterUseDayPageParam param) {
        String startDate = null;
        String endDate = null;
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            startDate = DateUtils.formatDate(param.getPeriod().getBegin());
            endDate = DateUtils.formatDate(param.getPeriod().getEnd());

        }
        List<WaterUseDayDTO> dtoList = waterUseDayClient.getWaterUseDayList(param.getMeterId(), startDate, endDate);
        // 排序
        dtoList.sort(Comparator.comparing(WaterUseDayDTO::getUseDate).reversed());
        // 分页
        Pagination<WaterUseDayDTO> pagination = PageResponse.doPage(dtoList, param.getPageNumber(), param.getPageSize());
        // 转换输出
        return new Pagination<>(pagination.getTotal(), pagination.getPages(), pagination.getRecords().stream().map(po -> {
            WaterUseDayPageResult waterUseDayPageResult = new WaterUseDayPageResult();
            BeanUtils.copyProperties(po, waterUseDayPageResult);
            return waterUseDayPageResult;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取水表用能统计
     *
     * @param param 条件
     *
     * @return 用能统计集合
     */
    @Override
    public List<UseDayStatResult> getWaterUseDayStat(UseDayStatParam param) {
        if (param == null || param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            throw new ServiceException(Constants.PARAM_NULL_PROMPT);
        }
        List<UseDayStatResult> list;
        if (TimePeriodTypeEnum.YEAR.name.equals(param.getType())) {
            list = waterUseDayMapper.getWaterUseDayStat(Wrappers.<WaterUseDayPO>lambdaQuery()
                                                                .between(WaterUseDayPO::getUseDate, param.getPeriod().getBegin(), param
                                                                        .getPeriod().getEnd())
                                                                .groupBy(WaterUseDayPO::getUseMonth)
                                                                .orderByAsc(WaterUseDayPO::getUseDate)
                                                       );
        } else {
            list = waterUseDayMapper.getWaterUseDayStat(Wrappers.<WaterUseDayPO>lambdaQuery()
                                                                .between(WaterUseDayPO::getUseDate, param.getPeriod().getBegin(), param
                                                                        .getPeriod().getEnd())
                                                                .groupBy(WaterUseDayPO::getUseDate)
                                                                .orderByAsc(WaterUseDayPO::getUseDate)
                                                       );
        }
        // 填充数据
        return ServiceUtils.setEveryDate(list, param.getPeriod(), param.getType());
    }
}
