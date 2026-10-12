package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.PositionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PositionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 岗位业务操作实现(前端目前展示的职位)
 *
 * @author Woody
 * @date 2021/7/2
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Resource
    private UserOrganizationService userOrganizationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePosition(@NonNull Long id) {
        Integer userCount = userOrganizationService.countUserPositions(Collections.singletonList(id));
        Assert.isNotAcceptable(0 != userCount, "删除失败,该职位下存在员工");
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Position addPosition(@NonNull PositionModifyParam param) {
        Position position = ObjectUtils.copying(param, Position.class);
        position.setSort(0F);
        this.save(position);
        return position;
    }

    @Override
    public Position updatePosition(@NonNull Long id, @NonNull PositionModifyParam param) {
        Position position = this.getById(id);
        Assert.isNotFound(position, "职位不存在");
        ObjectUtils.copying(param, position);
        this.updateById(position);
        return position;
    }

    @Override
    public List<Position> listPositions(@NonNull Collection<Long> ids) {
        return ids.isEmpty() ? Collections.emptyList() : this.listByIds(ids);
    }

    @Override
    public Page<Position> listPosistions(@NonNull PosistionQueryParam param) {
        return this.lambdaQuery().like(!StringUtils.isEmpty(param.getName()), Position::getName, param.getName())
                .eq(Objects.nonNull(param.getEnabled()), Position::getEnabled, param.getEnabled())
                .orderByAsc(Position::getSort).orderByDesc(Position::getId).page(PagingUtils.convert(param));
    }

    @Override
    public List<Position> list(PosistionQueryParam param) {
        return this.lambdaQuery().like(!StringUtils.isEmpty(param.getName()), Position::getName, param.getName())
                .eq(Objects.nonNull(param.getEnabled()), Position::getEnabled, param.getEnabled())
                .list();
    }
}
