package com.guhecloud.cube.srv.gen.cabbage.server.job;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcMarketMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcAreaPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcDeptPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcMarketPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStoreTypePO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcAreaService;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcDeptService;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcGoodsCategoryService;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcStoreService;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcStoreTypeService;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcVehicleService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcStoreClient;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcVehicleClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAllStoreListDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAreaDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcDeptDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreTypeDTO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcVehicleDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 大白菜数据拉取定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcDataPullTask {

    private final DbcStoreClient dbcStoreClient;
    private final DbcDeptService dbcDeptService;
    private final DbcAreaService dbcAreaService;
    private final DbcStoreTypeService dbcStoreTypeService;
    private final DbcStoreService dbcStoreService;
    private final DbcVehicleClient dbcVehicleClient;
    private final DbcVehicleService dbcVehicleService;
    private final DbcMarketMapper dbcMarketMapper;
    private final DbcGoodsCategoryService dbcGoodsCategoryService;

    @JobAnnotation(name = "dbcDataPull", cron = "0 0 3 * * ?", desc = "大白菜同步数据")
    public void dbcDataPull(String param) {
        LambdaQueryWrapper<DbcMarketPO> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(param)) {
            try {
                var marketIds = param.split(",");
                var marketIdList = Arrays.stream(marketIds).map(Integer::valueOf).collect(Collectors.toList());
                wrapper.in(DbcMarketPO::getMarketId, marketIdList);
            } catch (Exception e) {
                throw new ServiceException("param is number or 'number,number' !");
            }
        }

        SecurityContextHolder.setPrincipal(Principal.builder().id(1L).account("1").name("system").admin(true).type(Principal.Type.CLIENT).build());
        SessionContextHolder.clearSessionContext();
        var dbcMarketPOList = dbcMarketMapper.selectList(wrapper);
        log.warn("大白菜市场数据：{}", JSON.toJSONString(dbcMarketPOList));

        if (dbcMarketPOList != null) {
            for (DbcMarketPO dbcMarketPO : dbcMarketPOList) {
                if (dbcMarketPO.getTenantMarketId() == null) {
                    continue;
                }
                SecurityContextHolder.setPrincipal(Principal.builder().id(1L).account("1").name("system").admin(true).type(Principal.Type.CLIENT).build());
                SessionContextHolder.requestTenant(dbcMarketPO.getTenantMarketId());
                // 同步商品品类
                this.pullDbcGoodsCategory(dbcMarketPO.getMarketId(), dbcMarketPO.getTenantMarketId());
                // 同步挡位和车辆(挡位类型,部门，区域)
                this.pullDbcStoreVehicle(dbcMarketPO.getMarketId(), dbcMarketPO.getTenantMarketId());
            }
        }
        SessionContextHolder.clearSessionContext();
    }

    /**
     * 同步大白菜商品品类
     *
     * @param marketId 市场id
     * @param tenant   租户
     */
    public void pullDbcGoodsCategory(Integer marketId, Integer tenant) {
        try {
            dbcGoodsCategoryService.dbcGoodsCategoryPull(marketId, tenant, LocalDateTime.now(), "1", null);
        } catch (Exception e) {
            log.error("商品品类同步出错！", e);
        }

    }

    /**
     * 同步大白菜挡位车辆
     *
     * @param marketId 市场id
     * @param tenant   租户
     */
    private void pullDbcStoreVehicle(Integer marketId, Integer tenant) {
        log.warn("大白菜开始获取数据：{},{}", marketId, tenant);
        /* 挡位的信息(包含 挡位类型，挡位，部门，区域四个信息) */
        /* 部门，区域做映射；挡位类型，挡位新增 */
        List<DbcDeptDTO> deptDTOList = null;
        List<DbcAreaDTO> areaDTOList = null;
        List<DbcStoreTypeDTO> storeTypeDTOList = null;
        List<DbcStoreDTO> storeDTOList = null;
        try {
            DbcAllStoreListDTO allStoreListDTO = dbcStoreClient.dabAllStoreDTOList(marketId);
            deptDTOList = allStoreListDTO.getDeptList();
            areaDTOList = allStoreListDTO.getAreaList();
            storeTypeDTOList = allStoreListDTO.getStoreTypeList();
            storeDTOList = allStoreListDTO.getStoreList();
        } catch (Exception e) {
            log.error("大白菜获取挡位数据失败：", e);
        }
        if (deptDTOList == null) {
            log.warn("大白菜开始同步部门数据：null");
        } else {
            log.warn("大白菜开始同步部门数据：{}", deptDTOList.size());
        }
        // 同步部门数据
        Map<String, DbcDeptPO> oldDeptMap;
        try {
            oldDeptMap = dbcDeptService.dbcDeptPull(marketId, tenant, LocalDateTime.now(), "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, deptDTOList);
        } catch (Exception e) {
            oldDeptMap = new HashMap<>();
            log.error("大白菜同步部门数据失败：", e);
        }
        if (areaDTOList == null) {
            log.warn("大白菜开始同步区域数据：null");
        } else {
            log.warn("大白菜开始同步区域数据：{}", areaDTOList.size());
        }
        // 同步区域数据
        Map<String, DbcAreaPO> oldAreaMap;
        try {
            oldAreaMap = dbcAreaService.dbcAreaPull(marketId, tenant, LocalDateTime.now(), "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, areaDTOList);
        } catch (Exception e) {
            oldAreaMap = new HashMap<>();
            log.error("大白菜同步区域数据失败：", e);
        }
        if (storeTypeDTOList == null) {
            log.warn("大白菜开始同步挡位类型数据：null");
        } else {
            log.warn("大白菜开始同步挡位类型数据：{}", storeTypeDTOList.size());
        }
        // 同步挡位类型数据
        Map<String, DbcStoreTypePO> oldStoreTypeMap;
        try {
            oldStoreTypeMap = dbcStoreTypeService.dbcStoreTypePull(marketId, tenant, LocalDateTime.now(), "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, storeTypeDTOList);
        } catch (Exception e) {
            oldStoreTypeMap = new HashMap<>();
            log.error("大白菜同步挡位类型失败：", e);
        }
        if (storeDTOList == null) {
            log.warn("大白菜开始同步挡位数据：null");
        } else {
            log.warn("大白菜开始同步挡位数据：{}", storeDTOList.size());
        }
        // 同步挡位数据
        try {
            dbcStoreService.dbcStorePull(marketId, tenant, LocalDateTime.now(), "1", storeDTOList, oldDeptMap, oldAreaMap, oldStoreTypeMap);
        } catch (Exception e) {
            log.error("大白菜同步挡位数据失败：", e);
        }

        log.warn("大白菜开始同步车辆数据");
        /* 车辆的信息 */
        try {
            List<DbcVehicleDTO> vehicleDTOList = dbcVehicleClient.dbcVehicleList(marketId);
            dbcVehicleService.dbcVehiclePull(marketId, tenant, LocalDateTime.now(), "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, vehicleDTOList);
        } catch (Exception e) {
            log.error("大白菜同步车辆数据失败：", e);
        }
    }

}
