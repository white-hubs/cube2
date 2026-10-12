package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectStandardItemQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardItemMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardItemVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardItemQueryServiceImpl extends ServiceImpl<InspectStandardItemMapper, InspectStandardItemPO> implements InspectStandardItemQueryService {

    @Override
    public List<InspectStandardItemVO> getListByStandardId(Long standardId) {
        List<InspectStandardItemPO> pos = this.lambdaQuery()
                .eq(InspectStandardItemPO::getStandardId, standardId)
                .list();
        return ObjectUtils.copying(pos, InspectStandardItemVO.class);
    }

}
