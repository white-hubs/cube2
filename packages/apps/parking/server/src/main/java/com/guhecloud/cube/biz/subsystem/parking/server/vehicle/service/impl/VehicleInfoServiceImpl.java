package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.converts.VehicleInfoConverts;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.dao.VehicleInfoMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleTypePO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoBatchParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleInfoService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleTypeService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 车辆服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleInfoServiceImpl extends ServiceImpl<VehicleInfoMapper, VehicleInfo> implements VehicleInfoService {

    private final FileClient fileClient;

    private final FileRefClient fileRefClient;
    private final VehicleTypeService vehicleTypeService;

    private static final String VEHICLE_FILE_SCENE_CODE = "PARKING::VEHICLE";

    /**
     * 车辆分页列表
     *
     * @return 分页
     */
    @Override
    public Pagination<VehicleInfoVO> getPage(VehicleInfoQueryParam param) {
        Page<VehicleInfo> page = this.page(PagingUtils.convert(param), Wrappers.<VehicleInfo>lambdaQuery()
                                                   .eq(param.getId() != null, VehicleInfo::getId, param.getId())
                                                   .eq(Objects.nonNull(param.getInBlackList()), VehicleInfo::getIsBlacklist, param.getInBlackList())
                                                   .in(param.getIdList() != null, VehicleInfo::getId, param.getIdList())
                                                   .and(StringUtils.isNotEmpty(param.getVehicleNo()), q ->
                                                           q.like(VehicleInfo::getVehicleNo, param.getVehicleNo())
                                                                   .or().like(VehicleInfo::getCustomerName, param.getVehicleNo()))
                                                   .eq(StringUtils.isNotBlank(param.getVehicleTypeCode()), VehicleInfo::getVehicleTypeCode, param.getVehicleTypeCode())
                                                   .like(StringUtils.isNotBlank(param.getVehicleTypeName()), VehicleInfo::getVehicleTypeName, param.getVehicleTypeName())
                                                   .like(StringUtils.isNotBlank(param.getCustomerMobile()), VehicleInfo::getCustomerMobile, param.getCustomerMobile())
                                          );
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        Pagination<VehicleInfoVO> result = PagingUtils.convert(page, VehicleInfoVO.class);
        Set<Long> typeIds = result.getRecords().stream().filter(t -> StringUtils.isNotBlank(t.getVehicleTypeCode()))
                .map(t -> Long.valueOf(t.getVehicleTypeCode()))
                .collect(Collectors.toSet());
        Map<Long, String> typeMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(typeIds)) {
            typeMap = vehicleTypeService.lambdaQuery()
                    .in(VehicleTypePO::getId, typeIds)
                    .list()
                    .stream().collect(Collectors.toMap(VehicleTypePO::getId, VehicleTypePO::getName));
        }
        for (VehicleInfoVO record : result.getRecords()) {
            if (StringUtils.isNotBlank(record.getVehicleTypeCode())) {
                record.setVehicleTypeName(typeMap.get(Long.valueOf(record.getVehicleTypeCode())));
            }
        }
        return result;
    }

    /**
     * 获取全部车辆信息(完整PO信息）
     *
     * @return 车辆集合
     */
    @Override
    public List<VehicleInfoVO> getAllVehicleEntity() {
        return ConvertUtils.toList(VehicleInfoVO.class, this.list());
    }

    /**
     * 车辆批量更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatch(VehicleInfoBatchParam param) {
        if (param == null || param.getVehicleInfoList() == null || param.getVehicleInfoList().isEmpty()) {
            return;
        }
        this.saveOrUpdateBatch(ConvertUtils.toList(VehicleInfo.class, param.getVehicleInfoList()), 1000);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(VehicleInfoVO param) {
        Long count = this.lambdaQuery().eq(VehicleInfo::getVehicleNo, param.getVehicleNo())
                .count();
        VehicleInfo vehicleInfo;
        if (Objects.nonNull(param.getId())) {
            vehicleInfo = this.getById(param.getId());
            Assert.isParameterError((!StringUtils.equals(vehicleInfo.getVehicleNo(), param.getVehicleNo()))
                                    && count > 0, "车牌号已存在");
            buildEntityInfo(param, vehicleInfo);
            return this.updateById(vehicleInfo);
        } else {
            Assert.isParameterError(count > 0, "车牌号已存在");
            vehicleInfo = new VehicleInfo();
            vehicleInfo.setId(IdWorker.getId());
            vehicleInfo.setIsBlacklist(Objects.nonNull(param.getIsBlacklist()) ? param.getIsBlacklist() : false);
            buildEntityInfo(param, vehicleInfo);
            return this.save(vehicleInfo);
        }

    }

    private void buildEntityInfo(VehicleInfoVO param, VehicleInfo vehicleInfo) {
        VehicleInfoConverts.convert(param, vehicleInfo);
        //保存文件信息
        fileRefClient.removeByBusinessKey(vehicleInfo.getId());
        if (CollectionUtils.isNotEmpty(param.getFileIdList())) {
            FileRefCreateParam fileParam = new FileRefCreateParam();
            fileParam.setFileIdList(param.getFileIdList());
            fileParam.setBusinessKey(vehicleInfo.getId());
            fileParam.setSceneCode(VEHICLE_FILE_SCENE_CODE);
            fileRefClient.refInduce(fileParam);
        }
    }

    @Override
    public VehicleInfoDetailVO getVehicleInfo(Long id) {
        VehicleInfo vehicleInfo = this.getById(id);
        Assert.isNotFound(vehicleInfo, "车辆信息不存在");
        VehicleInfoDetailVO result = VehicleInfoConverts.convertToDetailVO(vehicleInfo);
        setExtendInfo(vehicleInfo, result);
        result.setFileVOList(fileClient.getByBusinessKey(vehicleInfo.getId()));
        return result;
    }

    @Override
    public VehicleInfoDetailVO getVehicleInfoByCode(String vehicleNo) {
        VehicleInfo vehicleInfo = this.lambdaQuery().eq(VehicleInfo::getVehicleNo, vehicleNo)
                .last("LIMIT 1")
                .one();
        if (Objects.isNull(vehicleInfo)) {
            VehicleInfoDetailVO vo = new VehicleInfoDetailVO();
            vo.setVehicleNo(vehicleNo);
            return vo;
        }
        VehicleInfoDetailVO result = VehicleInfoConverts.convertToDetailVO(vehicleInfo);
        setExtendInfo(vehicleInfo, result);
        result.setFileVOList(fileClient.getByBusinessKey(vehicleInfo.getId()));
        return result;
    }

    private void setExtendInfo(VehicleInfo vehicleInfo, VehicleInfoDetailVO result) {
        if (StringUtils.isNotBlank(vehicleInfo.getVehicleTypeCode())) {
            VehicleTypePO vehicleTypePO = vehicleTypeService.getById(Long.valueOf(vehicleInfo.getVehicleTypeCode()));
            if (Objects.nonNull(vehicleTypePO)) {
                result.setVehicleTypeName(vehicleTypePO.getName());
            }
        }
    }

    @Override
    public void saveNotExist(VehicleInfoVO param) {
        Long count = this.lambdaQuery().eq(VehicleInfo::getVehicleNo, param.getVehicleNo())
                .count();
        if (count > 0) {
            return;
        }
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setId(IdWorker.getId());
        vehicleInfo.setIsBlacklist(Objects.nonNull(param.getIsBlacklist()) ? param.getIsBlacklist() : false);
        buildEntityInfo(param, vehicleInfo);
        this.save(vehicleInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateVehicleNoColor(String vehicleNo, VehicleNoColorEnum vehicleNoColor) {
        if (StringUtils.isBlank(vehicleNo)) {
            return;
        }
        VehicleInfo existing = this.lambdaQuery().eq(VehicleInfo::getVehicleNo, vehicleNo)
                .last("LIMIT 1")
                .one();
        if (Objects.isNull(existing)) {
            // 不存在，新建车辆信息
            VehicleInfo vehicleInfo = new VehicleInfo();
            vehicleInfo.setId(IdWorker.getId());
            vehicleInfo.setVehicleNo(vehicleNo);
            vehicleInfo.setVehicleNoColor(vehicleNoColor);
            vehicleInfo.setIsBlacklist(false);
            this.save(vehicleInfo);
        } else if (Objects.isNull(existing.getVehicleNoColor())) {
            // 已存在且车牌颜色为空，更新车牌颜色
            existing.setVehicleNoColor(vehicleNoColor);
            this.updateById(existing);
        }
    }
}
