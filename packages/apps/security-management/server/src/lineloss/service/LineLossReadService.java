package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossReadQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossReadRecordVO;
import com.guhecloud.platform.base.model.Pagination;

public interface LineLossReadService {
    /**
     * 分页查询线损采集记录
     *
     * @param param 查询参数
     *
     * @return 分页结果
     */
    Pagination<LineLossReadRecordVO> pageLossRead(LineLossReadQueryParam param);
}
