package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRecordLogMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecordLog;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRecordLogService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：采价预警-价格修改日志服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRecordLogServiceImpl extends ServiceImpl<GoodsPriceWarnRecordLogMapper, GoodsPriceWarnRecordLog> implements GoodsPriceWarnRecordLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnRecordLogParam param) {
        GoodsPriceWarnRecordLog goodsPriceWarnRecordLog = ObjectUtils.copying(param, GoodsPriceWarnRecordLog.class);
        this.baseMapper.insert(goodsPriceWarnRecordLog);
        return goodsPriceWarnRecordLog.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPriceWarnRecordLogParam param) {
        GoodsPriceWarnRecordLog goodsPriceWarnRecordLog = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRecordLog, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPriceWarnRecordLog);
        this.baseMapper.updateById(goodsPriceWarnRecordLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public GoodsPriceWarnRecordLogVO getById(Long id) {
        GoodsPriceWarnRecordLog goodsPriceWarnRecordLog = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPriceWarnRecordLog, GoodsPriceWarnRecordLogVO.class);
    }

    @Override
    public Pagination<GoodsPriceWarnRecordLogVO> getPage(GoodsPriceWarnRecordLogQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPriceWarnRecordLog> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPriceWarnRecordLogVO.class);
    }
}