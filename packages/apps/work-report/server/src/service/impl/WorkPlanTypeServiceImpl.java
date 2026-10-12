package com.guhecloud.cube.srv.gen.reportmanagement.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.WorkPlanTypeMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlanType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.WorkPlanTypeService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：工作安排类型服务层
 *
 * @author Appet
 * @date 2024-01-04 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPlanTypeServiceImpl extends ServiceImpl<WorkPlanTypeMapper, WorkPlanType> implements WorkPlanTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(WorkPlanTypeParam param) {
        WorkPlanType workPlanType = ObjectUtils.copying(param, WorkPlanType.class);
        this.baseMapper.insert(workPlanType);
        return workPlanType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, WorkPlanTypeParam param) {
        WorkPlanType workPlanType = this.baseMapper.selectById(id);
        Assert.isNotFound(workPlanType, "未查询到相关信息");
        ObjectUtils.copying(param, workPlanType);
        this.baseMapper.updateById(workPlanType);
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
    public WorkPlanTypeVO getById(Long id) {
        WorkPlanType workPlanType = this.baseMapper.selectById(id);
        return ObjectUtils.copying(workPlanType, WorkPlanTypeVO.class);
    }

    @Override
    public Pagination<WorkPlanTypeVO> getPage(WorkPlanTypeQueryParam param) {
        //请自行添加查询条件
        Page<WorkPlanType> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), WorkPlanType::getName, param.getName())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, WorkPlanTypeVO.class);
    }

    @Override
    public List<WorkPlanTypeVO> getList(WorkPlanTypeQueryParam param) {
        List<WorkPlanType> list = this.list(Wrappers.<WorkPlanType>lambdaQuery()
                                                    .like(StringUtils.isNotBlank(param.getName()), WorkPlanType::getName, param.getName())
                                                    .eq(param.getStatus() != null, WorkPlanType::getStatus, param.getStatus()));
        return ObjectUtils.copying(list, WorkPlanTypeVO.class);
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        WorkPlanType workPlanType = this.baseMapper.selectById(id);
        Assert.isNotFound(workPlanType, "未找到对象，无法更新");
        workPlanType.setStatus(status);
        this.baseMapper.updateById(workPlanType);
    }
}