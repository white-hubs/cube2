package com.guhecloud.cube.srv.tj.electronic.guide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.tj.electronic.guide.dao.TjLayerManageMapper;
import com.guhecloud.cube.srv.tj.electronic.guide.entity.TjLayerManage;
import com.guhecloud.cube.srv.tj.electronic.guide.enumeration.LayerStateEnum;
import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjLayerManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.LayerManageDetailsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:36
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TjLayerManageServiceImpl extends ServiceImpl<TjLayerManageMapper, TjLayerManage> implements TjLayerManageService {
    @Override
    public LayerManageDetailsVO getLayerManageById(Long id) {
        TjLayerManage tjLayerManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(LayerManageDetailsVO.class, tjLayerManage);
    }

    @Override
    public LayerManageDetailsVO getLayerManageByState() {
        TjLayerManage tjLayerManage = this.baseMapper.selectOne(new LambdaQueryWrapper<TjLayerManage>()
                                                                        .eq(TjLayerManage::getState, LayerStateEnum.IN_USE.name())
                                                                        .last("limit 1"));
        return ConvertUtils.convert(LayerManageDetailsVO.class, tjLayerManage);
    }

    @Override
    public Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param) {
        Page<TjLayerManage> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<>());
        return new Pagination<>(page.getTotal(), page.getPages(), ConvertUtils.toList(LayerManageDetailsVO.class, page.getRecords()));
    }

    @Override
    public Boolean addLayerManage(LayerManageParam param) {
        TjLayerManage tjLayerManage = ConvertUtils.convert(TjLayerManage.class, param);
        tjLayerManage.setState(LayerStateEnum.NOT_USED.name());
        return this.baseMapper.insert(tjLayerManage) > 0;
    }

    @Override
    public Boolean updateLayerManage(LayerManageParam param) {
        TjLayerManage tjLayerManage = this.baseMapper.selectById(param.getId());
        tjLayerManage.setLayerName(param.getLayerName());
        tjLayerManage.setLayerFile(param.getLayerFile());
        tjLayerManage.setCenter(param.getCenter());
        tjLayerManage.setNortheast(param.getNortheast());
        tjLayerManage.setSouthwest(param.getSouthwest());
        return this.baseMapper.updateById(tjLayerManage) > 0;
    }

    @Override
    public Boolean updateLayerManageByState(LayerManageParam param) {
        this.baseMapper.updateLayerManageByState();
        TjLayerManage tjLayerManage = this.baseMapper.selectById(param.getId());
        tjLayerManage.setState(LayerStateEnum.IN_USE.name());
        return this.baseMapper.updateById(tjLayerManage) > 0;
    }

    @Override
    public Boolean deleteLayerManage(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
