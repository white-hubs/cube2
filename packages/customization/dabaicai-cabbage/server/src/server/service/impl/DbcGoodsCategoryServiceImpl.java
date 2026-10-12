package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.GoodsCategoryClient;
import com.guhecloud.cube.biz.mdata.client.dto.GoodsCategorySyncDTO;
import com.guhecloud.cube.biz.mdata.client.param.GoodsCategoryBatchRenewParam;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcGoodsCategoryMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcGoodsCategoryPO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcGoodsCategoryService;
import com.guhecloud.cube.srv.gen.cabbage.server.third.client.DbcGoodsCategoryClient;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcGoodsCategoryDTO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大白菜商品品类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcGoodsCategoryServiceImpl extends ServiceImpl<DbcGoodsCategoryMapper, DbcGoodsCategoryPO> implements DbcGoodsCategoryService {
    private final DbcGoodsCategoryClient dbcGoodsCategoryClient;
    private final GoodsCategoryClient goodsCategoryClient;

    /**
     * 同步大白菜商品品类信息
     *
     * @param marketId        市场 必须（如果商品品类集合为空(null)会根据市场id查询商品品类集合，如果有商品品类集合就直接使用集合数据，不会再用这个参数(marketId市场id)去查询。）
     * @param tenant          租户 必须
     * @param newTime         当前时间 必须
     * @param username        操作人 必须
     * @param categoryDTOList 商品品类集合（如果此参数为空(null)会根据市场id查询商品品类集合，如果有商品品类集合就直接使用集合数据。）
     */
    @Override
    public void dbcGoodsCategoryPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                     List<DbcGoodsCategoryDTO> categoryDTOList
                                    ) {
        if (marketId == null) {
            return;
        }
        if (categoryDTOList == null) {
            categoryDTOList = dbcGoodsCategoryClient.dbcGoodsCategoryList(marketId);
        }
        if (categoryDTOList == null || categoryDTOList.isEmpty()) {
            log.warn("获取到的品类为空！");
            return;
        }

        // 插入的同步记录
        List<DbcGoodsCategoryPO> insertList = new ArrayList<>();
        // 父级
        List<GoodsCategorySyncDTO> parentList = new ArrayList<>();
        // 末级
        List<GoodsCategorySyncDTO> lastList = new ArrayList<>();

        Map<String, Boolean> parentIdMap = new HashMap<>();
        for (DbcGoodsCategoryDTO dbcGoodsCategoryDTO : categoryDTOList) {
            String parent = dbcGoodsCategoryDTO.getParent();
            if (!parentIdMap.containsKey(parent)) {
                GoodsCategorySyncDTO parentDto = new GoodsCategorySyncDTO();
                parentDto.setId(Long.valueOf(dbcGoodsCategoryDTO.getParent()));
                parentDto.setCode(dbcGoodsCategoryDTO.getParent());
                parentDto.setName(dbcGoodsCategoryDTO.getParentName());
                parentDto.setLevel(2);
                parentDto.setDeleted(false);
                parentDto.setTenant(tenant);
                parentDto.setCreateTime(newTime);
                parentDto.setUpdateTime(newTime);
                parentDto.setCreateBy(username);
                parentDto.setUpdateBy(username);
                parentList.add(parentDto);
            }
            parentIdMap.put(parent, true);

            DbcGoodsCategoryPO po = new DbcGoodsCategoryPO();
            po.setCode(dbcGoodsCategoryDTO.getCode());
            po.setName(dbcGoodsCategoryDTO.getName());
            po.setOid(dbcGoodsCategoryDTO.getOid());
            po.setLev(dbcGoodsCategoryDTO.getLev());
            po.setParent(dbcGoodsCategoryDTO.getParent());
            po.setMarketId(marketId);
            po.setDeleted(false);
            po.setTenant(tenant);
            EntityAuditor.populateAudit(po, username, StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, newTime);
            insertList.add(po);

            GoodsCategorySyncDTO dto = new GoodsCategorySyncDTO();
            dto.setParentId(Long.valueOf(dbcGoodsCategoryDTO.getParent()));
            dto.setId(Long.valueOf(dbcGoodsCategoryDTO.getOid()));
            dto.setCode(dbcGoodsCategoryDTO.getCode());
            dto.setName(dbcGoodsCategoryDTO.getName());
            dto.setLevel(3);
            dto.setDeleted(false);
            dto.setTenant(tenant);
            dto.setCreateTime(newTime);
            dto.setUpdateTime(newTime);
            dto.setCreateBy(username);
            dto.setUpdateBy(username);

            lastList.add(dto);
        }

        // 先保存获取到的数据(保存同步记录)
        this.saveBatch(insertList, 1000);
        // 最后移除之前的
        this.remove(Wrappers.<DbcGoodsCategoryPO>lambdaQuery()
                            .lt(DbcGoodsCategoryPO::getCreateTime, DateUtils.offset(newTime, ChronoUnit.SECONDS, -1))
                   );
        List<GoodsCategorySyncDTO> paramList = new ArrayList<>();
        paramList.addAll(parentList);
        paramList.addAll(lastList);
        // 同步数据到mdata
        GoodsCategoryBatchRenewParam param = new GoodsCategoryBatchRenewParam();
        param.setList(paramList);
        goodsCategoryClient.thirdCategoryBatchRenew(param);

        log.warn("商品品类同步成功！" + lastList.size());
    }
}
