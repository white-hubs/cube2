package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcDeptMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcDeptPO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcDeptService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcStoreClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcDeptDTO;
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
 * 大白菜部门数据服务
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcDeptServiceImpl extends ServiceImpl<DbcDeptMapper, DbcDeptPO> implements DbcDeptService {

    private final DbcStoreClient dbcStoreClient;

    /**
     * 同步大白菜部门信息
     *
     * @param marketId    市场id 必须 （如果部门集合为空(null)会根据市场id查询部门集合，如果有部门集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param newTime     当前时间 必须
     * @param username    操作人 必须
     * @param deptDTOList 部门集合（如果此参数为空(null)会根据市场id查询部门集合，如果有部门集合就直接使用集合数据。）
     */
    @Override
    public Map<String, DbcDeptPO> dbcDeptPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                              String userId,
                                              ActorTypeEnum userType,
                                              List<DbcDeptDTO> deptDTOList
                                             ) {
        // 现有的数据
        Map<String, DbcDeptPO> oldMap = new HashMap<>();
        List<DbcDeptPO> oldList = this.list();
        if (oldList != null && !oldList.isEmpty()) {
            for (DbcDeptPO po : oldList) {
                if (StringUtils.isBlank(po.getDepartmentCode())) {
                    continue;
                }
                oldMap.put(po.getDepartmentCode(), po);
            }
        }

        // 大白菜获取到的部门数据
        if (deptDTOList == null) {
            deptDTOList = dbcStoreClient.dbcDeptList(marketId);
        }
        if (deptDTOList == null || deptDTOList.isEmpty()) {
            return oldMap;
        }

        List<DbcDeptPO> insertPOList = new ArrayList<>();
        List<DbcDeptPO> updatePOList = new ArrayList<>();
        // 遍历处理获取到的数据
        for (DbcDeptDTO dbcDeptDTO : deptDTOList) {
            if (StringUtils.isBlank(dbcDeptDTO.getDepartmentCode())) {
                continue;
            }
            DbcDeptPO oldPO = oldMap.get(dbcDeptDTO.getDepartmentCode());
            if (oldPO != null) {
                oldPO.setDepartmentName(dbcDeptDTO.getDepartmentName());
                EntityAuditor.populateUpdateAudit(oldPO, username, userId, userType, newTime);
                updatePOList.add(oldPO);
            } else {
                DbcDeptPO deptPO = new DbcDeptPO(IdWorker.getId(), marketId, dbcDeptDTO.getDepartmentCode(), dbcDeptDTO.getDepartmentName(), null);
                deptPO.setDeleted(false);
                deptPO.setTenant(tenant);
                EntityAuditor.populateAudit(deptPO, username, userId, userType, newTime);
                insertPOList.add(deptPO);
            }
        }

        this.saveBatch(insertPOList, 1000);
        this.updateBatchById(updatePOList, 1000);

        return oldMap;
    }
}
