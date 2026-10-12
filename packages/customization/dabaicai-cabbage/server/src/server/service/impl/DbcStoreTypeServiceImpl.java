package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.param.StallCategoryCreateBatchParam;
import com.guhecloud.cube.biz.mdata.client.param.StallCategoryCreateParam;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcStoreTypeMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStoreTypePO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcStoreTypeService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcStoreClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreTypeDTO;
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
 * 大白菜挡位类型服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcStoreTypeServiceImpl extends ServiceImpl<DbcStoreTypeMapper, DbcStoreTypePO> implements DbcStoreTypeService {

    private final StallClient stallClient;
    private final DbcStoreClient dbcStoreClient;

    /**
     * 同步大白菜挡位类型信息
     *
     * @param marketId         市场id 必须（如果挡位类型集合为空(null)会根据市场id查询挡位类型集合，如果有挡位类型集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param newTime          当前时间 必须
     * @param username         操作人 必须
     * @param userId           操作人 id
     * @param userType         操作人类型
     * @param storeTypeDTOList 挡位类型集合（如果此参数为空(null)会根据市场id查询挡位类型集合，如果有挡位类型集合就直接使用集合数据。）
     */
    @Override
    public Map<String, DbcStoreTypePO> dbcStoreTypePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                                        String userId,
                                                        ActorTypeEnum userType,
                                                        List<DbcStoreTypeDTO> storeTypeDTOList
                                                       ) {
        // (源数据)原来的挡位类型map
        Map<String, DbcStoreTypePO> oldStoreTypeMap = new HashMap<>();
        List<DbcStoreTypePO> oldStoreTypeList = this.list();
        if (oldStoreTypeList != null) {
            for (DbcStoreTypePO dbcStoreTypePO : oldStoreTypeList) {
                if (StringUtils.isNotBlank(dbcStoreTypePO.getStoreTypeCode())) {
                    oldStoreTypeMap.put(dbcStoreTypePO.getStoreTypeCode(), dbcStoreTypePO);
                }
            }
        }
        // 如果没有就去查询
        if (storeTypeDTOList == null && marketId != null) {
            storeTypeDTOList = dbcStoreClient.dbcStoreTypeList(marketId);
        }
        if (storeTypeDTOList == null || storeTypeDTOList.isEmpty()) {
            return oldStoreTypeMap;
        }

        // 源数据
        Map<String, DbcStoreTypePO> reultMap = new HashMap<>();
        List<DbcStoreTypePO> typeInsertList = new ArrayList<>();
        List<DbcStoreTypePO> typeUpdateList = new ArrayList<>();
        // 已同步数据
        List<StallCategoryCreateParam> categoryCreateParamList = new ArrayList<>();
        // 遍历获取到的数据，组装插入或修改的数据
        for (DbcStoreTypeDTO typeDTO : storeTypeDTOList) {
            Long stallId = IdWorker.getId();
            DbcStoreTypePO oldStoreType = oldStoreTypeMap.get(typeDTO.getStorefrontTypeCode());
            if (oldStoreType == null) {
                oldStoreType = new DbcStoreTypePO(IdWorker.getId(), marketId, typeDTO.getStorefrontTypeCode(), typeDTO.getStorefrontTypeName(), stallId);
                oldStoreType.setDeleted(false);
                oldStoreType.setTenant(tenant);
                EntityAuditor.populateAudit(oldStoreType, username, userId, userType, newTime);
                typeInsertList.add(oldStoreType);
            } else {
                oldStoreType.setStoreTypeName(typeDTO.getStorefrontTypeName());
                EntityAuditor.populateUpdateAudit(oldStoreType, username, userId, userType, newTime);
                typeUpdateList.add(oldStoreType);
            }
            /* 这里全部添加到新增是因为client的批量添加接口是做了名称重复就不添加的 */
            StallCategoryCreateParam createParam = new StallCategoryCreateParam();
            createParam.setId(stallId);
            createParam.setName(typeDTO.getStorefrontTypeName());
            createParam.setEnabled(true);
            categoryCreateParamList.add(createParam);
            reultMap.put(oldStoreType.getStoreTypeCode(), oldStoreType);
        }
        if (!typeInsertList.isEmpty()) {
            this.saveBatch(typeInsertList, 1000);
        }
        if (!typeUpdateList.isEmpty()) {
            this.updateBatchById(typeUpdateList, 1000);
        }
        // (在古河云)添加新的挡位类型
        if (!categoryCreateParamList.isEmpty()) {
            StallCategoryCreateBatchParam param = new StallCategoryCreateBatchParam();
            param.setCategoryList(categoryCreateParamList);
            stallClient.saveBathOnlyEntity(param);
        }
        return reultMap;
    }
}
