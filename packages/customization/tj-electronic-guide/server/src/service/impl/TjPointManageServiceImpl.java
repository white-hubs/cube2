package com.guhecloud.cube.srv.tj.electronic.guide.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.tj.electronic.guide.dao.TjPointManageMapper;
import com.guhecloud.cube.srv.tj.electronic.guide.entity.TjPointManage;
import com.guhecloud.cube.srv.tj.electronic.guide.enumeration.ElectronGuideStateEnum;
import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjPointManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  12:02
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TjPointManageServiceImpl extends ServiceImpl<TjPointManageMapper, TjPointManage> implements TjPointManageService {
    @Override
    public PointManageDetailsVO getPointManageById(Long id) {
        TjPointManage tjPointManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(PointManageDetailsVO.class, tjPointManage);
    }

    @Override
    public Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param) {
        if (ObjectUtils.isEmpty(param.getState())) {
            param.setState(ElectronGuideStateEnum.ENABLE.name());
        } else if ("ALL".equals(param.getState())) {
            param.setState(null);
        }
        Page<PointManagePageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPointManagePage(page, param);
        List<PointManagePageVO> list = ConvertUtils.toList(PointManagePageVO.class, page.getRecords());
        return new Pagination<>(page.getTotal(), page.getPages(), list);
    }

    @Override
    public Boolean addPointManage(PointManageParam param) {
        TjPointManage tjPointManage = ConvertUtils.convert(TjPointManage.class, param);
        tjPointManage.setState(ElectronGuideStateEnum.ENABLE.name());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            tjPointManage.setLevel(0);
        }
        return this.baseMapper.insert(tjPointManage) > 0;
    }

    @Override
    public Boolean updatePointManage(PointManageParam param) {
        TjPointManage tjPointManage = this.baseMapper.selectById(param.getId());
        tjPointManage.setName(param.getName());
        tjPointManage.setAddress(param.getAddress());
        tjPointManage.setAreaId(param.getAreaId());
        tjPointManage.setCoordinate(param.getCoordinate());
        tjPointManage.setRemarks(param.getRemarks());
        tjPointManage.setState(param.getState());
        tjPointManage.setLevel(param.getLevel());
        if (ObjectUtils.isEmpty(param.getLevel())) {
            tjPointManage.setLevel(0);
        }
        return this.baseMapper.updateById(tjPointManage) > 0;
    }

    @Override
    public Boolean deletePointManage(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }
}
