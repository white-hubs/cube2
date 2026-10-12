package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerRelationship;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerRelationshipService;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerRelationshipMapper;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerRelationshipServiceImpl extends ServiceImpl<WholesalerRelationshipMapper, WholesalerRelationship> implements WholesalerRelationshipService {

    private final WholesalerRelationshipMapper mapper;
    @Override
    public void saveBatch(List<String> stallIds, String businessId, String businessType) {
        stallIds.forEach(stallId->{
            // 如果改档位已经做过，就跳过
            WholesalerRelationship one = mapper.selectOne(Wrappers.<WholesalerRelationship>lambdaQuery().eq(WholesalerRelationship::getBusinessId, businessId).eq(WholesalerRelationship::getStallId, stallId).eq(WholesalerRelationship::getType, businessType));
            if (one != null) return;
            //创建一个中间表，存放协议和档位之间的对应关系，多对多
            WholesalerRelationship relationship = new WholesalerRelationship();
            relationship.setBusinessId(Long.parseLong(businessId));
            relationship.setStallId(stallId);
            relationship.setType(businessType);
            relationship.setStatus(0);
            relationship.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            relationship.setCreateTime(LocalDateTime.now());
            mapper.insert(relationship);
        });
    }

    @Override
    public void removeBatch(String businessId, String businessType) {
        List<String> ids = this.lambdaQuery().eq(WholesalerRelationship::getBusinessId,businessId)
                .eq(WholesalerRelationship::getType,businessType)
                .list()
                .stream().map(WholesalerRelationship::getId).collect(Collectors.toList());
        if(!ids.isEmpty()){
            mapper.deleteBatchIds(ids);
        }

    }
}
