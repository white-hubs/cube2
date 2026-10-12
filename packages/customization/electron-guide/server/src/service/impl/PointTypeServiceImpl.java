package com.guhecloud.cube.srv.electron.guide.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.electron.guide.dao.PointTypeMapper;
import com.guhecloud.cube.srv.electron.guide.entity.PointType;
import com.guhecloud.cube.srv.electron.guide.param.PointTypeModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.param.query.PointTypeQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.PointManageService;
import com.guhecloud.cube.srv.electron.guide.service.PointTypeService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointTypeServiceImpl extends ServiceImpl<PointTypeMapper, PointType> implements PointTypeService {

    private final PointManageService pointManageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull PointTypeModifyParam param) {
        PointType pointType = ObjectUtils.copying(param, PointType.class);
        this.baseMapper.insert(pointType);
        return pointType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull PointTypeModifyParam param) {
        PointType pointType = this.getById(id);
        if (Objects.nonNull(pointType)) {
            ObjectUtils.copying(param, pointType);
            this.updateById(pointType);
        }
    }

    @Override
    public PointType getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Page<PointType> getPage(PointTypePageQueryParam param) {
        return this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<PointType>lambdaQuery()
                                                  .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                                                          .like(PointType::getCode, param.getKeyword())
                                                          .or()
                                                          .like(PointType::getName, param.getKeyword()))
                                                  .orderByDesc(PointType::getCreateTime)
                                         );
    }

    @Override
    public List<PointType> getList(PointTypeQueryParam param) {
        return this.baseMapper.selectList(Wrappers.<PointType>lambdaQuery()
                                                  .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                                                          .like(PointType::getCode, param.getKeyword())
                                                          .or()
                                                          .like(PointType::getName, param.getKeyword()))
                                                  .orderByDesc(PointType::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        var pointManageCount = pointManageService.getPointManageByTypeId(id);
        if (pointManageCount > 0) {
            throw new ServiceException("删除失败,类型下还存在正在使用的电位");
        }
        DatabaseContextHolder.softDelete(this, id);
    }
}
