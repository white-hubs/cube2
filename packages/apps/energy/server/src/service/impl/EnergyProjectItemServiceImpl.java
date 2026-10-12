package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectItemQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectItemService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ProjectItemVo;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：分项及区域仪表绑定服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyProjectItemServiceImpl extends ServiceImpl<EnergyProjectItemMapper, EnergyProjectItem> implements EnergyProjectItemService {

    private final EnergyProjectItemMapper energyProjectItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnergyProjectItemParam param) {
        EnergyProjectItem energyProjectItem = ObjectUtils.copying(param, EnergyProjectItem.class);
        this.baseMapper.insert(energyProjectItem);
        return energyProjectItem.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EnergyProjectItemParam param) {
        EnergyProjectItem energyProjectItem = this.baseMapper.selectById(id);
        Assert.isNotFound(energyProjectItem, "未查询到相关信息");
        ObjectUtils.copying(param, energyProjectItem);
        this.baseMapper.updateById(energyProjectItem);
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
    public EnergyProjectItemVO getById(Long id) {
        EnergyProjectItem energyProjectItem = this.baseMapper.selectById(id);
        return ObjectUtils.copying(energyProjectItem, EnergyProjectItemVO.class);
    }

    @Override
    public Pagination<EnergyProjectItemVO> getPage(EnergyProjectItemQueryParam param) {
        //请自行添加查询条件
        Page<EnergyProjectItem> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EnergyProjectItemVO.class);
    }

    @Override
    public List<ProjectItemVo> getProjectItems() {

        return energyProjectItemMapper.getProjectItems();
    }

    @Override
    public List<EnergyProjectItem> getEnergyProjectItem(String bindType, String meterTable) {
        if ("energy_project".equals(meterTable)) {
            return energyProjectItemMapper.getEnergyProjectItem(bindType);
        }
        return new ArrayList<EnergyProjectItem>();
    }
}