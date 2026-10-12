package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingTaskTransferMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskTransfer;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskTransferParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskTransferQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPricingTaskTransferService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskTransferVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：采价任务服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPricingTaskTransferServiceImpl extends ServiceImpl<GoodsPricingTaskTransferMapper, GoodsPricingTaskTransfer> implements GoodsPricingTaskTransferService {
    private final GoodsPricingTaskTransferMapper goodsPricingTaskTransferMapper;

    @Override
    public Long add(GoodsPricingTaskTransferParam param) {
        GoodsPricingTaskTransfer taskTransfer = ObjectUtils.copying(param, GoodsPricingTaskTransfer.class);
        this.goodsPricingTaskTransferMapper.insert(taskTransfer);
        return taskTransfer.getId();
    }

    @Override
    public GoodsPricingTaskTransferVO getById(Long id) {
        GoodsPricingTaskTransfer taskTransfer = this.goodsPricingTaskTransferMapper.selectById(id);
        return ObjectUtils.copying(taskTransfer, GoodsPricingTaskTransferVO.class);
    }

    @Override
    public Pagination<GoodsPricingTaskTransferVO> getPage(GoodsPricingTaskTransferQueryParam param) {
        Page<GoodsPricingTaskTransfer> page = this.page(PagingUtils.convert(param), Wrappers.<GoodsPricingTaskTransfer>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getToUserNames()), GoodsPricingTaskTransfer::getFromUserName, param.getFromUserName())
                .like(StringUtils.isNotBlank(param.getToUserNames()), GoodsPricingTaskTransfer::getToUserNames, param.getToUserNames())
                .orderByDesc(GoodsPricingTaskTransfer::getCreateTime));
        return PagingUtils.convert(page, GoodsPricingTaskTransferVO.class);
    }

    @Override
    public List<GoodsPricingTaskTransferVO> getList(GoodsPricingTaskTransferQueryParam param) {
        List<GoodsPricingTaskTransfer> list = this.list(Wrappers.<GoodsPricingTaskTransfer>lambdaQuery()
                                                                .like(StringUtils.isNotBlank(param.getToUserNames()), GoodsPricingTaskTransfer::getFromUserName, param.getFromUserName())
                                                                .like(StringUtils.isNotBlank(param.getToUserNames()), GoodsPricingTaskTransfer::getToUserNames, param.getToUserNames())
                                                                .orderByDesc(GoodsPricingTaskTransfer::getCreateTime));
        return ObjectUtils.copying(list, GoodsPricingTaskTransferVO.class);
    }
}