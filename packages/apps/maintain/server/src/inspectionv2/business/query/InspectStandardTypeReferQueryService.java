package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardTypeReferVO;

import java.util.List;

public interface InspectStandardTypeReferQueryService extends IService<InspectStandardTypeReferPO> {

    List<InspectStandardTypeReferVO> getListByStandardId(Long standardId);

    List<Long> getStandardIdsByTarget(String targetCategory, String targetType);

}
