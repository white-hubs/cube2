package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.AcreageDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.HistoryArea;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.mapper.MerchantAreaMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.SpaceMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.AreaDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.BimInfoDTO;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantMerchantAreaServiceImpl extends ServiceImpl<MerchantAreaMapper, AreaDeliver> implements MerchantAreaService {

    private final MerchantAreaMapper areaMapper;
    private final SpaceMapper spaceMapper;
    //    private final BimInfoService bimInfoService;
    //    private final HistoryAreaService historyAreaService;
    //    private final BimModelApi bimModelApi;
    //    private final BuildingApi buildingApi;

    @Override
    public AreaDTO extendAreaDtoBimInfo(AreaDTO dto) {
        //        BimInfo bimInfo = bimInfoService.lambdaQuery().eq(BimInfo::getBusinessType, AreaService.BUSINESS_TYPE).eq(BimInfo::getBusinessKey, dto.getId()).one();
        //        if (bimInfo != null) {
        //            BimInfoDTO bimInfoDTO = new BimInfoDTO(bimInfo);
        // specialty 楼栋名称
        //            if (StringUtils.isNotEmpty(bimInfo.getSpecialty())) {
        //                BuildingDTO buildingDTO = buildingApi.get(bimInfo.getSpecialty(), dto.getProjectId());
        //                if (buildingDTO != null) {
        // 楼层和片区类型的区域，获取楼栋模型
        // 获取楼栋模型：bimmodel.buildingId = building.id
        //                    if (AreaService.LEVEL_BUILDINGS_FLOOR.equals(dto.getLevel()) || AreaService.LEVEL_BUILDINGS_SLICE.equals(dto.getLevel())) {
        //                        BimModelDTO bimModelDTO = bimModelApi.getByBuildingId(buildingDTO.getId());
        //                        if (bimModelDTO != null) {
        //                            bimInfoDTO.setModelLoadPath(bimModelDTO.getLoadPath());
        //                        }
        //                    }
        // 其它类型的区域，获取园区模型
        // 获取园区模型: bimmodel.modelFileId = building.fileId
        //                    else {
        //                        BimModelDTO bimModelDTO = bimModelApi.getByFileId(buildingDTO.getFileId());
        //                        if (bimModelDTO != null) {
        //                            bimInfoDTO.setModelLoadPath(bimModelDTO.getLoadPath());
        //                        }
        //                    }
        //                }
        //            }
        //            dto.setBimInfo(bimInfoDTO);
        //        }
        return dto;
    }

    @Override
    public void save(AreaDTO areaDTO) {
        //        Area saved = areaMapper.selectOne(Wrappers.<Area>lambdaQuery().eq(Area::getProjectId, areaDTO.getProjectId()).eq(Area::getName, areaDTO.getName()).eq(Area::getType, areaDTO.getType()).eq(Area::getLevel, areaDTO.getLevel()));
        //        if (saved != null) {
        //            throw new ServiceException("区域名已经存在");
        //        }
        areaDTO.setCode(BizCodeUtils.generate("AREA"));
        AreaDeliver area = areaDTO.convert();
        areaMapper.insert(area);
        // 保存bim绑定信息
        this.saveBimInfo(area.getId(), areaDTO.getProjectId(), areaDTO.getBimInfo());
        // 保存历史面积
        this.saveHistoryAcreage(area, true);
    }

    @Override
    public void update(AreaDTO areaDTO) {
        AreaDeliver saved = areaMapper.selectById(areaDTO.getId());
        if (null == saved) {
            throw new ServiceException("区域不存在");
        }
/*        Area savedName = areaMapper.selectOne(Wrappers.<Area>lambdaQuery().eq(Area::getProjectId, areaDTO.getProjectId()).eq(Area::getName, areaDTO.getName()).eq(Area::getType, areaDTO.getType()).eq(Area::getLevel, areaDTO.getLevel()));
        if (savedName != null && !savedName.getId().equals(areaDTO.getId())) {
            throw new ServiceException("名称已经存在");
        }*/
        areaMapper.updateById(areaDTO.convert());
        // 保存bim绑定信息
        this.saveBimInfo(saved.getId(), areaDTO.getProjectId(), areaDTO.getBimInfo());
        // 保存历史面积
        this.updateHistoryAreas(areaDTO, saved);
    }

    @Override
    public void remove(String id) {
        AreaDeliver area = areaMapper.selectById(id);
        if (null == area) {
            throw new ServiceException("区域ID不存在");
        }
        if (areaMapper.selectList(Wrappers.<AreaDeliver>lambdaQuery().eq(AreaDeliver::getPid, id)).size() > 0) {
            throw new ServiceException("该区域存在子区域不能删除");
        }
        // TODO 关联关系的校验，记得随时想起来了就添加 设备，工单
        // 与空间的关联
        if (spaceMapper.selectList(Wrappers.<Space>lambdaQuery().eq(Space::getAreaId1, id)).size() > 0) {
            throw new ServiceException("该区域被空间绑定了，不能删除");
        }
        if (spaceMapper.selectList(Wrappers.<Space>lambdaQuery().eq(Space::getAreaId2, id)).size() > 0) {
            throw new ServiceException("该区域被空间绑定了，不能删除");
        }
        if (spaceMapper.selectList(Wrappers.<Space>lambdaQuery().eq(Space::getAreaId3, id)).size() > 0) {
            throw new ServiceException("该区域被空间绑定了，不能删除");
        }
        // 与设备的关联
        /*if (deviceService.lambdaQuery().eq(Device::getAreaId, id).list().size() > 0) {
            throw new ServiceException("该区域被设备绑定了，不能删除");
        }*/
        // 与巡检点的关联 TODO
        //        if (insppointApi.getByAreaId(id)) {
        //            throw new ServiceException("该区域被巡检点绑定了，不能删除");
        //        }
        // 与黑名单报警的关联 TODO
        areaMapper.deleteById(id);

        //        bimInfoService.remove(BUSINESS_TYPE, id);
        //        historyAreaService.remove(Wrappers.<HistoryArea>lambdaQuery().eq(HistoryArea::getObjectType, BUSINESS_TYPE).eq(HistoryArea::getObjectId, id));
    }

    @Override
    public List<String> parseNames(List<String> areaIds) {
        List<String> names = new ArrayList<>();
        for (String areaId : areaIds) {
            AreaDeliver area = areaMapper.selectById(areaId);
            if (area != null) {
                names.add(area.getName());
            }
        }
        return names;
    }

    @Override
    public List<String> parsePath(String areaId) {
        List<String> areas = new ArrayList<>();
        AreaDeliver area = areaMapper.selectById(areaId);
        if (area == null) {
            areas.add("");
            areas.add("");
            areas.add("");
        } else if (StringUtils.isEmpty(area.getPid())) {
            areas.add(areaId);
            areas.add("");
            areas.add("");
        } else {
            AreaDeliver areaPar = areaMapper.selectById(area.getPid());
            if (StringUtils.isEmpty(areaPar.getPid())) {
                areas.add(areaPar.getId());
                areas.add(areaId);
                areas.add("");
            } else {
                areas.add(areaPar.getPid());
                areas.add(areaPar.getId());
                areas.add(areaId);
            }
        }
        return areas;
    }

    @Override
    public void updateAcreage(String areaId, AcreageDTO acreageDTO) {
        AreaDeliver area = areaMapper.selectById(areaId);
        if (null == area) {
            return;
        }
        if (MerchantAreaService.LEVEL_BUILDINGS_FLOOR.equals(area.getLevel()) ||
            MerchantAreaService.LEVEL_OUTDOOR_SECOND.equals(area.getLevel())) {
            area = this.updateAllAcreages(area, acreageDTO);
            AreaDeliver parentArea = areaMapper.selectById(area.getPid());
            this.updateAllAcreages(parentArea, acreageDTO);
        }
    }

    // 修改所有类型的面积
    private AreaDeliver updateAllAcreages(AreaDeliver area, AcreageDTO acreageDTO) {
        boolean flag = false;
        if (acreageDTO.getLeaseArea() != 0) {
            area.setLeaseArea(area.getLeaseArea() + acreageDTO.getLeaseArea());
            flag = true;
        }
        if (acreageDTO.getLeaseRealArea() != 0) {
            area.setLeaseRealArea(area.getLeaseRealArea() + acreageDTO.getLeaseRealArea());
            flag = true;
        }
        if (acreageDTO.getLeasePublicArea() != 0) {
            area.setLeasePublicArea(area.getLeasePublicArea() + acreageDTO.getLeasePublicArea());
            flag = true;
        }
        if (acreageDTO.getFireArea() != 0) {
            area.setFireArea(area.getFireArea() + acreageDTO.getFireArea());
            flag = true;
        }
        if (acreageDTO.getCleanArea() != 0) {
            area.setCleanArea(area.getCleanArea() + acreageDTO.getCleanArea());
            flag = true;
        }
        if (acreageDTO.getParkingArea() != 0) {
            area.setParkingArea(area.getParkingArea() + acreageDTO.getParkingArea());
            flag = true;
        }
        areaMapper.updateById(area);
        this.saveHistoryAcreage(area, flag);
        return area;
    }

    private void saveBimInfo(String id, String projectId, BimInfoDTO bimInfoDTO) {
        if (bimInfoDTO != null) {
            bimInfoDTO.setProjectId(projectId);
            bimInfoDTO.setBusinessType(BUSINESS_TYPE);
            bimInfoDTO.setBusinessKey(id);
            //        	bimInfoService.saveOrUpdate(bimInfoDTO);
        }
    }

    private void saveHistoryAcreage(AreaDeliver areaDeliver, boolean flag) {
        if (!flag) {
            return;
        }
        HistoryArea historyArea = new HistoryArea();
        BeanUtils.copyProperties(areaDeliver, historyArea);
        historyArea.setId("");
        historyArea.setObjectType(BUSINESS_TYPE);
        historyArea.setObjectId(areaDeliver.getId());
        historyArea.setProjectId(areaDeliver.getProjectId());
        historyArea.setCreateBy(null);
        historyArea.setCreateTime(null);
        //        historyAreaService.save(historyArea);
    }

    private void updateHistoryAreas(AreaDTO areaDTO, AreaDeliver saved) {
        AcreageDTO oldAreas = new AcreageDTO();
        BeanUtils.copyProperties(saved, oldAreas);
        AcreageDTO newAreas = new AcreageDTO();
        BeanUtils.copyProperties(areaDTO, newAreas);
        if (!oldAreas.equals(newAreas)) {
            this.saveHistoryAcreage(areaDTO.convert(), true);
        }
    }

}
