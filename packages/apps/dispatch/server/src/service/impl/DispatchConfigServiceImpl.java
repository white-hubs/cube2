package com.guhecloud.cube.biz.dispatch.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.dao.DispatchConfigMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchConfig;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchConfigQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchConfigVO;
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
 * 描述：调度台配置服务层
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchConfigServiceImpl extends ServiceImpl<DispatchConfigMapper, DispatchConfig> implements DispatchConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(DispatchConfigParam param) {
        DispatchConfig dispatchConfig = ObjectUtils.copying(param, DispatchConfig.class);
        this.baseMapper.insert(dispatchConfig);
        return dispatchConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, DispatchConfigParam param) {
        DispatchConfig dispatchConfig = this.baseMapper.selectById(id);
        Assert.isNotFound(dispatchConfig == null, "未查询到相关信息");
        ObjectUtils.copying(param, dispatchConfig);
        this.baseMapper.updateById(dispatchConfig);
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
    public DispatchConfigVO getById(Long id) {
        DispatchConfig dispatchConfig = this.baseMapper.selectById(id);
        return ObjectUtils.copying(dispatchConfig, DispatchConfigVO.class);
    }

    @Override
    public Pagination<DispatchConfigVO> getPage(DispatchConfigQueryParam param) {
        //请自行添加查询条件
        Page<DispatchConfig> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, DispatchConfigVO.class);
    }
}