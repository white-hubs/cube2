package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcAreaMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcAreaPO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcAreaService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcStoreClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAreaDTO;
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
 * 大白菜区域服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcAreaServiceImpl extends ServiceImpl<DbcAreaMapper, DbcAreaPO> implements DbcAreaService {

    private final DbcStoreClient dbcStoreClient;

    /**
     * 同步大白菜区域信息
     *
     * @param marketId    市场id 必须 如果区域集合为空(null)会根据市场id查询区域集合，如果有区域集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param newTime     当前时间 必须
     * @param username    操作人 必须
     * @param areaDTOList 区域集合 如果此参数为空(null)会根据市场id查询区域集合，如果有区域集合就直接使用集合数据。）
     */
    @Override
    public Map<String, DbcAreaPO> dbcAreaPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                              String userId,
                                              ActorTypeEnum userType,
                                              List<DbcAreaDTO> areaDTOList
                                             ) {
        // 现有的数据
        Map<String, DbcAreaPO> oldMap = new HashMap<>();
        List<DbcAreaPO> oldList = this.list();
        if (oldList != null && !oldList.isEmpty()) {
            for (DbcAreaPO po : oldList) {
                if (StringUtils.isBlank(po.getAreaCode())) {
                    continue;
                }
                oldMap.put(po.getAreaCode(), po);
            }
        }

        // 大白菜获取到的区域数据
        if (areaDTOList == null) {
            areaDTOList = dbcStoreClient.dbcAreaList(marketId);
        }
        if (areaDTOList == null || areaDTOList.isEmpty()) {
            return oldMap;
        }

        List<DbcAreaPO> insertPOList = new ArrayList<>();
        List<DbcAreaPO> updatePOList = new ArrayList<>();
        // 遍历
        for (DbcAreaDTO dbcAreaDTO : areaDTOList) {
            if (StringUtils.isBlank(dbcAreaDTO.getAreaCode())) {
                continue;
            }
            DbcAreaPO oldPO = oldMap.get(dbcAreaDTO.getAreaCode());
            if (oldPO != null) {
                oldPO.setAreaName(dbcAreaDTO.getAreaName());
                EntityAuditor.populateAudit(oldPO, username, userId, userType, newTime);
                updatePOList.add(oldPO);
            } else {
                DbcAreaPO areaPO = new DbcAreaPO(IdWorker.getId(), marketId, dbcAreaDTO.getAreaCode(), dbcAreaDTO.getAreaName(), null);
                areaPO.setDeleted(false);
                areaPO.setTenant(tenant);
                EntityAuditor.populateAudit(areaPO, username, userId, userType, newTime);
                insertPOList.add(areaPO);
            }
        }

        this.saveBatch(insertPOList, 1000);
        this.updateBatchById(updatePOList, 1000);

        return oldMap;
    }
}
