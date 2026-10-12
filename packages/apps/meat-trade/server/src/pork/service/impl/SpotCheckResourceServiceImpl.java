package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.SpotCheckResourceMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckResourceQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckResourceService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourcePageVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service("meattrade-spotCheckResourceService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotCheckResourceServiceImpl extends ServiceImpl<SpotCheckResourceMapper, SpotCheckResource> implements SpotCheckResourceService {
    private final SpotCheckResourceMapper spotCheckResourceMapper;

    @Override
    public SpotCheckResource getById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<SpotCheckResource> listByOrder(@NonNull Serializable orderId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<SpotCheckResource>lambdaQuery()
                                                  .eq(SpotCheckResource::getOrderId, orderId)
                                                  .eq(SpotCheckResource::getTenant, tenant));
    }

    @Override
    public Page<SpotCheckResourcePageVO> pageSCR(SpotCheckResourceQueryParam queryParam) {
        return spotCheckResourceMapper.pageSCR(PagingUtils.convert(queryParam), Wrappers.query()
                .eq("r.tenant", queryParam.getTenant())
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like("r.pork_report_code", queryParam.getKeyword())
                        .or()
                        .like("d.label", queryParam.getKeyword())
                        .or()
                        .like("d.foot_mark_code", queryParam.getKeyword())
                        .or()
                        .in(!CollectionUtils.isEmpty(queryParam.getSupplierIds()), "r.supplier_id", queryParam.getSupplierIds())
                    ).orderByDesc("r.create_time"));
    }
}
