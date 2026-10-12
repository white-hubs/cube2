package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseInterviewVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseInterviewPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseInterviewQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface EnterpriseInterviewQueryService extends IService<EnterpriseInterviewPO> {

    EnterpriseInterviewVO getVoById(Long id);

    List<EnterpriseInterviewListVO> getVOList(EnterpriseInterviewQuery query);

    Pagination<EnterpriseInterviewListVO> getVOPage(EnterpriseInterviewQuery query);

}
