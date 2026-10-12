package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardTypeReferCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardTypeReferMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardTypeReferCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardTypeReferCommandServiceImpl extends ServiceImpl<InspectStandardTypeReferMapper, InspectStandardTypeReferPO> implements InspectStandardTypeReferCommandService {

    @Override
    public void saveOrUpdate(Long standardId, List<InspectStandardTypeReferCommandModel> standardTypeRefers) {
        Map<String, String> defaultTypeMap = new HashMap<>();
        List<InspectStandardTypeReferPO> typeReferPOList = this.lambdaQuery().eq(InspectStandardTypeReferPO::getStandardId, standardId)
                .list();
        if (!CollectionUtils.isEmpty(typeReferPOList)) {
            typeReferPOList.forEach(po -> defaultTypeMap.put(po.getTargetCategory() + po.getTargetType(), po.getDefaultStandard()));
        }
        this.lambdaUpdate()
                .eq(InspectStandardTypeReferPO::getStandardId, standardId)
                .remove();

        Optional.ofNullable(standardTypeRefers).orElse(Collections.emptyList())
                .forEach(typeRefer -> {
                    List<InspectStandardTypeReferPO> collect = Optional.ofNullable(typeRefer.getTargetTypes())
                            .orElse(Collections.emptyList())
                            .stream().map(targetType -> {
                                InspectStandardTypeReferPO po = InspectStandardTypeReferPO.builder()
                                        .standardId(standardId)
                                        .targetCategory(typeRefer.getTargetCategory())
                                        .targetType(targetType)
                                        .defaultStandard(defaultTypeMap.get(typeRefer.getTargetCategory() + targetType))
                                        .build();
                                return po;
                            }).collect(Collectors.toList());

                    this.saveBatch(collect);
                });

    }

    @Override
    public void removeByStandardId(Long standardId) {
        List<InspectStandardTypeReferPO> pos = this.lambdaQuery()
                .eq(InspectStandardTypeReferPO::getStandardId, standardId)
                .list();
        DatabaseContextHolder.softDelete(this, pos);
    }
}
