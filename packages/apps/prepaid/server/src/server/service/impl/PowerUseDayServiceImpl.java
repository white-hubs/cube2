package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.common.constants.Constants;
import com.guhecloud.cube.srv.prepaid.common.enums.TimePeriodTypeEnum;
import com.guhecloud.cube.srv.prepaid.server.dao.PowerUseDayMapper;
import com.guhecloud.cube.srv.prepaid.server.param.PowerUseDayPageParam;
import com.guhecloud.cube.srv.prepaid.server.param.UseDayStatParam;
import com.guhecloud.cube.srv.prepaid.server.po.PowerUseDayPO;
import com.guhecloud.cube.srv.prepaid.server.result.PowerUseDayPageResult;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import com.guhecloud.cube.srv.prepaid.server.service.PowerUseDayService;
import com.guhecloud.cube.srv.prepaid.server.utils.ServiceUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日用电服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerUseDayServiceImpl extends ServiceImpl<PowerUseDayMapper, PowerUseDayPO> implements PowerUseDayService {

    private final PowerUseDayMapper powerUseDayMapper;

    /**
     * 日用电分页查询
     *
     * @param param 条件入参
     *
     * @return 分页
     */
    @Override
    public Pagination<PowerUseDayPageResult> getPowerUserDayPage(PowerUseDayPageParam param) {
        String startDate = null;
        String endDate = null;
        if (param.getPeriod() != null && param.getPeriod().getBegin() != null && param.getPeriod().getEnd() != null) {
            startDate = DateUtils.format(param.getPeriod().getBegin());
            endDate = DateUtils.format(param.getPeriod().getEnd());
        }
        Page<PowerUseDayPO> page = this.page(PagingUtils.convert(param), Wrappers.<PowerUseDayPO>lambdaQuery()
                                                     .eq(StringUtils.isNotBlank(param.getMeterId()), PowerUseDayPO::getMeterId, param.getMeterId())
                                                     .ge(startDate != null, PowerUseDayPO::getUseDate, startDate)
                                                     .le(endDate != null, PowerUseDayPO::getUseDate, endDate)

                                            );
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(po -> {
            PowerUseDayPageResult powerUseDayPageResult = new PowerUseDayPageResult();
            BeanUtils.copyProperties(po, powerUseDayPageResult);
            return powerUseDayPageResult;
        }).collect(Collectors.toList()));
    }

    /**
     * 获取电表日用能统计
     *
     * @param param 条件
     *
     * @return 列表
     */
    @Override
    public List<UseDayStatResult> getPowerUseDayStat(UseDayStatParam param) {
        if (param == null || param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            throw new ServiceException(Constants.PARAM_NULL_PROMPT);
        }
        List<UseDayStatResult> list;
        String name = TimePeriodTypeEnum.YEAR.name;
        if (name.equals(param.getType())) {
            list = powerUseDayMapper.getPowerUseMonthStat(Wrappers.<PowerUseDayPO>lambdaQuery()
                                                                  .between(PowerUseDayPO::getUseDate, param.getPeriod().getBegin(), param
                                                                          .getPeriod().getEnd())
                                                                  .groupBy(PowerUseDayPO::getUseMonth)
                                                                  .orderByAsc(PowerUseDayPO::getUseDate)
                                                         );
        } else {
            list = powerUseDayMapper.getPowerUseDayStat(Wrappers.<PowerUseDayPO>lambdaQuery()
                                                                .between(PowerUseDayPO::getUseDate, param.getPeriod().getBegin(), param
                                                                        .getPeriod().getEnd())
                                                                .groupBy(PowerUseDayPO::getUseDate)
                                                                .orderByAsc(PowerUseDayPO::getUseDate)
                                                       );
        }

        // 填充每日数据
        return ServiceUtils.setEveryDate(list, param.getPeriod(), param.getType());
    }
}
