package com.guhecloud.cube.srv.electron.guide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.electron.guide.dao.LayerManageMapper;
import com.guhecloud.cube.srv.electron.guide.entity.LayerManage;
import com.guhecloud.cube.srv.electron.guide.enumeration.LayerStateEnum;
import com.guhecloud.cube.srv.electron.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideAreaManageService;
import com.guhecloud.cube.srv.electron.guide.service.LayerManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.LayerManageDetailsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:36
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LayerManageServiceImpl extends ServiceImpl<LayerManageMapper, LayerManage> implements LayerManageService {

    private final GuideAreaManageService guideAreaManageService;

    @Override
    public LayerManageDetailsVO getLayerManageById(Long id) {
        LayerManage layerManage = this.baseMapper.selectById(id);
        if (Objects.isNull(layerManage)) {
            return null;
        }
        return buildDetailsVO(layerManage);
    }

    private LayerManageDetailsVO buildDetailsVO(LayerManage layerManage) {
        LayerManageDetailsVO layerManageDetailsVO = ConvertUtils.convert(LayerManageDetailsVO.class, layerManage);
        if (Objects.nonNull(layerManage.getAreaId())) {
            GuideAreaDetailsVO guideAreaDetailsVO = this.guideAreaManageService.getGuideAreaById(layerManage.getAreaId());
            layerManageDetailsVO.setArea(guideAreaDetailsVO);
        }
        return layerManageDetailsVO;
    }

    private List<LayerManageDetailsVO> buildDetailsVO(List<LayerManage> layerManages) {
        return layerManages.stream().map(this::buildDetailsVO).collect(Collectors.toList());
    }

    @Override
    public LayerManageDetailsVO getLayerManageByState() {
        LayerManage layerManage = this.baseMapper.selectOne(new LambdaQueryWrapper<LayerManage>()
                                                                    .eq(LayerManage::getState, LayerStateEnum.IN_USE.name())
                                                                    .last("limit 1"));
        if (Objects.isNull(layerManage)) {
            return null;
        }
        return buildDetailsVO(layerManage);
    }

    @Override
    public LayerManageDetailsVO getLayerManageByAreaId(@NonNull Long areaId) {
        LayerManage layerManage = this.baseMapper.selectOne(new LambdaQueryWrapper<LayerManage>()
                                                                    .eq(LayerManage::getAreaId, areaId)
                                                                    .eq(LayerManage::getState, LayerStateEnum.IN_USE.name())
                                                                    .last("limit 1"));
        if (Objects.isNull(layerManage)) {
            return null;
        }
        return buildDetailsVO(layerManage);
    }

    @Override
    public Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param) {
        Page<LayerManage> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, Wrappers.<LayerManage>lambdaQuery()
                                                  .like(StringUtils.isNotBlank(param.getAreaId()), LayerManage::getAreaSequence, param.getAreaId())
                                         );
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return new Pagination<>(page.getTotal(), page.getPages(), buildDetailsVO(page.getRecords()));
    }

    @Override
    public Boolean addLayerManage(LayerManageParam param) {
        GuideAreaDetailsVO area = this.guideAreaManageService.getGuideAreaById(param.getAreaId());
        LayerManage layerManage = ConvertUtils.convert(LayerManage.class, param);
        layerManage.setState(LayerStateEnum.NOT_USED.name());
        if (Objects.nonNull(area)) {
            layerManage.setAreaSequence(area.getSequence());
        }
        return this.baseMapper.insert(layerManage) > 0;
    }

    @Override
    public Boolean updateLayerManage(LayerManageParam param) {
        GuideAreaDetailsVO area = this.guideAreaManageService.getGuideAreaById(param.getAreaId());
        LayerManage layerManage = this.baseMapper.selectById(param.getId());
        layerManage.setLayerName(param.getLayerName());
        layerManage.setLayerFile(param.getLayerFile());
        layerManage.setCenter(param.getCenter());
        layerManage.setNortheast(param.getNortheast());
        layerManage.setSouthwest(param.getSouthwest());
        layerManage.setAreaId(param.getAreaId());
        if (Objects.nonNull(area)) {
            layerManage.setAreaSequence(area.getSequence());
        }
        return this.baseMapper.updateById(layerManage) > 0;
    }

    @Override
    public Boolean updateLayerManageByState(LayerManageParam param) {
        LayerManage layerManage = this.baseMapper.selectById(param.getId());
        layerManage.setState(LayerStateEnum.IN_USE.name());
        this.updateLayerManageByAreaId(layerManage.getAreaId());
        return this.baseMapper.updateById(layerManage) > 0;
    }

    public void updateLayerManageByAreaId(Long areaId) {
        this.update(Wrappers.<LayerManage>lambdaUpdate()
                            .set(LayerManage::getState, LayerStateEnum.NOT_USED.name())
                            .eq(LayerManage::getState, LayerStateEnum.IN_USE.name())
                            .eq(Objects.nonNull(areaId), LayerManage::getAreaId, areaId)
                   );
    }

    @Override
    public Boolean deleteLayerManage(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
