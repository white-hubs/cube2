package com.guhecloud.cube.srv.prepaid.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.prepaid.server.dao.WaterRechargeMapper;
import com.guhecloud.cube.srv.prepaid.server.param.WaterRechargePageParam;
import com.guhecloud.cube.srv.prepaid.server.po.WaterRechargePO;
import com.guhecloud.cube.srv.prepaid.server.result.WaterRechargePageResult;
import com.guhecloud.cube.srv.prepaid.server.service.WaterRechargeService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 水表充值记录服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterRechargeServiceImpl extends ServiceImpl<WaterRechargeMapper, WaterRechargePO> implements WaterRechargeService {

    /**
     * 水表充值记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @Override
    public Pagination<WaterRechargePageResult> getWaterRechargePage(WaterRechargePageParam param) {
        Page<WaterRechargePO> page = this.page(PagingUtils.convert(param), Wrappers.<WaterRechargePO>lambdaQuery()
                                                       .eq(WaterRechargePO::getMeterId, param.getMeterId())
                                                       .orderByDesc(WaterRechargePO::getRechargeTime)
                                              );
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(po -> {
            WaterRechargePageResult waterRechargePageResult = new WaterRechargePageResult();
            BeanUtils.copyProperties(po, waterRechargePageResult);
            return waterRechargePageResult;
        }).collect(Collectors.toList()));
    }
}
