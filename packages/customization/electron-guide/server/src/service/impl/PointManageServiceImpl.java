package com.guhecloud.cube.srv.electron.guide.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.electron.guide.dao.PointManageMapper;
import com.guhecloud.cube.srv.electron.guide.entity.PointManage;
import com.guhecloud.cube.srv.electron.guide.enumeration.ElectronGuideStateEnum;
import com.guhecloud.cube.srv.electron.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.PointManageParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideAreaManageService;
import com.guhecloud.cube.srv.electron.guide.service.PointManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.PointManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  12:02
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointManageServiceImpl extends ServiceImpl<PointManageMapper, PointManage> implements PointManageService {

    private final GuideAreaManageService guideAreaManageService;

    @Override
    public PointManageDetailsVO getPointManageById(Long id) {
        PointManage pointManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(PointManageDetailsVO.class, pointManage);
    }

    @Override
    public Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param) {
        if (ObjectUtils.isEmpty(param.getState())) {
            param.setState(ElectronGuideStateEnum.ENABLE.name());
        } else if ("ALL".equals(param.getState())) {
            param.setState(null);
        }
        if (Objects.nonNull(param.getAreaId())) {
            param.setAreaIdStr(String.valueOf(param.getAreaId()));
        }
        Page<PointManagePageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPointManagePage(page, param);
        List<PointManagePageVO> list = ConvertUtils.toList(PointManagePageVO.class, page.getRecords());
        return new Pagination<>(page.getTotal(), page.getPages(), list);
    }

    @Override
    public Long getPointManageByTypeId(@NonNull Long typeId) {
        return this.baseMapper.selectCount(Wrappers.<PointManage>lambdaQuery()
                                                   .eq(PointManage::getId, typeId));
    }

    @Override
    public Boolean addPointManage(PointManageParam param) {
        PointManage pointManage = ConvertUtils.convert(PointManage.class, param);
        pointManage.setState(ElectronGuideStateEnum.ENABLE.name());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            pointManage.setLevel(0);
        }
        GuideAreaDetailsVO area = this.guideAreaManageService.getGuideAreaById(param.getAreaId());
        if (Objects.nonNull(area)) {
            pointManage.setAreaSequence(area.getSequence());
        }
        return this.baseMapper.insert(pointManage) > 0;
    }

    @Override
    public Boolean updatePointManage(PointManageParam param) {
        PointManage pointManage = this.baseMapper.selectById(param.getId());
        pointManage.setName(param.getName());
        pointManage.setTypeId(param.getTypeId());
        pointManage.setAddress(param.getAddress());
        pointManage.setAreaId(param.getAreaId());
        pointManage.setCoordinate(param.getCoordinate());
        pointManage.setRemarks(param.getRemarks());
        pointManage.setState(param.getState());
        pointManage.setLevel(param.getLevel());
        pointManage.setFilePath(param.getFilePath());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            pointManage.setLevel(0);
        }
        GuideAreaDetailsVO area = this.guideAreaManageService.getGuideAreaById(param.getAreaId());
        if (Objects.nonNull(area)) {
            pointManage.setAreaSequence(area.getSequence());
        }
        return this.baseMapper.updateById(pointManage) > 0;
    }

    @Override
    public Boolean deletePointManage(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
