package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;//package com.guhecloud.cube.srv.gen.maintain.electron.server.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolRouteMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolPlan;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolRoute;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.DeleteStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.PlanStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRoutePageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRouteParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolRouteService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.util.StrUtils;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 巡更路线
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolRouteServiceImpl extends ServiceImpl<PatrolRouteMapper, PatrolRoute> implements PatrolRouteService {
    private final AreaClient areaClient;
    private final OrganizationClientService organizationClientService;
    private final PatrolPlanMapper patrolPlanMapper;

    @Override
    public PatrolRouteVO queryById(Long id) {
        PatrolRoute route = baseMapper.selectOne(new LambdaQueryWrapper<PatrolRoute>()
                                                         .eq(PatrolRoute::getUuid, id)
                                                         .last("limit 1"));
        Assert.isNotFound(ObjectUtils.isEmpty(route), "没有查询到巡更路线!");
        List<PatrolLocationVO> location = baseMapper.queryById(id);
        location.forEach(ex -> {
            List<AreaDTO> area = areaClient.getByIds(StringUtils.splitAndTranslate(ex.getAreaId(), Long::valueOf));
            if (ObjectUtils.isNotEmpty(area)) {
                ex.setArea(area);
            }
        });
        PatrolRouteVO patrolRoute = new PatrolRouteVO();
        patrolRoute.setPatrolRouteName(route.getPatrolRouteName());
        patrolRoute.setPatrolLocation(location);
        patrolRoute.setId(id);
        return patrolRoute;
    }

    @Override
    public Pagination<PatrolRouteVO> queryRoutePage(PatrolRoutePageParam param) {
        Page<PatrolRouteVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.queryRoutePage(page, param);
        page.getRecords().stream().forEach(ex -> {
            if (StringUtils.isNotBlank(ex.getCreatorId()) && ActorTypeEnum.USER.name().equals(ex.getCreatorType())) {
                ex.setCreateBy(organizationClientService.getUserName(Long.valueOf(ex.getCreatorId())));
            } else {
                ex.setCreateBy(organizationClientService.getUserName(ex.getCreateBy()));
            }
        });
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public List<PatrolRouteVO> queryRouteList(PatrolRoutePageParam param) {
        List<PatrolRouteVO> patrolRouteVOS = baseMapper.queryRouteList(param);
        return patrolRouteVOS;
    }

    /**
     * 查询所有巡更路线
     *
     * @return 巡更路线
     */
    @Override
    public List<PatrolRouteVO> queryRouteAll() {
        List<PatrolRouteVO> routeList = baseMapper.queryRouteAll();
        routeList.forEach(ex -> {
            List<PatrolLocationVO> location = baseMapper.queryLocationAllByRoute(ex.getId());
            if (ObjectUtils.isNotEmpty(location)) {
                location.forEach(obj -> {
                    List<AreaDTO> area = areaClient.getByIds(StrUtils.StrToList(obj.getAreaId()));
                    obj.setArea(area);
                });
                ex.setPatrolLocation(location);
            }
        });
        return routeList;
    }

    @Override
    public Boolean insert(PatrolRouteParam param) {
        PatrolRoute patrolRoute = this.baseMapper.selectOne(new LambdaQueryWrapper<PatrolRoute>()
                                                                    .eq(PatrolRoute::getPatrolRouteName, param.getPatrolRouteName())
                                                                    .eq(PatrolRoute::getStatus, DeleteStatusEnum.NO_REMOVE.name())
                                                                    .last("limit 1"));
        if (ObjectUtils.isNotEmpty(patrolRoute)) {
            throw new ServiceException("巡更路线已存在");
        }
        Long uuid = System.currentTimeMillis();
        return insert(param, uuid);
    }

    private Boolean insert(PatrolRouteParam param, Long uuid) {
        List<PatrolRouteParam> locationIds = param.getLocationIds();
        for (int i = 0; i < locationIds.size(); i++) {
            PatrolRoute route = new PatrolRoute();
            route.setPatrolRouteName(param.getPatrolRouteName());
            route.setRuleOrder(i + 1);
            route.setStatus(DeleteStatusEnum.NO_REMOVE.name());
            route.setUuid(uuid);
            route.setPatrolLocationId(locationIds.get(i).getId());
            route.setNumber(locationIds.size());
            baseMapper.insert(route);
        }
        return true;
    }

    @Override
    public Boolean update(PatrolRouteParam param) {
        PatrolRoute patrolRoute = this.baseMapper.selectOne(new LambdaQueryWrapper<PatrolRoute>()
                                                                    .eq(PatrolRoute::getPatrolRouteName, param.getPatrolRouteName())
                                                                    .eq(PatrolRoute::getStatus, DeleteStatusEnum.NO_REMOVE.name())
                                                                    .last("limit 1"));
        if (ObjectUtils.isNotEmpty(patrolRoute)) {
            if (!param.getId().equals(patrolRoute.getUuid())) {
                throw new ServiceException("巡更路线已存在");
            }
        }
        this.baseMapper.delete(new LambdaQueryWrapper<PatrolRoute>()
                                       .eq(PatrolRoute::getUuid, param.getId()));
        return this.insert(param, param.getId());
    }

    @Override
    public Boolean deleteById(Long id) {
        var check = this.checkDeleteById(id);
        Assert.isConflict(StringUtils.isNotBlank(check), check);
        List<PatrolRoute> list = this.lambdaQuery().eq(PatrolRoute::getUuid, id).list();
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(route -> {
            route.setStatus(DeleteStatusEnum.REMOVE.name());
        });
        return this.updateBatchById(list);
    }

    private String checkDeleteById(Long id) {
        var result = new StringBuffer();
        var planList = patrolPlanMapper.selectList(Wrappers.<PatrolPlan>lambdaQuery().eq(PatrolPlan::getRouteId, id)
                                                           .eq(PatrolPlan::getStatus, PlanStatusEnum.ENABLE.name()));
        if (CollectionUtils.isEmpty(planList)) {
            return result.toString();
        }
        result.append("计划：");
        planList.forEach(ex -> result.append(ex.getPlanName()).append(","));
        result.append("正在使用，请先移除后再删除。");
        return result.toString();
    }

    @Override
    public Boolean deleteByLocationId(Long locationId) {
        return this.lambdaUpdate().set(PatrolRoute::getStatus, DeleteStatusEnum.REMOVE.name())
                .eq(PatrolRoute::getPatrolLocationId, locationId)
                .update();
    }

    @Override
    public List<PatrolRouteVO> queryByUuids(Set<Long> collect) {
        return this.baseMapper.queryByUuids(collect);
    }
}
