package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.ObjTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultCaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultTrendVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultOrder;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultCaseQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultCaseCollectionQuery;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lzy
 * @date 2022/5/6
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultCaseQueryServiceImpl extends ServiceImpl<FaultCaseMapper, FaultCase> implements FaultCaseQueryService {

    private final FaultCaseMapper faultCaseMapper;
    private final FaultOrderService faultOrderService;

    @Override
    public Page<FaultCaseVO> pageFaultCase(@NonNull FaultCaseCollectionQuery query) {
        return faultCaseMapper.pageFaultCase(PagingUtils.convert(query), query);
    }

    @Override
    public List<FaultCase> listFaultCase(FaultCaseCollectionQuery query) {
        return this.baseMapper.selectList(Wrappers.<FaultCase>lambdaQuery()
                                                  .like(StringUtils.isNotEmpty(query.getKeyword()), FaultCase::getDeviceTypeName, query.getKeyword()));
    }

    @Override
    public Map<String, Object> getFaultTrend(Long id) {
        HashMap<String, Object> remap = new HashMap<>();
        Long count = faultOrderService.lambdaQuery().eq(id != null, FaultOrder::getObjId, id).count();
        var endTime = DateUtils.endOfMonth(LocalDateTime.now());
        var beginTime = DateUtils.beginOfMonth(LocalDateTime.now());
        List<FaultTrendVo> trendVos = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            Period period = new Period(DateUtils.offsetMonth(beginTime, -i), DateUtils.offsetMonth(endTime, -i));
            Long monthCount = faultOrderService.lambdaQuery()
                    .eq(FaultOrder::getObjType, ObjTypeEnum.DEVICE.name())
                    .eq(id != null, FaultOrder::getObjId, id)
                    .between(FaultOrder::getSubmitTime, period.getBegin(), period.getEnd()).count();
            trendVos.add(FaultTrendVo.builder().num(monthCount).timeFlag(DateUtils.format(period.getBegin(), "yyyy-MM")).build());
        }
        //总数
        remap.put("total", count);
        //趋势
        remap.put("months", trendVos);
        return remap;
    }

    @Override
    public Long countCase() {
        return this.lambdaQuery().select(FaultCase::getId).count();
    }
}
