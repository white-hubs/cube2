package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardItemVO;

import java.util.List;

public interface InspectStandardItemQueryService extends IService<InspectStandardItemPO> {

    List<InspectStandardItemVO> getListByStandardId(Long standardId);

}
