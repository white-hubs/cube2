package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectStandardItemCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardItemCommandServiceImpl extends ServiceImpl<InspectStandardItemMapper, InspectStandardItemPO> implements InspectStandardItemCommandService {

    @Override
    public void saveOrUpdate(Long standardId, List<InspectStandardItemCommandModel> standardItems) {
        List<Long> ids = null;
        if (CollectionUtils.isNotEmpty(standardItems)) {
            ids = standardItems.stream().map(InspectStandardItemCommandModel::getId).filter(Objects::nonNull).collect(Collectors.toList());
        }
        this.lambdaUpdate()
                .eq(InspectStandardItemPO::getStandardId, standardId)
                .notIn(CollectionUtils.isNotEmpty(ids), InspectStandardItemPO::getId, ids)
                .remove();

        List<InspectStandardItemPO> collect = Optional.ofNullable(standardItems).orElse(Collections.emptyList())
                .stream().map(item -> {
                    InspectStandardItemPO tempItem = ObjectUtils.copying(item, InspectStandardItemPO.class);
                    tempItem.setStandardId(standardId);
                    return tempItem;
                }).collect(Collectors.toList());
        this.saveOrUpdateBatch(collect);
    }
}
