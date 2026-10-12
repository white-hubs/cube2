package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealListVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseAppealVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseAppealPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseAppealQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface EnterpriseAppealQueryService extends IService<EnterpriseAppealPO> {

    EnterpriseAppealVO getVoById(Long id);

    List<EnterpriseAppealListVO> getVOList(EnterpriseAppealQuery query);

    List<EnterpriseAppealListVO> getVOListByIds(List<Long> ids);

    Pagination<EnterpriseAppealListVO> getVOPage(EnterpriseAppealQuery query);

}
