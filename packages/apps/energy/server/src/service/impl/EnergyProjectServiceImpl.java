package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectItemMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyProjectMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyProjectItem;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.BindTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectItemParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceInEnergyProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.DeviceProjectVo;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectItemVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：能耗分项服务层
 *
 * @author lrj
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyProjectServiceImpl extends ServiceImpl<EnergyProjectMapper, EnergyProject> implements EnergyProjectService {

    private final EnergyProjectItemMapper energyProjectItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EnergyProjectParam param) {

        if (param.getParentId() != null) {
            EnergyProject parent = baseMapper.selectById(param.getParentId());
            if (parent != null && StringUtils.isNotBlank(parent.getSequence()) && parent.getSequence().length() >= 120) {
                throw new ServiceException("层级过长！");
            }
        }

        //处理分项基本信息
        EnergyProject energyProject = ObjectUtils.copying(param, EnergyProject.class);
        energyProject.setCode(BizCodeUtils.generateNoRandom("ITEM"));
        this.baseMapper.insert(energyProject);
        //处理分项关联设备
        if (!CollectionUtils.isEmpty(param.getEnergyProjectItemParams())) {
            param.getEnergyProjectItemParams().forEach(energyProjectItemParam -> {
                EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
                //设置分项类型
                energyProjectItem.setBindType(BindTypeEnum.ITEM.toString());
                //设置分项id
                energyProjectItem.setProjectId(energyProject.getId());
                energyProjectItemMapper.insert(energyProjectItem);
            });
        }
        return energyProject.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EnergyProjectParam param) {
        EnergyProject energyProject = this.baseMapper.selectById(id);
        Assert.isNotFound(energyProject, "未查询到相关信息");
        ObjectUtils.copying(param, energyProject);
        this.baseMapper.updateById(energyProject);
        if (!CollectionUtils.isEmpty(param.getEnergyProjectItemParams())) {
            //删除所有的分项设置
            energyProjectItemMapper.deleteByProjectId(id);
            //处理分项关联设备
            param.getEnergyProjectItemParams().forEach(energyProjectItemParam -> {
                EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
                //设置分项类型
                energyProjectItem.setBindType(BindTypeEnum.ITEM.toString());
                //设置分项id
                energyProjectItem.setProjectId(energyProject.getId());
                energyProjectItemMapper.insert(energyProjectItem);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
        energyProjectItemMapper.deleteByProjectId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EnergyProjectVO getById(Long id) {
        EnergyProject energyProject = this.baseMapper.selectById(id);
        EnergyProjectVO energyProjectVO = ObjectUtils.copying(energyProject, EnergyProjectVO.class);
        if (energyProjectVO != null) {
            List<EnergyProjectItem> allByProjectIdList = energyProjectItemMapper.getAllByProjectIdList(id);
            List<EnergyProjectItemVO> energyProjectItemVOS = ObjectUtils.copying(allByProjectIdList, EnergyProjectItemVO.class);
            energyProjectItemVOS.forEach(energyProjectItemVO -> {
                if (StringUtils.isNotEmpty(energyProjectItemVO.getDeviceArea())) {
                    energyProjectItemVO.setDeviceAreaName(energyProjectItemVO.getDeviceArea());
                }
            });
            energyProjectVO.setEnergyProjectItemVOS(energyProjectItemVOS);
        }
        return energyProjectVO;
    }

    @Override
    public Pagination<EnergyProjectVO> getPage(EnergyProjectQueryParam param) {
        //请自行添加查询条件
        Page<EnergyProject> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), EnergyProject::getInstrumentType, param.getInstrumentType())
                .page(PagingUtils.convert(param));
        Pagination<EnergyProjectVO> pagination = PagingUtils.convert(page, EnergyProjectVO.class);
        List<EnergyProjectVO> records = pagination.getRecords();
        //扩展信息
        records.forEach(e -> this.extendEnergyProject(e));
        return pagination;
    }

    /**
     * 扩展分项信息
     *
     * @param data
     */
    private void extendEnergyProject(EnergyProjectVO data) {
        Long count = energyProjectItemMapper.selectCount(new LambdaQueryWrapper<EnergyProjectItem>()
                                                                 .eq(EnergyProjectItem::getProjectId, data.getId()));
        data.setMetersNum(count);
        //上级名称
        if (data.getParentId() != null) {
            EnergyProjectVO parent = this.getById(data.getParentId());
            data.setParentName(parent != null ? parent.getProjectName() : "");
        }
        //翻译仪表类型
        data.setInstrumentTypeName(InstrumentTypeEnum.getFireinspEnum(data.getInstrumentType()));
    }

    @Override
    public List<EnergyProjectVO> getList(EnergyProjectQueryParam param) {
        List<EnergyProject> dataList = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getInstrumentType()), EnergyProject::getInstrumentType, param.getInstrumentType())
                .list();
        if (!CollectionUtils.isEmpty(dataList)) {
            List<EnergyProjectVO> datavoList = ObjectUtils.copying(dataList, EnergyProjectVO.class);
            datavoList.stream().forEach(d -> this.extendEnergyProject(d));
            return datavoList;
        }
        //扩展信息
        return Collections.emptyList();
    }

    @Override
    public List<DeviceProjectVo> getDeviceProject() {

        return energyProjectItemMapper.getDeviceProject();
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
    public void saveProjectReferDevice(Long id, List<EnergyProjectItemParam> referDeviceList) {
        //绑定设备，先删除之前的设备
        energyProjectItemMapper.deleteByProjectId(id);
        //处理分项关联设备
        if (!CollectionUtils.isEmpty(referDeviceList)) {
            //保存当前绑定的设备
            referDeviceList.forEach(energyProjectItemParam -> {
                EnergyProjectItem energyProjectItem = ObjectUtils.copying(energyProjectItemParam, EnergyProjectItem.class);
                //设置分项类型
                energyProjectItem.setBindType(energyProjectItemParam.getBindType().name());
                //设置分项id
                energyProjectItem.setProjectId(id);
                energyProjectItemMapper.insert(energyProjectItem);
            });
        }
    }

    @Override
    public List<EnergyProjectItemVO> getProjectReferDevice(Long id) {
        List<EnergyProjectItem> energyProjectItems = energyProjectItemMapper.selectList(Wrappers.<EnergyProjectItem>lambdaQuery()
                                                                                                .eq(EnergyProjectItem::getProjectId, id));
        return ObjectUtils.copying(energyProjectItems, EnergyProjectItemVO.class);
    }

    @Override
    public List<DeviceInEnergyProjectVO> getAllDeviceInProject(String projectId) {
        return this.baseMapper.getAllDeviceInProject(projectId);
    }
}