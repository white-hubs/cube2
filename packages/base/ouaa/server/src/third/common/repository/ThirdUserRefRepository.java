package com.guhecloud.cube.biz.ouaa.server.third.common.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.server.third.common.entity.ThirdUserRef;
import com.guhecloud.cube.biz.ouaa.server.third.common.enumeration.ThirdTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.third.common.repository.mapper.ThirdUserRefMapper;
import com.guhecloud.cube.biz.ouaa.server.third.common.repository.po.ThirdUserRefPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdUserRefRepository {

    private final ThirdUserRefMapper thirdUserRefMapper;

    @Transactional(rollbackFor = Exception.class)
    public Long saveThirdUserRef(ThirdUserRef thirdUserRef) {
        Optional<ThirdUserRef> isExistThirdUserRef = this.getThirdUserRef(thirdUserRef.getConfigId(),
                                                                          thirdUserRef.getType().name(),
                                                                          thirdUserRef.getUserId(),
                                                                          thirdUserRef.getTenant());
        ThirdUserRefPO thirdUserRefPO;
        if (isExistThirdUserRef.isPresent()) {
            ThirdUserRef userRef = isExistThirdUserRef.get();
            thirdUserRefPO = convertEntityTOPO(thirdUserRef);
            thirdUserRefPO.setId(userRef.getId());
            thirdUserRefPO.setDeleted(false);
            EntityAuditor.populateCreateAudit(thirdUserRefPO, userRef.getCreateBy(), userRef.getCreatorId(), userRef.getCreatorType(), userRef.getCreateTime());
            EntityAuditor.populateUpdateAudit(thirdUserRefPO);
            this.thirdUserRefMapper.updateById(thirdUserRefPO);
        } else {
            thirdUserRefPO = convertEntityTOPO(thirdUserRef);
            thirdUserRefPO.setDeleted(false);
            EntityAuditor.populateAudit(thirdUserRefPO);
            this.thirdUserRefMapper.insert(thirdUserRefPO);
        }
        return thirdUserRefPO.getId();
    }

    public Optional<ThirdUserRef> getThirdUserRef(Long configId, String type, Long userId, Integer tenant
                                                 ) {
        List<ThirdUserRefPO> thirdUserRefPOS = this.thirdUserRefMapper
                .selectList(Wrappers.<ThirdUserRefPO>lambdaQuery()
                                    .eq(ThirdUserRefPO::getConfigId, configId)
                                    .eq(ThirdUserRefPO::getType, type)
                                    .eq(ThirdUserRefPO::getUserId, userId)
                                    .eq(Objects.nonNull(tenant), ThirdUserRefPO::getTenant, tenant));
        if (CollectionUtils.isEmpty(thirdUserRefPOS)) {
            return Optional.empty();
        }
        ThirdUserRefPO thirdUserRefPO = thirdUserRefPOS.getFirst();
        return Optional.of(convertPOToEntity(thirdUserRefPO));
    }

    private ThirdUserRef convertPOToEntity(ThirdUserRefPO thirdUserRefPO) {
        return new ThirdUserRef(thirdUserRefPO.getId(),
                                ThirdTypeEnum.valueOf(thirdUserRefPO.getType()),
                                thirdUserRefPO.getConfigId(),
                                thirdUserRefPO.getUserId(),
                                thirdUserRefPO.getRefId(),
                                thirdUserRefPO.getTenant(),
                                thirdUserRefPO.getCreateBy(),
                                thirdUserRefPO.getCreatorId(),
                                thirdUserRefPO.getCreatorType(),
                                thirdUserRefPO.getCreateTime(),
                                thirdUserRefPO.getUpdateBy(),
                                thirdUserRefPO.getUpdaterId(),
                                thirdUserRefPO.getUpdaterType(),
                                thirdUserRefPO.getUpdateTime());
    }

    private ThirdUserRefPO convertEntityTOPO(ThirdUserRef thirdUserRef) {
        return ThirdUserRefPO.builder()
                .id(thirdUserRef.getId())
                .type(thirdUserRef.getType().name())
                .configId(thirdUserRef.getConfigId())
                .userId(thirdUserRef.getUserId())
                .refId(thirdUserRef.getRefId())
                .tenant(thirdUserRef.getTenant())
                .build();
    }
}
