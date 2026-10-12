package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardTypeReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardTypeReferMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardTypeReferVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardTypeReferQueryServiceImpl extends ServiceImpl<InspectStandardTypeReferMapper, InspectStandardTypeReferPO> implements InspectStandardTypeReferQueryService {

    @Override
    public List<InspectStandardTypeReferVO> getListByStandardId(Long standardId) {
        List<InspectStandardTypeReferPO> pos = this.lambdaQuery()
                .eq(InspectStandardTypeReferPO::getStandardId, standardId)
                .list();

        List<InspectStandardTypeReferVO> vos = new ArrayList<>();
        Optional.ofNullable(pos).orElse(Collections.emptyList())
                .stream().collect(Collectors.groupingBy(InspectStandardTypeReferPO::getTargetCategory))
                .forEach((targetCategory, ls) -> {
                    InspectStandardTypeReferVO vo = new InspectStandardTypeReferVO();
                    vo.setTargetCategory(targetCategory);
                    vo.setTargetTypes(ls.stream().map(InspectStandardTypeReferPO::getTargetType).collect(Collectors.toList()));
                    vos.add(vo);
                });

        return vos;
    }

    @Override
    public List<Long> getStandardIdsByTarget(String targetCategory, String targetType) {
        List<InspectStandardTypeReferPO> pos = this.lambdaQuery()
                .eq(InspectStandardTypeReferPO::getTargetCategory, targetCategory)
                .eq(InspectStandardTypeReferPO::getTargetType, targetType)
                .select(InspectStandardTypeReferPO::getStandardId)
                .list();

        return pos.stream().map(InspectStandardTypeReferPO::getStandardId).distinct().collect(Collectors.toList());
    }

}
