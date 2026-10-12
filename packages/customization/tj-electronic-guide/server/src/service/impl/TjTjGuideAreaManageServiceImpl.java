package com.guhecloud.cube.srv.tj.electronic.guide.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.tj.electronic.guide.dao.TjGuideAreaManageMapper;
import com.guhecloud.cube.srv.tj.electronic.guide.entity.TjGuideAreaManage;
import com.guhecloud.cube.srv.tj.electronic.guide.enumeration.ElectronGuideStateEnum;
import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjGuideAreaManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:38
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TjTjGuideAreaManageServiceImpl extends ServiceImpl<TjGuideAreaManageMapper, TjGuideAreaManage> implements TjGuideAreaManageService {
    @Override
    public GuideAreaDetailsVO getGuideAreaById(Long id) {
        TjGuideAreaManage tjGuideAreaManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(GuideAreaDetailsVO.class, tjGuideAreaManage);
    }

    @Override
    public Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param) {
        if (ObjectUtils.isEmpty(param.getState())) {
            param.setState(ElectronGuideStateEnum.ENABLE.name());
        } else if ("ALL".equals(param.getState())) {
            param.setState(null);
        }
        Page<TjGuideAreaManage> page = new Page<>(param.getPageNumber(), param.getPageSize());
        if (ObjectUtils.isEmpty(param.getSequence()) || param.getSequence()) {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<TjGuideAreaManage>()
                    .eq(ObjectUtils.isNotEmpty(param.getState()), TjGuideAreaManage::getState, param.getState())
                    .orderByAsc(TjGuideAreaManage::getLevel));
        } else {
            page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<TjGuideAreaManage>()
                    .eq(ObjectUtils.isNotEmpty(param.getState()), TjGuideAreaManage::getState, param.getState())
                    .orderByDesc(TjGuideAreaManage::getLevel));
        }
        return new Pagination<>(page.getTotal(), page.getPages(), ConvertUtils.toList(GuideAreaPageVO.class, page.getRecords()));
    }

    @Override
    public Boolean addGuideArea(GuideAreaParam param) {
        TjGuideAreaManage areaManage = ConvertUtils.convert(TjGuideAreaManage.class, param);
        areaManage.setState(ElectronGuideStateEnum.ENABLE.name());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            areaManage.setLevel(0);
        }
        return this.baseMapper.insert(areaManage) > 0;
    }

    @Override
    public Boolean updateGuideArea(GuideAreaParam param) {
        TjGuideAreaManage tjGuideAreaManage = this.baseMapper.selectById(param.getId());
        tjGuideAreaManage.setName(param.getName());
        tjGuideAreaManage.setCoordinate(param.getCoordinate());
        tjGuideAreaManage.setZoom(param.getZoom());
        tjGuideAreaManage.setLevel(param.getLevel());
        tjGuideAreaManage.setState(param.getState());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            tjGuideAreaManage.setLevel(0);
        }
        return this.baseMapper.updateById(tjGuideAreaManage) > 0;
    }

    @Override
    public Boolean deleteGuideArea(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
