package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.DbcStallDTO;
import com.guhecloud.cube.biz.mdata.client.param.StallCreateBathParam;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcAreaMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcDeptMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcStoreMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcStoreTypeMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcAreaPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcDeptPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStorePO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStoreTypePO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcStoreService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcStoreClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreDTO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大白菜挡位服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcStoreServiceImpl extends ServiceImpl<DbcStoreMapper, DbcStorePO> implements DbcStoreService {

    private final DbcStoreClient dbcStoreClient;
    private final StallClient stallClient;
    private final DbcDeptMapper dbcDeptMapper;
    private final DbcAreaMapper dbcAreaMapper;
    private final DbcStoreTypeMapper dbcStoreTypeMapper;

    /**
     * 同步大白菜挡位信息
     *
     * @param marketId        市场id 必须（如果挡位集合为空(null)会根据市场id查询挡位集合，如果有挡位集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param newTime         当前时间 必须
     * @param username        操作人 必须
     * @param storeDTOList    挡位集合（如果此参数为空(null)会根据市场id查询挡位集合，如果有挡位集合就直接使用集合数据。）
     * @param oldDeptMap      映射的部门数据(为空(null)自己查询)
     * @param oldAreaMap      映射的区域数据(为空(null)就自己查询)
     * @param oldStoreTypeMap 挡位类型map(为空(null)就自己查询)
     */
    @Override
    public void dbcStorePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username, List<DbcStoreDTO> storeDTOList,
                             Map<String, DbcDeptPO> oldDeptMap, Map<String, DbcAreaPO> oldAreaMap,
                             Map<String, DbcStoreTypePO> oldStoreTypeMap
                            ) {
        if (storeDTOList == null) {
            storeDTOList = dbcStoreClient.dbcStoreList(marketId);
        }
        if (storeDTOList == null || storeDTOList.isEmpty()) {
            return;
        }
        // 去重
        Map<String, DbcStoreDTO> httpMap = new HashMap<>();
        for (DbcStoreDTO dbcStoreDTO : storeDTOList) {
            httpMap.put(dbcStoreDTO.getStorefrontCode(), dbcStoreDTO);
        }
        // (大白菜源数据)原来的挡位数据
        Map<String, DbcStorePO> oldStoreMap = new HashMap<>();
        List<DbcStorePO> oldStoreList = this.list();
        if (oldStoreList != null && !oldStoreList.isEmpty()) {
            for (DbcStorePO dbcStorePO : oldStoreList) {
                if (StringUtils.isNotBlank(dbcStorePO.getStorefrontCode())) {
                    oldStoreMap.put(dbcStorePO.getStorefrontCode(), dbcStorePO);
                }
            }
        }
        // 古河云已有挡位Map
        Map<String, DbcStallDTO> oldStallMap = new HashMap<>();
        List<DbcStallDTO> oldStallList = stallClient.getAllStallEntity();
        if (oldStoreList != null) {
            for (DbcStallDTO stallDTO : oldStallList) {
                oldStallMap.put(stallDTO.getCode(), stallDTO);
            }
        }
        // 部门映射map
        if (oldDeptMap == null) {
            oldDeptMap = new HashMap<>();
            List<DbcDeptPO> oldPOList = dbcDeptMapper.selectList(Wrappers.emptyWrapper());
            if (oldPOList != null) {
                for (DbcDeptPO po : oldPOList) {
                    oldDeptMap.put(po.getDepartmentCode(), po);
                }
            }
        }
        // 区域映射map
        if (oldAreaMap == null) {
            oldAreaMap = new HashMap<>();
            List<DbcAreaPO> oldPOList = dbcAreaMapper.selectList(Wrappers.emptyWrapper());
            if (oldPOList != null) {
                for (DbcAreaPO dbcAreaPO : oldPOList) {
                    oldAreaMap.put(dbcAreaPO.getAreaCode(), dbcAreaPO);
                }
            }
        }
        // 挡位类型数据
        if (oldStoreTypeMap == null) {
            oldStoreTypeMap = new HashMap<>();
            List<DbcStoreTypePO> oldPOList = dbcStoreTypeMapper.selectList(Wrappers.emptyWrapper());
            if (oldPOList != null) {
                for (DbcStoreTypePO dbcStoreTypePO : oldPOList) {
                    oldStoreTypeMap.put(dbcStoreTypePO.getStoreTypeCode(), dbcStoreTypePO);
                }
            }
        }
        List<DbcStorePO> insertStoreList = new ArrayList<>();
        List<DbcStorePO> updateStoreList = new ArrayList<>();
        List<DbcStallDTO> paramList = new ArrayList<>();
        // 遍历获取到的数据
        for (DbcStoreDTO dbcStoreDTO : httpMap.values()) {
            DbcStorePO oldStore = oldStoreMap.get(dbcStoreDTO.getStorefrontCode());
            // 要填入的 部门 区域 挡位类型
            String organizationIds = null;
            String areaIds = null;
            Long stallCategoryId = null;
            DbcDeptPO deptOrganizationPO = oldDeptMap.get(dbcStoreDTO.getDepartmentCode());
            if (deptOrganizationPO != null) {
                organizationIds = deptOrganizationPO.getOrganizationIds();
            }
            DbcAreaPO areaPO = oldAreaMap.get(dbcStoreDTO.getAreaCode());
            if (areaPO != null) {
                areaIds = areaPO.getAreaIds();
            }
            DbcStoreTypePO storeTypePO = oldStoreTypeMap.get(dbcStoreDTO.getStorefrontTypeCode());
            if (storeTypePO != null) {
                stallCategoryId = storeTypePO.getStallCategoryId();
            }
            if (oldStore == null) {
                oldStore = new DbcStorePO(IdWorker.getId(), dbcStoreDTO.getStorefrontCode(), dbcStoreDTO.getStorefrontName(), dbcStoreDTO.getAlias(), dbcStoreDTO.getAreaCode(), dbcStoreDTO.getAreaName(), dbcStoreDTO.getStorefrontTypeCode(), dbcStoreDTO.getStorefrontTypeName(), dbcStoreDTO.getDepartmentCode(), dbcStoreDTO.getDepartmentName(), dbcStoreDTO.getCustomerName(), dbcStoreDTO.getCustomerMobile(), dbcStoreDTO.getMarketId(), dbcStoreDTO.getMarketName(), organizationIds, areaIds, stallCategoryId);
                insertStoreList.add(oldStore);
            } else {
                oldStore.setAlias(dbcStoreDTO.getAlias());
                oldStore.setAreaCode(dbcStoreDTO.getAreaCode());
                oldStore.setAreaIds(areaIds);
                oldStore.setCustomerName(dbcStoreDTO.getCustomerName());
                oldStore.setAreaName(dbcStoreDTO.getAreaName());
                oldStore.setCustomerMobile(dbcStoreDTO.getCustomerMobile());
                oldStore.setDepartmentCode(dbcStoreDTO.getDepartmentCode());
                oldStore.setDepartmentName(dbcStoreDTO.getDepartmentName());
                oldStore.setMarketId(dbcStoreDTO.getMarketId());
                oldStore.setMarketName(dbcStoreDTO.getMarketName());
                oldStore.setStorefrontCode(dbcStoreDTO.getStorefrontCode());
                oldStore.setOrganizationIds(organizationIds);
                oldStore.setAlias(dbcStoreDTO.getAlias());
                updateStoreList.add(oldStore);
            }
            // 更新已同步到古河云的挡位
            DbcStallDTO oldStall = oldStallMap.get(dbcStoreDTO.getStorefrontCode());
            if (oldStall == null) {
                oldStall = new DbcStallDTO();
                oldStall.setId(IdWorker.getId());

                oldStall.setDeleted(false);
                oldStall.setTenant(tenant);
                oldStall.setCreateTime(newTime);
                oldStall.setCreateBy(username);
            }
            oldStall.setArea(areaIds);
            oldStall.setCode(dbcStoreDTO.getStorefrontCode());
            oldStall.setEnabled(true);
            oldStall.setName(dbcStoreDTO.getStorefrontName());
            oldStall.setDeptId(organizationIds);
            oldStall.setType(stallCategoryId == null ? null : String.valueOf(stallCategoryId));
            oldStall.setMasterName(dbcStoreDTO.getCustomerName());
            oldStall.setMasterPhone(dbcStoreDTO.getCustomerMobile());

            oldStall.setUpdateTime(newTime);
            oldStall.setUpdateBy(username);
            paramList.add(oldStall);
        }
        // 更新源数据
        if (!insertStoreList.isEmpty()) {
            this.saveBatch(insertStoreList, 1000);
        }
        if (!updateStoreList.isEmpty()) {
            this.updateBatchById(updateStoreList, 1000);
        }
        if (paramList.isEmpty()) {
            return;
        }
        // 更新已经同步到古河云的数据
        StallCreateBathParam param = new StallCreateBathParam();
        param.setStallList(paramList);
        stallClient.saveOrUpdateBatch(param);
    }
}
