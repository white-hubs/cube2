package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AreaProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AreaProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AreaProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AreaProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AreaProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectItemService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AreaProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceAreaVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：区域分项配置服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaProjectServiceImpl extends ServiceImpl<AreaProjectMapper, AreaProject> implements AreaProjectService {

    private final EnergyProjectItemMapper energyProjectItemMapper;

    private final EnergyProjectItemService energyProjectItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(AreaProjectParam param) {
        AreaProject areaProject = ObjectUtils.copying(param, AreaProject.class);
        this.baseMapper.insert(areaProject);
        //处理分项关联设备
        this.saveReferDevice(areaProject.getId(), param.getEnergyProjectItemParams());
        return areaProject.getId();
    }

    /**
     * 区域和设备关联
     *
     * @param id
     * @param deviceList
     */
    private void saveReferDevice(Long id, List<EnergyProjectItemParam> deviceList) {
        if (!CollectionUtils.isEmpty(deviceList)) {
            deviceList.forEach(energyProjectItemParam -> {
                EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
                //设置分项类型
                energyProjectItem.setBindType(BindTypeEnum.AREA.toString());
                //设置分项id
                energyProjectItem.setProjectId(id);
                energyProjectItemMapper.insert(energyProjectItem);
            });
        }
    }

    @Override
    public void areaReferDevice(Long id, List<EnergyProjectItemParam> deviceList) {
        //删除所有的分项设置
        energyProjectItemMapper.deleteByProjectId(id);
        //保存现有的配置
        this.saveReferDevice(id, deviceList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, AreaProjectParam param) {
        AreaProject areaProject = this.baseMapper.selectById(id);
        Assert.isNotFound(areaProject, "未查询到相关信息");
        ObjectUtils.copying(param, areaProject);
        this.baseMapper.updateById(areaProject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        List<Long> objects = new ArrayList<>();
        objects.add(id);
        DatabaseContextHolder.softDelete(this, id);
        List<AreaProject> list = this.list(Wrappers.<AreaProject>lambdaQuery()
                                                   .eq(BasicEnhancedTreeEntity::getParentId, id));
        if (list.size() > 0) {//三级
            List<Long> collect = list.stream().map(AreaProject::getId).collect(Collectors.toList());
            objects.addAll(collect);
            DatabaseContextHolder.softDelete(this, list);
            List<AreaProject> list1 = this.list(Wrappers.<AreaProject>lambdaQuery()
                                                        .in(BasicEnhancedTreeEntity::getParentId, collect));
            if (list1.size() > 0) {
                List<Long> collect1 = list1.stream().map(AreaProject::getId).collect(Collectors.toList());
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
    public AreaProjectVO getById(Long id) {
        AreaProject areaProject = this.baseMapper.selectById(id);
        AreaProjectVO areaProjectVO = ObjectUtils.copying(areaProject, AreaProjectVO.class);
        if (areaProjectVO != null) {
            //根据分项id查询绑定设备信息
            List<EnergyProjectItem> allByProjectIdList = energyProjectItemMapper.getAllByProjectIdList(id);
            List<EnergyProjectItemVO> energyProjectItemVOS = ObjectUtils.copying(allByProjectIdList, EnergyProjectItemVO.class);
            energyProjectItemVOS.forEach(energyProjectItemVO -> {
                if (StringUtils.isNotEmpty(energyProjectItemVO.getDeviceArea())) {
                    energyProjectItemVO.setDeviceAreaName(energyProjectItemVO.getDeviceArea());
                }
            });

            areaProjectVO.setEnergyProjectItemVOS(energyProjectItemVOS);
        }
        return areaProjectVO;
    }

    @Override
    public List<EnergyProjectItemVO> getReferDeviceById(Long id) {
        List<EnergyProjectItem> allByProjectIdList = energyProjectItemMapper.getAllByProjectIdList(id);
        List<EnergyProjectItemVO> energyProjectItemVOS = ObjectUtils.copying(allByProjectIdList, EnergyProjectItemVO.class);
        //todo 这里的逻辑待定 目的是返回区域名称
        energyProjectItemVOS.forEach(energyProjectItemVO -> {
            if (StringUtils.isNotEmpty(energyProjectItemVO.getDeviceArea())) {
                energyProjectItemVO.setDeviceAreaName(energyProjectItemVO.getDeviceArea());
            }
        });

        return energyProjectItemVOS;
    }

    @Override
    public Pagination<AreaProjectVO> getPage(AreaProjectQueryParam param) {
        //请自行添加查询条件
        Page<AreaProject> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), AreaProject::getInstrumentType, param.getInstrumentType())
                .page(PagingUtils.convert(param));
        Pagination<AreaProjectVO> pagination = PagingUtils.convert(page, AreaProjectVO.class);
        //统计各分项仪表个数
        List<AreaProjectVO> records = pagination.getRecords();
        records.forEach(e -> {
            Long count = energyProjectItemMapper.selectCount(new LambdaQueryWrapper<EnergyProjectItem>().eq(EnergyProjectItem::getProjectId, e.getId()));
            e.setMetersNum(count);
        });
        return pagination;
    }

    @Override
    public List<DeviceAreaVo> getDeviceAreas() {
        return energyProjectItemMapper.getDeviceAreas();
    }

    /**
     * 根据绑定类型以及设备id判断分摊比例是否超过限制
     *
     * @param bindType
     * @param deviceId
     *
     * @return
     */
    private Integer judgmentRatio(String bindType, Long deviceId, Long id) {
        return energyProjectItemMapper.selectList(new LambdaQueryWrapper<EnergyProjectItem>()
                                                          .eq(EnergyProjectItem::getBindType, bindType)
                                                          .notIn(id != null, EnergyProjectItem::getId, id)
                                                          .eq(EnergyProjectItem::getDeviceId, deviceId))
                .stream()
                .map(EnergyProjectItem::getShareNum)
                .collect(Collectors.toList())
                .stream()
                .reduce(Integer::sum)
                .orElse(0);
    }

    @Override
    public long getDeviceNumInAreaProject(Long projectId) {
        return energyProjectItemMapper.selectCount(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                           .eq(EnergyProjectItem::getProjectId, projectId));
    }

    @Override
    public List<DeviceInEnergyProjectVO> getAllDeviceInAreaProject(String projectId) {
        return this.baseMapper.getAllDeviceInAreaProject(projectId);
    }

    @Override
    public List<Long> getDeviceForStat() {
        List<AreaProject> topAreaList = this.baseMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                           .isNull(AreaProject::getParentId));
        List<Long> allDeviceForStat = new ArrayList<>();
        this.getDeviceForStat(topAreaList, allDeviceForStat);
        return allDeviceForStat;
    }

    /**
     * 获取区域关联的设备id
     * 如果自己有关联设备就用自己的，如果自己没有就汇总子级的设备(子级找到设备就停止，不再继续找下级)
     *
     * @param areaIdList 区域id集合，如果传空就查询所有。
     *
     * @return 每个区域对应的设备
     */
    @Override
    public Map<Long, List<Long>> getAreaLinkDevice(List<Long> areaIdList) {
        // 区域对应的设备id
        Map<Long, List<Long>> areaDeviceIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(areaIdList)) {
            // 最上级区域
            List<AreaProject> areaProjectList = this.list(Wrappers.<AreaProject>lambdaQuery()
                                                                  .isNull(AreaProject::getParentId)
                                                         );
            if (!CollectionUtils.isEmpty(areaProjectList)) {
                areaIdList = areaProjectList.stream().map(AreaProject::getId).collect(Collectors.toList());
            }
        }

        if (!CollectionUtils.isEmpty(areaIdList)) {
            for (Long areaId : areaIdList) {
                List<Long> idList = new ArrayList<>();
                this.getAreaDeviceIdList(areaId, idList, areaDeviceIdMap);
                areaDeviceIdMap.put(areaId, idList);
            }
        }
        return areaDeviceIdMap;
    }

    /**
     * 获取区域生效的设备表id(如果有表就是改表，如果没有表递归获取子节点的表，直到没有子节点)
     *
     * @param areaId       区域id
     * @param deviceIdList 最终的设备表id
     */
    private void getAreaDeviceIdList(Long areaId, List<Long> deviceIdList, Map<Long, List<Long>> areaDeviceIdMap) {
        //查询该区域下是否有表
        List<EnergyProjectItem> deviceList = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                        .eq(EnergyProjectItem::getProjectId, areaId));
        if (CollectionUtils.isEmpty(deviceList)) {
            //检查下级节点
            List<AreaProject> childrenAreaList = this.baseMapper.selectList(Wrappers.<AreaProject>lambdaQuery()
                                                                                    .eq(AreaProject::getParentId, areaId));
            if (!CollectionUtils.isEmpty(childrenAreaList)) {
                for (AreaProject areaProject : childrenAreaList) {
                    List<Long> chilIdList = new ArrayList<>();
                    this.getAreaDeviceIdList(areaProject.getId(), chilIdList, areaDeviceIdMap);
                    deviceIdList.addAll(chilIdList);
                    areaDeviceIdMap.put(areaProject.getId(), chilIdList);
                }
            }
        } else {
            List<Long> deviceIds = deviceList.stream().map(EnergyProjectItem::getDeviceId).collect(Collectors.toList());
            deviceIdList.addAll(deviceIds);
            areaDeviceIdMap.put(areaId, new ArrayList<>(deviceIdList));
        }
    }

    private void getDeviceForStat(List<AreaProject> areaList, List<Long> deviceIdList) {
        for (AreaProject area : areaList) {
            this.getAreaDeviceIdList(area.getId(), deviceIdList, new HashMap<>());
        }
    }
}