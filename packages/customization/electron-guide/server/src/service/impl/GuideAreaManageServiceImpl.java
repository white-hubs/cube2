package com.guhecloud.cube.srv.electron.guide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.electron.guide.dao.GuideAreaManageMapper;
import com.guhecloud.cube.srv.electron.guide.entity.GuideAreaManage;
import com.guhecloud.cube.srv.electron.guide.enumeration.ElectronGuideStateEnum;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideAreaManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:38
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GuideAreaManageServiceImpl extends ServiceImpl<GuideAreaManageMapper, GuideAreaManage> implements GuideAreaManageService {
    @Override
    public GuideAreaDetailsVO getGuideAreaById(Long id) {
        GuideAreaManage guideAreaManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(GuideAreaDetailsVO.class, guideAreaManage);
    }

    @Override
    public Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param) {
        if (ObjectUtils.isEmpty(param.getState())) {
            param.setState(ElectronGuideStateEnum.ENABLE.name());
        } else if ("ALL".equals(param.getState())) {
            param.setState(null);
        }
        Page<GuideAreaManage> page = new Page<>(param.getPageNumber(), param.getPageSize());
        if (ObjectUtils.isEmpty(param.getSequence()) || param.getSequence()) {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<GuideAreaManage>()
                    .eq(ObjectUtils.isNotEmpty(param.getState()), GuideAreaManage::getState, param.getState())
                    .orderByAsc(GuideAreaManage::getSort));
        } else {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<GuideAreaManage>()
                    .eq(ObjectUtils.isNotEmpty(param.getState()), GuideAreaManage::getState, param.getState())
                    .orderByDesc(GuideAreaManage::getSort));
        }
        return new Pagination<>(page.getTotal(), page.getPages(), ConvertUtils.toList(GuideAreaPageVO.class, page.getRecords()));
    }

    @Override
    public List<GuideAreaPageVO> getGuideAreas(GuideAreaQueryParam param) {
        boolean isAsc = false;
        if (Objects.nonNull(param.getSequence())) {
            isAsc = param.getSequence();
        }
        List<GuideAreaManage> guideAreaManages = this.baseMapper
                .selectList(Wrappers.<GuideAreaManage>lambdaQuery()
                                    .eq(StringUtils.isNotBlank(param.getState()), GuideAreaManage::getState, param.getState())
                                    .orderBy(true, isAsc, GuideAreaManage::getSort)
                           );
        return ObjectUtils.copying(guideAreaManages, GuideAreaPageVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addGuideArea(GuideAreaParam param) {
        GuideAreaManage areaManage = ConvertUtils.convert(GuideAreaManage.class, param);
        areaManage.setState(ElectronGuideStateEnum.ENABLE.name());
        if (ObjectUtils.isEmpty(param.getSort())) {
            areaManage.setSort(0);
        }
        return this.baseMapper.insert(areaManage) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateGuideArea(GuideAreaParam param) {
        GuideAreaManage guideAreaManage = this.baseMapper.selectById(param.getId());
        guideAreaManage.setParentId(param.getParentId());
        guideAreaManage.setName(param.getName());
        guideAreaManage.setCoordinate(param.getCoordinate());
        guideAreaManage.setZoom(param.getZoom());
        guideAreaManage.setSort(param.getSort());
        guideAreaManage.setState(param.getState());
        if (ObjectUtils.isEmpty(param.getSort())) {
            guideAreaManage.setSort(0);
        }
        boolean result = this.baseMapper.updateById(guideAreaManage) > 0;
        if (ElectronGuideStateEnum.DISABLE.name().equals(param.getState())) {
            //同步禁用子级区域
            this.update(Wrappers.<GuideAreaManage>lambdaUpdate()
                                .set(GuideAreaManage::getState, ElectronGuideStateEnum.DISABLE.name())
                                .like(GuideAreaManage::getSequence, guideAreaManage.getSequence())
                       );
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGuideArea(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
