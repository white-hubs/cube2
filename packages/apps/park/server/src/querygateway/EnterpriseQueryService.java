package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseDirectoryVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.EnterpriseQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface EnterpriseQueryService extends IService<EnterprisePO> {

    EnterpriseVO getVoById(Long id);

    List<EnterpriseVO> getVOList(EnterpriseQuery query);

    Pagination<EnterpriseVO> getVOPage(EnterpriseQuery query);

    Pagination<EnterpriseDirectoryVO> getVOPageByDirectory(EnterpriseQuery query);

}
