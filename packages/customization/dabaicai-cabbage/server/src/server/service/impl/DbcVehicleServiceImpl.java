package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.client.VehicleClient;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.VehicleInfoBatchParam;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.VehicleInfoVO;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcVehicleMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcVehiclePO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcVehicleService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcVehicleClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcVehicleDTO;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大白菜车辆服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcVehicleServiceImpl extends ServiceImpl<DbcVehicleMapper, DbcVehiclePO> implements DbcVehicleService {

    private final DbcVehicleClient dbcVehicleClient;
    private final VehicleClient vehicleClient;

    /**
     * 同步大白菜车辆信息
     *
     * @param marketId       市场 必须（如果车辆集合为空(null)会根据市场id查询车辆集合，如果有车辆集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param newTime        当前时间 必须
     * @param username       操作人 必须
     * @param vehicleDTOList 车辆集合（如果此参数为空(null)会根据市场id查询车辆集合，如果有车辆集合就直接使用集合数据。）
     */
    @Override
    public void dbcVehiclePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                               String userId, ActorTypeEnum userType, List<DbcVehicleDTO> vehicleDTOList
                              ) {
        if (vehicleDTOList == null) {
            vehicleDTOList = dbcVehicleClient.dbcVehicleList(marketId);
        }
        if (vehicleDTOList == null || vehicleDTOList.isEmpty()) {
            return;
        }
        Map<String, DbcVehicleDTO> httpMap = new HashMap<>();
        for (DbcVehicleDTO dbcVehicleDTO : vehicleDTOList) {
            if (StringUtils.isBlank(dbcVehicleDTO.getVehicleNumber())) {
                continue;
            }
            httpMap.put(dbcVehicleDTO.getVehicleNumber(), dbcVehicleDTO);
        }
        // (源数据)大白菜车辆数据map
        Map<String, DbcVehiclePO> dbcVehicleMap = new HashMap<>();
        List<DbcVehiclePO> dbcOldVehicleList = this.list();
        if (dbcOldVehicleList != null) {
            for (DbcVehiclePO dbcVehiclePO : dbcOldVehicleList) {
                dbcVehicleMap.put(dbcVehiclePO.getVehicleNumber(), dbcVehiclePO);
            }
        }

        Map<String, VehicleInfoVO> oldVehicleInfoMap = new HashMap<>();
        List<VehicleInfoVO> vehicleInfoVOList = vehicleClient.getAllVehicleEntity();
        if (vehicleInfoVOList != null) {
            for (VehicleInfoVO vehicleInfoVO : vehicleInfoVOList) {
                oldVehicleInfoMap.put(vehicleInfoVO.getVehicleNo(), vehicleInfoVO);
            }
        }

        List<DbcVehiclePO> dbcInsertList = new ArrayList<>();
        List<DbcVehiclePO> dbcUpdateList = new ArrayList<>();
        List<VehicleInfoVO> paramList = new ArrayList<>();
        // 遍历获取到的数据
        for (DbcVehicleDTO dbcVehicleDTO : httpMap.values()) {
            dbcVehicleDTO.setMarketId(marketId);
            // 处理大白菜源数据
            DbcVehiclePO dbcOldPO = dbcVehicleMap.get(dbcVehicleDTO.getVehicleNumber());
            Long vehicleId = IdWorker.getId();
            if (dbcOldPO == null) {
                dbcOldPO = new DbcVehiclePO(IdWorker.getId(), dbcVehicleDTO.getVehicleNumber(), dbcVehicleDTO.getVehicleTypeCode(), dbcVehicleDTO.getVehicleTypeName(), dbcVehicleDTO.getCustomerName(), dbcVehicleDTO.getCustomerMobile(), dbcVehicleDTO.getMarketId(), dbcVehicleDTO.getMarketName(), vehicleId);
                dbcOldPO.setDeleted(false);
                dbcOldPO.setTenant(tenant);
                EntityAuditor.populateAudit(dbcOldPO, username, userId, userType, newTime);
                dbcInsertList.add(dbcOldPO);
            } else {
                dbcOldPO.setVehicleTypeName(dbcVehicleDTO.getVehicleTypeName());
                dbcOldPO.setCustomerMobile(dbcVehicleDTO.getCustomerMobile());
                dbcOldPO.setCustomerName(dbcVehicleDTO.getCustomerName());
                dbcOldPO.setMarketId(dbcVehicleDTO.getMarketId());
                dbcOldPO.setVehicleTypeCode(dbcVehicleDTO.getVehicleTypeCode());
                dbcOldPO.setMarketName(dbcVehicleDTO.getMarketName());
                EntityAuditor.populateUpdateAudit(dbcOldPO, username, userId, userType, newTime);
                dbcUpdateList.add(dbcOldPO);
            }
            // 处理古河云已经同步的数据
            VehicleInfoVO vehicleInfoVO = oldVehicleInfoMap.get(dbcVehicleDTO.getVehicleNumber());
            if (vehicleInfoVO == null) {
                vehicleInfoVO = new VehicleInfoVO();
                vehicleInfoVO.setId(IdWorker.getId());
                vehicleInfoVO.setTenant(tenant);
                vehicleInfoVO.setDeleted(false);
                vehicleInfoVO.setCreateBy(username);
                vehicleInfoVO.setCreateTime(newTime);
            }
            vehicleInfoVO.setVehicleNo(dbcVehicleDTO.getVehicleNumber());
            vehicleInfoVO.setVehicleTypeCode(dbcVehicleDTO.getVehicleTypeCode());
            vehicleInfoVO.setVehicleTypeName(dbcVehicleDTO.getVehicleTypeName());
            vehicleInfoVO.setCustomerName(dbcVehicleDTO.getCustomerName());
            vehicleInfoVO.setCustomerMobile(dbcVehicleDTO.getCustomerMobile());

            vehicleInfoVO.setUpdateBy(username);
            vehicleInfoVO.setUpdateTime(newTime);
            paramList.add(vehicleInfoVO);
        }
        // 更新大白菜源数据
        if (!dbcInsertList.isEmpty()) {
            this.saveBatch(dbcInsertList, 1000);
        }
        if (!dbcUpdateList.isEmpty()) {
            this.updateBatchById(dbcUpdateList, 1000);
        }
        // 更新古河云已同步的数据
        if (!paramList.isEmpty()) {
            VehicleInfoBatchParam param = new VehicleInfoBatchParam();
            param.setVehicleInfoList(paramList);
            vehicleClient.saveOrUpdateBatch(param);
        }

    }
}
