package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.common.vo.PowerRechargeDTO;
import com.guhecloud.cube.srv.prepaid.server.dao.PowerRechargeMapper;
import com.guhecloud.cube.srv.prepaid.server.param.PowerRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.po.PowerRechargePO;
import com.guhecloud.cube.srv.prepaid.server.result.PowerRechargeResult;
import com.guhecloud.cube.srv.prepaid.server.service.PowerRechargeService;
import com.guhecloud.cube.srv.prepaid.server.third.client.PowerRechargeClient;
import com.guhecloud.cube.srv.prepaid.server.utils.PageResponse;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 电表充值记录服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerRechargeServiceImpl extends ServiceImpl<PowerRechargeMapper, PowerRechargePO> implements PowerRechargeService {

    private final PowerRechargeClient powerRechargeClient;

    /**
     * 电表充值记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页对象
     */
    @Override
    public Pagination<PowerRechargeResult> getPowerRechargePage(PowerRechargePageParam param) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        // 时间默认为最近一年
        if (param.getPeriod() != null) {
            startDate = param.getPeriod().getBegin();
            endDate = param.getPeriod().getEnd();
        } else {
            startDate = DateUtils.offsetMonth(LocalDateTime.now(), -12);
            endDate = LocalDateTime.now();
        }
        // 获取第三方数据
        List<PowerRechargeDTO> powerRechargeList = powerRechargeClient.getPowerRechargeList(param.getMeterId(), startDate, endDate);
        // 内存分页
        Pagination<PowerRechargeDTO> doPage = PageResponse.doPage(powerRechargeList, param.getPageNumber(), param.getPageSize());
        // 组装返回对象
        return new Pagination<>(doPage.getTotal(), doPage.getPages(), doPage.getRecords().stream().map(po -> {
            PowerRechargeResult powerRechargeResult = new PowerRechargeResult();
            BeanUtils.copyProperties(po, powerRechargeResult);
            powerRechargeResult.setRechargeTime(DateUtils.parse(po.getCreateTime(), "yyyy-MM-dd'T'HH:mm:ss"));
            return powerRechargeResult;
        }).collect(Collectors.toList()));
    }
}
