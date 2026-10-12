package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsSafetyCheckMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsSafetyCheck;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.GoodsCheckResultEnum;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsSafetyCheckQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsSafetyCheckService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsSafetyCheckVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：食品安全检查服务层
 *
 * @author Appet
 * @date 2023-12-04 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsSafetyCheckServiceImpl extends ServiceImpl<GoodsSafetyCheckMapper, GoodsSafetyCheck> implements GoodsSafetyCheckService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsSafetyCheckParam param) {
        GoodsSafetyCheck goodsSafetyCheck = ObjectUtils.copying(param, GoodsSafetyCheck.class);
        if (StringUtils.isEmpty(param.getCheckerName())) {
            goodsSafetyCheck.setCheckerName(SecurityContextHolder.getPrincipal().getName());
        }
        this.baseMapper.insert(goodsSafetyCheck);
        return goodsSafetyCheck.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsSafetyCheckParam param) {
        GoodsSafetyCheck goodsSafetyCheck = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsSafetyCheck, "未查询到相关信息");
        ObjectUtils.copying(param, goodsSafetyCheck);
        if (StringUtils.isEmpty(param.getCheckerName())) {
            goodsSafetyCheck.setCheckerName(SecurityContextHolder.getPrincipal().getName());
        }
        this.baseMapper.updateById(goodsSafetyCheck);
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
    public GoodsSafetyCheckVO getById(Long id) {
        GoodsSafetyCheck goodsSafetyCheck = this.baseMapper.selectById(id);
        GoodsSafetyCheckVO vo = ObjectUtils.copying(goodsSafetyCheck, GoodsSafetyCheckVO.class);
        this.extend(vo);
        return vo;
    }

    @Override
    public Pagination<GoodsSafetyCheckVO> getPage(GoodsSafetyCheckQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<GoodsSafetyCheck> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getStallCode()), GoodsSafetyCheck::getStallCode, param.getStallCode())
                .like(StringUtils.isNotEmpty(param.getGoodsCategoryName()), GoodsSafetyCheck::getGoodsCategoryName, param.getGoodsCategoryName())
                .eq(StringUtils.isNotEmpty(param.getCheckResult()), GoodsSafetyCheck::getCheckResult, param.getCheckResult())
                .between(period !=
                         null, GoodsSafetyCheck::getCheckDate, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .orderByDesc(GoodsSafetyCheck::getCheckDate)
                .page(PagingUtils.convert(param));
        Pagination<GoodsSafetyCheckVO> pagination = PagingUtils.convert(page, GoodsSafetyCheckVO.class);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(v -> this.extend(v));
        }
        return pagination;
    }

    /**
     * 扩展
     *
     * @param vo
     */
    private void extend(GoodsSafetyCheckVO vo) {
        vo.setCheckResult(GoodsCheckResultEnum.getLabelByCode(vo.getCheckResult()));
    }

    @Override
    public void insertOrUpdateBatch(List<GoodsSafetyCheck> dataList) {
        this.baseMapper.insertOrUpdateBatch(dataList);
    }
}