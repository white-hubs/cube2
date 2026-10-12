package com.guhecloud.cube.srv.gen.supplier.server.supplier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.dao.GenSupplierMapper;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.entity.GenSupplier;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierModifyParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierQueryParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.UpdateDisableParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.rocketmq.message.GenSupplierMessage;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.service.GenSupplierService;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.vo.GenSupplierVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenSupplierServiceImpl extends ServiceImpl<GenSupplierMapper, GenSupplier> implements GenSupplierService {

    private final GenSupplierMapper genSupplierMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addSupplier(@NonNull GenSupplierModifyParam param) {
        GenSupplier genSupplier = genSupplierMapper.selectOne(Wrappers.<GenSupplier>lambdaQuery()
                                                                      .eq(GenSupplier::getCode, param.getCode()));
        Assert.isConflict(Objects.nonNull(genSupplier), "批发商编码重复：" + param.getCode());
        // 新增批发商基础信息
        GenSupplier supplier = ObjectUtils.copying(param, GenSupplier.class);
        if (CollectionUtils.isNotEmpty(param.getAreaIds())) {
            supplier.setArea(String.join("-", param.getAreaIds()));
        }
        supplier.setDisable(false);
        supplier.setTenant(SessionContextHolder.getRequester().getTenant());
        this.baseMapper.insert(supplier);
        return supplier.getId();
    }

    @Override
    public void importSupplierTemplate(List<GenSupplierModifyParam> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            for (GenSupplierModifyParam param : params) {
                GenSupplier genSupplier = genSupplierMapper.selectOne(Wrappers.<GenSupplier>lambdaQuery()
                                                                              .eq(GenSupplier::getCode, param.getCode()));
                Assert.isConflict(Objects.nonNull(genSupplier), "批发商编码重复：" + param.getCode());
                GenSupplier supplier = ObjectUtils.copying(param, GenSupplier.class);
                supplier.setDisable(false);
                supplier.setTenant(SessionContextHolder.getRequester().getTenant());
                this.baseMapper.insert(supplier);

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupplier(@NonNull Long id, @NonNull GenSupplierModifyParam param) {
        GenSupplier supplier = this.baseMapper.selectById(id);
        Assert.isNotFound(supplier, "不存在该批发商资料信息!");

        // 更新批发商信息
        if (!CollectionUtils.isEmpty(param.getAreaIds())) {
            supplier.setArea(String.join("-", param.getAreaIds()));
        }

        ObjectUtils.copying(param, supplier);
        supplier.setTenant(SessionContextHolder.getRequester().getTenant());
        this.baseMapper.updateById(supplier);

        //数据同步
        GenSupplierMessage copying = ObjectUtils.copying(supplier, GenSupplierMessage.class);
        RocketMQContextHolder.send(GenSupplierMessage.TOPIC, copying);
    }

    @Override
    public Pagination<GenSupplierVO> pageSupplierDetail(GenSupplierQueryParam param) {
        Page<GenSupplier> page = this.page(PagingUtils.convert(param), Wrappers.<GenSupplier>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), GenSupplier::getName, param.getKeyword())
                .or()
                .like(StringUtils.isNotEmpty(param.getKeyword()), GenSupplier::getShiftMaster, param.getKeyword())
                .or()
                .like(StringUtils.isNotEmpty(param.getKeyword()), GenSupplier::getCode, param.getKeyword())
                .or()
                .like(StringUtils.isNotEmpty(param.getKeyword()), GenSupplier::getShiftMasterPhone, param.getKeyword()));
        return PagingUtils.convert(page, GenSupplierVO.class);
    }

    @Override
    public void updateDisable(List<UpdateDisableParam> updateDisableParams) {
        genSupplierMapper.updateDisable(updateDisableParams);
    }

}
