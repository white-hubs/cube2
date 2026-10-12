package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.RouteProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.RouteProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.RouteProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectItemService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.RouteProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteDeviceVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteProjectVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：支路分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteProjectServiceImpl extends ServiceImpl<RouteProjectMapper, RouteProject> implements RouteProjectService {

    private final EnergyProjectItemMapper energyProjectItemMapper;

    private final RouteProjectMapper routeProjectMapper;

    private final EnergyProjectItemService energyProjectItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(RouteProjectParam param) {
        RouteProject routeProject = ObjectUtils.copying(param, RouteProject.class);
        this.baseMapper.insert(routeProject);
        //处理分项关联设备
        if (!CollectionUtils.isEmpty(param.getEnergyProjectItemParams())) {
            param.getEnergyProjectItemParams().forEach(energyProjectItemParam -> {
                EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
                //设置分项类型
                energyProjectItem.setBindType(BindTypeEnum.ROUTE.toString());
                //设置分项id
                energyProjectItem.setProjectId(routeProject.getId());
                energyProjectItemMapper.insert(energyProjectItem);
            });
        }
        return routeProject.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, RouteProjectParam param) {
        RouteProject routeProject = this.baseMapper.selectById(id);
        Assert.isNotFound(routeProject, "未查询到相关信息");
        ObjectUtils.copying(param, routeProject);
        this.baseMapper.updateById(routeProject);
        //删除所有的分项设置
        energyProjectItemMapper.deleteByProjectId(id);
        //处理分项关联设备
        param.getEnergyProjectItemParams().forEach(energyProjectItemParam -> {
            EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
            //设置分项类型
            energyProjectItem.setBindType(BindTypeEnum.ROUTE.toString());
            //设置分项id
            energyProjectItem.setProjectId(routeProject.getId());
            energyProjectItemMapper.insert(energyProjectItem);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        List<Long> objects = new ArrayList<>();
        objects.add(id);
        DatabaseContextHolder.softDelete(this, id);
        List<RouteProject> list = this.list(Wrappers.<RouteProject>lambdaQuery()
                                                    .eq(BasicEnhancedTreeEntity::getParentId, id));
        if (list.size() > 0) {//三级
            List<Long> collect = list.stream().map(RouteProject::getId).collect(Collectors.toList());
            objects.addAll(collect);
            DatabaseContextHolder.softDelete(this, list);
            List<RouteProject> list1 = this.list(Wrappers.<RouteProject>lambdaQuery()
                                                         .in(BasicEnhancedTreeEntity::getParentId, collect));
            if (list1.size() > 0) {
                List<Long> collect1 = list1.stream().map(RouteProject::getId).collect(Collectors.toList());
                DatabaseContextHolder.softDelete(this, list1);
                objects.addAll(collect1);
            }
        }
        List<EnergyProjectItem> energyProjectItems = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                                .in(EnergyProjectItem::getProjectId, objects));
        DatabaseContextHolder.softDelete(energyProjectItemService, energyProjectItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public RouteProjectVO getById(Long id) {
        RouteProject routeProject = this.baseMapper.selectById(id);
        RouteProjectVO routeProjectVO = ObjectUtils.copying(routeProject, RouteProjectVO.class);
        if (routeProjectVO != null) {
            //根据分项id查询绑定设备信息
            List<EnergyProjectItem> allByProjectIdList = energyProjectItemMapper.getAllByProjectIdList(id);
            List<EnergyProjectItemVO> energyProjectItemVOS = ObjectUtils.copying(allByProjectIdList, EnergyProjectItemVO.class);
            energyProjectItemVOS.forEach(energyProjectItemVO -> {
                if (StringUtils.isNotEmpty(energyProjectItemVO.getDeviceArea())) {
                    energyProjectItemVO.setDeviceAreaName(energyProjectItemVO.getDeviceArea());
                }
            });

            routeProjectVO.setEnergyProjectItemVOS(energyProjectItemVOS);
        }
        return routeProjectVO;
    }

    @Override
    public Pagination<RouteProjectVO> getPage(RouteProjectQueryParam param) {
        //请自行添加查询条件
        Page<RouteProject> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), RouteProject::getInstrumentType, param.getInstrumentType())
                .page(PagingUtils.convert(param));
        Pagination<RouteProjectVO> pagination = PagingUtils.convert(page, RouteProjectVO.class);
        //统计各分项仪表个数
        List<RouteProjectVO> records = pagination.getRecords();
        records.forEach(e -> {
            Long count = energyProjectItemMapper.selectCount(new LambdaQueryWrapper<EnergyProjectItem>().eq(EnergyProjectItem::getProjectId, e.getId()));
            e.setMetersNum(count);
        });
        return pagination;
    }

    @Override
    public List<RouteDeviceVo> getRouteDevices() {
        return routeProjectMapper.getRouteDevices();
    }
}